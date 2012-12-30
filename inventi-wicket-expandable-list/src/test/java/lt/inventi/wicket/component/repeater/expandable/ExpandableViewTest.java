package lt.inventi.wicket.component.repeater.expandable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Test;

import lt.inventi.wicket.test.BaseNonInjectedTest;

public class ExpandableViewTest extends BaseNonInjectedTest {

    private ArrayList<String> values;

    @Before
    public void before(){
        values = new ArrayList<String>();
        values.add("test1");
        values.add("test2");
    }

    @Test
    public void shouldContainUpdatedModelItems() {
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);
        TestView view = panel.getTestView();

        view.onBeforeRender();
        values.add("test3");
        view.onBeforeRender();
        values.add("test4");
        view.onBeforeRender();

        Iterator<Item<String>> items = view.getItems();
        int size = 0;
        while(items.hasNext()){
            items.next();
            size++;
        }
        assertEquals(4, size);
    }

    @Test
    public void testComponentInit(){
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);
        TestView view = panel.getTestView();

        assertEquals(ReuseExistingItemsStrategy.class, view.getItemReuseStrategy().getClass());

        Iterator<Item<String>> items = view.getItems();
        Item<String> item = items.next();
        assertEquals("test1", item.getModelObject());
        assertEquals("test2", items.next().getModelObject());

        assertEquals(CompoundPropertyModel.class, item.getModel().getClass());
    }

    @Test
    public void shouldAddItemsToTheList() {
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(panel.getAddItemLink(), "click");

        assertThat(values, contains("test1", "test2", "test3"));
        assertThat(panel.getTestView().get("2:field").getOutputMarkupId(), is(true));

        String addElementScript = "var item=document.createElement('span');item.id='test3';$('#form4').append(item);";
        String focusScript = "Wicket.Focus.setFocusOnId('test3_field');";
        checkScripts(addElementScript, focusScript);

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", "test3", "test3"));
    }

    @Test
    public void shouldAddAndRemoveItems() {
        TestPanel panel = new TestPanel();
        //TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);

        // add third item
        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", "test3"));

        // remove second item
        tester.executeAjaxEvent(panel.getTestView().get("1:remove"), "click");
        assertThat(values, contains("test1", "test3"));

        FormTester formTester = tester.newFormTester("test:form");
        formTester.setValue("view:2:field", "newValue");
        formTester.submit();

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "newValue", "test3"));
    }

    @Test
    public void shouldRemoveItemsFromTheList() {
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", "test3"));
        String focusScript = "Wicket.Focus.setFocusOnId('test2_field');";

        // remove last item
        tester.executeAjaxEvent(panel.getTestView().get("2:remove"), "click");
        assertThat(values, contains("test1", "test2"));
        checkScripts("$('#test3').remove()", focusScript);

        // remove first item
        tester.executeAjaxEvent(panel.getTestView().get("0:remove"), "click");
        assertThat(values, contains("test2"));
        checkScripts("$('#test1').remove()", focusScript);
    }

    @Test
    public void shouldAddAnItemAndRemoveTheFirstTwo() {
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", "test3"));

        tester.executeAjaxEvent(panel.getTestView().get("0:remove"), "click");
        assertThat(values, contains("test2", "test3"));
        tester.executeAjaxEvent(panel.getTestView().get("1:remove"), "click");
        assertThat(values, contains("test3"));
    }

    @Test
    public void shouldReplaceItemsInTheBackingListModel() {
        TestPanel panel = new TestPanel();
        tester.startComponentInPage(panel);

        FormTester formTester = tester.newFormTester("test:form");
        formTester.setValue("view:0:field", "newValue");
        formTester.submit();

        assertThat(values, contains("newValue", "test2"));
    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldNotReplaceItemsInTheBackingNonListModel() {
        Set<String> nonListValues = new HashSet<String>();
        nonListValues.add("1");
        IModel model = Model.of(nonListValues);

        TestPanel panel = new TestPanel(model);
        tester.startComponentInPage(panel);

        FormTester formTester = tester.newFormTester("test:form");
        formTester.setValue("view:0:field", "newValue");
        formTester.submit();

        assertThat(nonListValues, contains("1"));
    }

    private void checkScripts(String... scripts) {
        MockHttpServletResponse response = tester.getLastResponse();
        for (String s : scripts) {
            assertThat(response.getDocument(), containsString(s));
        }
    }

    public class TestPanel extends Panel {
        private final IModel<? extends Iterable<String>> model;

        TestPanel() {
            super("test");
            this.model = new PropertyModel<List<String>>(ExpandableViewTest.this, "values");
        }

        TestPanel(IModel<? extends Iterable<String>> model) {
            super("test");
            this.model = model;
        }

        public Component getAddItemLink() {
            return get("form:add");
        }

        public TestView getTestView() {
            return (TestView) get("form:view");
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            add(new Form<Void>("form").add(
                new TestView(this.model),
                new AddNewItemLink("add") {
                    @Override
                    protected String onAddNewItem() {
                        values.add("test3");
                        return "test3";
                    }
                }
            ));
        }
    }

    public class TestView extends ExpandableView<String> {
        public TestView(IModel<? extends Iterable<String>> model) {
            super("view", model);
        }

        @Override
        protected void populateItem(final Item<String> item) {
            item.setMarkupId(item.getModelObject());
            item.add(new TextField<String>("field", item.getModel()).setMarkupId(item.getModelObject() + "_field"));
            RemoveItemLink<String> removeItemLink = new RemoveItemLink<String>("remove", item) {
                @Override
                protected void onRemoveItem(Item<String> linkItem, AjaxRequestTarget target) {
                    values.remove(linkItem.getModelObject());
                }
            };
            item.add(removeItemLink);
        }

        @Override
        protected void onBeforeRender(){
            super.onBeforeRender();
        }
    }

}
