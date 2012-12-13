package lt.inventi.wicket.component.autocomplete;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lt.inventi.wicket.component.autocomplete.Autocomplete.ValueField;
import lt.inventi.wicket.resource.ResourceSettings;
import lt.inventi.wicket.test.BaseNonInjectedTest;

public class AutoCompleteTest extends BaseNonInjectedTest {

    private Autocomplete<Long, TestObject> autoComplete;
    private Panel panel;
    private List<TestObject> list;
    private IModel<TestObject> model;

    private AutocompleteDataProvider<TestObject> dataProvider;

    @Before
    public void setUp(){
        ResourceSettings.installEmpty(tester.getApplication());

        list = new ArrayList<TestObject>();
        TestObject oldObj = new TestObject(2L, "OldName");
        list.add(oldObj);
        list.add(new TestObject(3L, "OtherName"));

        model = new Model<TestObject>(oldObj);
        dataProvider = new TestProvider();
        autoComplete = new Autocomplete<Long, TestObject>("autoComplete", model, dataProvider);

        panel = new TestPanel("panel");
        Form<TestObject> form = new Form<TestObject>("form", new Model<TestObject>());
        form.add(autoComplete);
        panel.add(form);

        tester.startComponentInPage(panel);
    }

    @Test
    public void testQuery(){

        tester.getRequest().getPostParameters().addParameterValue("term", "test text");
        tester.getRequest().getPostParameters().addParameterValue("limit", "10");

        autoComplete.onQuery();
        tester.processRequest(tester.getRequestCycle().getRequestHandlerScheduledAfterCurrent());

        assertEquals("application/json; charset=utf-8", tester.getLastResponse().getContentType());
        assertEquals("Mon, 26 Jul 1997 05:00:00 GMT", tester.getLastResponse().getHeader("Expires"));
        assertEquals("no-cache, must-revalidate", tester.getLastResponse().getHeader("Cache-Control"));
        assertEquals("no-cache", tester.getLastResponse().getHeader("Pragma"));


        JSONArray items = JSONArray.fromObject(tester.getLastResponse().getDocument());

        assertEquals(String.valueOf(2), ((JSONObject)items.get(0)).get("id"));
        assertEquals("OldName", ((JSONObject)items.get(0)).get("label"));
    }

    @Test
    public void testStructure(){
        tester.assertComponent("panel:form:autoComplete:value", ValueField.class);
        tester.assertComponent("panel:form:autoComplete:id", HiddenField.class);
        tester.assertComponent("panel:form:autoComplete:addNew", SubmitLink.class);
        tester.assertVisible("panel:form:autoComplete:addNew");

        autoComplete.setAddNewLinkEnabled(false);
        tester.startComponentInPage(panel);
        tester.assertInvisible("panel:form:autoComplete:addNew");
    }

    @Test
    public void testClearInput() {
        FormTester formTest = tester.newFormTester("panel:form");
        formTest.setValue("autoComplete:value", "test");
        formTest.setValue("autoComplete:id", "otherId");
        autoComplete.getValueField().inputChanged();
        autoComplete.getIdField().inputChanged();
        assertNotNull(autoComplete.getValueField().getRawInput());
        assertNotNull(autoComplete.getIdField().getRawInput());

        autoComplete.clearAllInput();

        assertNull(autoComplete.getValueField().getRawInput());
        assertNull(autoComplete.getIdField().getRawInput());
    }

    @Test
    @Ignore
    public void testNewItem(){
        //click addNew button
        Component cmp = tester.getComponentFromLastRenderedPage("panel:form:autoComplete:addNew");
        ((SubmitLink)cmp).onSubmit();

        //create new item
        TestObject newObj = new TestObject(1L, "NewName");
        list.add(newObj);

        tester.startComponent(autoComplete);

        assertEquals(newObj.getId(), autoComplete.getIdField().getInput());
    }

    @Test
    public void testSubmit(){
        FormTester formTest = tester.newFormTester("panel:form");
        formTest.setValue("autoComplete:value", "test");
        formTest.setValue("autoComplete:id", "3");
        formTest.submit();

        assertEquals(list.get(1).getId(), model.getObject().getId());
    }


    private class TestObject implements Serializable{
        private Long id;
        private String name;

        public TestObject(Long id, String name){
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }
    }


    private class TestPanel extends Panel{
        public TestPanel(String id) {
            super(id);
        }
    }


    public  class TestProvider extends AbstractDataProvider<TestObject>{

        @Override
        public TestObject getObject(String id, String value, TestObject oldItem) {
            for(TestObject obj : list){
                if(obj.getId().equals(Long.valueOf(id))){
                    return obj;
                }
            }
            return null;
        }

        @Override
        public String getValue(TestObject object) {
            return object.getName();
        }

        @Override
        public String getId(TestObject object) {
            return ""+object.getId();
        }

        @Override
        public List<TestObject> searchItems(String query, int size) {
            return list;
        }

        @Override
        protected TestObject doLoadById(String id) {
            return null;
        }
    }
}
