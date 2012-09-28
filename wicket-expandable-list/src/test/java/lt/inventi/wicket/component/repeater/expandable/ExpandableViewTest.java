package lt.inventi.wicket.component.repeater.expandable;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lt.inventi.wicket.test.BaseNonInjectedTest;

import org.apache.wicket.ajax.AjaxRequestHandler;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.util.tester.DummyHomePage;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ExpandableViewTest extends BaseNonInjectedTest {

    private TestView view;
    private ArrayList<String> model;
    private DummyHomePage page;
    private AddNewItemLink addItemLink;

    @Before
    public void before(){
        page = new DummyHomePage();
        WebMarkupContainer container = new WebMarkupContainer("table");
        container.setMarkupId("table");
        page.add(container);

        model = new ArrayList<String>();
        model.add("test1");
        model.add("test2");
        // view.setDefaultModel();

        IModel<List<String>> tmpModel = new IModel<List<String>>() {

            @Override
            public void detach() {
            }

            @Override
            public List<String> getObject() {
                return model;
            }

            @Override
            public void setObject(List<String> object) {
            }
        };
        view = new TestView("testId", tmpModel);

        addItemLink = new AddNewItemLink("add") {
            @Override
            protected String onAddNewItem() {
                model.add("test3");
                return "test3";
            }
        };
        page.add(addItemLink);


        tester.startComponent(view);
        view.onInitialize();

        container.add(view);
    }

    @Test
    public void testAddItem(){
        AjaxRequestTarget target = new AjaxRequestHandler(page);
        RequestCycle.get().scheduleRequestHandlerAfterCurrent(target);

        addItemLink.onClick(target);
        assertEquals("test3", model.get(2));
        assertTrue(view.get("2").get("field").getOutputMarkupId());

        String addElementScript = "var item=document.createElement('span');item.id='test3';$('#table').append(item);";
        String focusScript = "Wicket.Focus.setFocusOnId('test3_field');";

        //        assertTrue(target.toString().indexOf(addElementScript) > -1);
        //        assertTrue(target.toString().indexOf(focusScript) > -1);
    }


    @Test
    @Ignore("TODO: started to fail, why?")
    public void testRemoveItem(){
        AjaxRequestTarget target = new AjaxRequestHandler(page);
        addItemLink.onClick(target);

        target = new AjaxRequestHandler(page);
        RemoveItemLink link = (RemoveItemLink)view.get("2:remove");
        link.onClick(target);
        assertEquals(2, model.size());

        String removeItemScript = "$('#test3').remove();";
        String focusScript = "Wicket.Focus.setFocusOnId('test2_field');";
        assertTrue(target.toString().indexOf(removeItemScript) > -1);
        assertTrue(target.toString().indexOf(focusScript) > -1);

        target = new AjaxRequestHandler(page);
        link = (RemoveItemLink)view.get("0:remove");
        link.onClick(target);
        assertEquals(1, model.size());
        removeItemScript = "$('#test1').remove();";
        assertTrue(target.toString().indexOf(removeItemScript) > -1);
        assertTrue(target.toString().indexOf(focusScript) > -1);
    }


    @Test
    public void testNewItem(){
        view.onBeforeRender();
        model.add("test3");
        view.onBeforeRender();
        model.add("test4");
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
        assertEquals(ReuseExistingItemsStrategy.class,
                view.getItemReuseStrategy().getClass());

        Iterator<Item<String>> items = view.getItems();
        Item<String> item = items.next();
        assertEquals("test1", item.getModelObject());
        assertEquals("test2", items.next().getModelObject());

        assertEquals(CompoundPropertyModel.class, item.getModel().getClass());
    }

    public class TestView extends ExpandableView<String> {

        public TestView(String id, IModel<List<String>> model) {
            super(id, model);
        }

        @Override
        protected void populateItem(final Item<String> item) {
            item.setMarkupId(item.getModelObject());
            item.add(new TextField<String>("field", item.getModel())
                    .setMarkupId(item.getModelObject()+"_field"));
            RemoveItemLink<String> removeItemLink = new RemoveItemLink<String>("remove", item) {
                @Override
                protected void onRemoveItem(Item<String> linkItem, AjaxRequestTarget target) {
                    model.remove(linkItem.getModelObject());
                }
            };
            item.add(removeItemLink);
        }

        public void reset(){
            setFlag(FLAG_INITIALIZED, true);
        }

        @Override
        protected void onBeforeRender(){
            super.onBeforeRender();
        }
    }

}
