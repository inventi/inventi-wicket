package lt.inventi.wicket.component.repeater.expandable;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
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
    public void outputsItemMarkupIds() {
        IModel<List<String>> model = new PropertyModel<List<String>>(this, "values");
        ExpandableView<String> panel = new ExpandableView<String>("test", model) {
            @Override
            protected void populateItem(Item<String> item) {
                // do nothing
            }
        };
        tester.startComponentInPage(panel);

        Iterator<Item<String>> items = panel.getItems();
        while (items.hasNext()) {
            assertThat(items.next().getOutputMarkupId(), is(true));
        }
    }

    @Test
    public void shouldContainUpdatedModelItems() {
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
        tester.startComponentInPage(panel);
        TestView view = panel.getTestView();

        view.onBeforeRender();
        values.add("test3");
        view.onBeforeRender();
        values.add("test4");
        view.onBeforeRender();

        assertEquals(4, numberOfItems(view));
    }

    @Test
    public void handlesModelObjectChanges() {
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
        tester.startComponentInPage(panel);
        TestView view = panel.getTestView();

        values.remove(0);
        view.onBeforeRender();

        assertThat(numberOfItems(view), is(1));
    }

    @Test
    public void shouldAddNonNullItemsToTheList() {
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
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
    public void addsNullItemsToTheEndOfTheList() {
        NullItemTestPanel panel = new NullItemTestPanel();
        tester.startComponentInPage(panel);
        tester.executeAjaxEvent(panel.getAddItemLink(), "click");

        assertThat(values, contains("test1", "test2", null));
        assertThat(panel.getTestView().size(), is(3));

        String addElementScript = "var item=document.createElement('span');item.id='null';$('#form4').append(item);";
        String focusScript = "Wicket.Focus.setFocusOnId('null_field');";
        checkScripts(addElementScript, focusScript);

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", null, null));
        assertThat(panel.getTestView().size(), is(4));

        // this is a degenerate case. We shouldn't be setting markup ids equal to model object values
        addElementScript = "var item=document.createElement('span');item.id='null';$('#form4').append(item);";
        focusScript = "Wicket.Focus.setFocusOnId('null_field');";
        checkScripts(addElementScript, focusScript);
    }

    @Test
    public void removesNullItemsUsingRemoveItemLink() {
        NullItemTestPanel panel = new NullItemTestPanel();
        tester.startComponentInPage(panel);

        tester.clickLink(panel.getAddItemLink());
        tester.clickLink(panel.getAddItemLink());
        assertThat(values, contains("test1", "test2", null, null));

        tester.executeAjaxEvent(panel.getTestView().get("2:remove"), "click");
        assertThat(values, contains("test1", "test2", null));
    }

    @Test
    public void shouldAddAndRemoveItems() {
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
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
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
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
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
        tester.startComponentInPage(panel);

        tester.executeAjaxEvent(panel.getAddItemLink(), "click");
        assertThat(values, contains("test1", "test2", "test3"));
        assertThat(panel.getTestView().size(), is(3));

        tester.executeAjaxEvent(panel.getTestView().get("0:remove"), "click");
        assertThat(values, contains("test2", "test3"));
        tester.executeAjaxEvent(panel.getTestView().get("1:remove"), "click");
        assertThat(values, contains("test3"));
    }

    @Test
    public void shouldReplaceItemsInTheBackingListModel() {
        NonNullItemTestPanel panel = new NonNullItemTestPanel();
        tester.startComponentInPage(panel);

        FormTester formTester = tester.newFormTester("test:form");
        formTester.setValue("view:0:field", "newValue");
        formTester.submit();

        assertThat(values, contains("newValue", "test2"));
    }

    private void checkScripts(String... scripts) {
        MockHttpServletResponse response = tester.getLastResponse();
        for (String s : scripts) {
            assertThat(response.getDocument(), containsString(s));
        }
    }

    public abstract class TestPanel extends Panel {
        private final IModel<? extends List<String>> model;

        protected TestPanel() {
            super("test");
            this.model = new PropertyModel<List<String>>(ExpandableViewTest.this, "values");
        }

        protected TestPanel(IModel<? extends List<String>> model) {
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

            TestView view = new TestView(this.model);
            add(new Form<Void>("form").add(view, newItemLink("add", view)));
        }

        protected abstract AddNewItemLink<String> newItemLink(String id, ExpandableView<String> view);
    }

    public class NonNullItemTestPanel extends TestPanel {

        public NonNullItemTestPanel() {
            super();
        }

        public NonNullItemTestPanel(IModel<? extends List<String>> model) {
            super(model);
        }

        @Override
        protected AddNewItemLink<String> newItemLink(String id, ExpandableView<String> view) {
            return new AutoAddNewItemLink<String>(id, view) {
                @Override
                protected String createNewItem() {
                    return "test3";
                }
            };
        }
    }

    public class NullItemTestPanel extends TestPanel {
        public NullItemTestPanel() {
            super();
        }

        public NullItemTestPanel(IModel<? extends List<String>> model) {
            super(model);
        }

        @Override
        protected AddNewItemLink<String> newItemLink(String id, ExpandableView<String> view) {
            return new AutoAddNewItemLink<String>(id, view) {
                @Override
                protected String createNewItem() {
                    return null;
                }
            };
        }
    }

    public class TestView extends ExpandableView<String> {
        public TestView(IModel<? extends List<String>> model) {
            super("view", model);
        }

        @Override
        protected void populateItem(final Item<String> item) {
            item.setMarkupId(String.valueOf(item.getModelObject()));
            item.add(new TextField<String>("field", item.getModel()).setMarkupId(item.getModelObject() + "_field"));
            item.add(new AutoRemoveItemLink<String>("remove", item));
        }

        @Override
        protected void onBeforeRender(){
            super.onBeforeRender();
        }
    }

    private static int numberOfItems(TestView view) {
        Iterator<Item<String>> items = view.getItems();
        int size = 0;
        while (items.hasNext()) {
            items.next();
            size++;
        }
        return size;
    }

}
