package lt.inventi.wicket.component.autocomplete;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
import org.apache.wicket.protocol.http.mock.MockHttpServletResponse;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import lt.inventi.wicket.component.autocomplete.Autocomplete.ValueField;
import lt.inventi.wicket.resource.ResourceSettings;
import lt.inventi.wicket.test.BaseNonInjectedTest;

public class AutoCompleteTest extends BaseNonInjectedTest {

    private Autocomplete<Long, TestObject, SearchObject> autoComplete;
    private Panel panel;
    private List<SearchObject> list;
    private IModel<TestObject> model;

    @Before
    public void setUp(){
        ResourceSettings.installEmpty(tester.getApplication());

        list = new ArrayList<SearchObject>();
        list.add(new SearchObject("1", "SomeName", "SomeDescription"));
        list.add(new SearchObject("2", "OldName", "OldDescription"));
        list.add(new SearchObject("3", "OtherName", "OtherDescription"));

        model = new Model<TestObject>(new TestObject(2L, "OldName"));
        autoComplete = new Autocomplete<Long, TestObject, SearchObject>("autoComplete", model);
        autoComplete.setSearchProvider(new SearchProvider());
        autoComplete.setDataProvider(new DataProvider());
        autoComplete.setDataLabelProvider(new DataLabelProvider());
        autoComplete.setNewItemHandler(new NewItemProvider());

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

        MockHttpServletResponse response = tester.getLastResponse();
        assertThat(response.getContentType(), equalTo("application/json; charset=utf-8"));
        assertThat(response.getHeader("Expires"), equalTo("Mon, 26 Jul 1997 05:00:00 GMT"));
        assertThat(response.getHeader("Cache-Control"), equalTo("no-cache, must-revalidate"));
        assertThat(response.getHeader("Pragma"), equalTo("no-cache"));

        JSONArray items = JSONArray.fromObject(response.getDocument());
        assertThat(((JSONObject) items.get(0)).get("id").toString(), equalTo("1"));
        assertThat(((JSONObject) items.get(0)).get("label").toString(), equalTo("SomeName - SomeDescription"));
        assertThat(((JSONObject) items.get(1)).get("id").toString(), equalTo("2"));
        assertThat(((JSONObject) items.get(1)).get("label").toString(), equalTo("OldName - OldDescription"));
    }

    @Test
    public void rerendersValueFieldInput() {
        tester.newFormTester("panel:form")
            .setValue("autoComplete:id", "3")
            .submit();

        assertThat(model.getObject().id, is(3L));
        assertThat(autoComplete.getValueField().getModelObject(), equalTo("OtherName"));

        autoComplete.clearAllInput();
        panel.render();

        assertThat(autoComplete.getValueField().getModelObject(), equalTo("OtherName"));
    }

    @Test
    public void testStructure(){
        tester.assertComponent("panel:form:autoComplete:value", ValueField.class);
        tester.assertComponent("panel:form:autoComplete:id", HiddenField.class);
        tester.assertComponent("panel:form:autoComplete:addNew", SubmitLink.class);
        tester.assertVisible("panel:form:autoComplete:addNew");

        autoComplete.disallowAddingNewItems();
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
        //list.add(newObj);

        tester.startComponent(autoComplete);

        assertEquals(newObj.getId(), autoComplete.getIdField().getInput());
    }

    @Test
    public void testSubmit(){
        FormTester formTest = tester.newFormTester("panel:form");
        formTest.setValue("autoComplete:value", "test");
        formTest.setValue("autoComplete:id", "3");
        formTest.submit();

        assertThat(list.get(2).id, equalTo(model.getObject().id.toString()));
        assertThat(list.get(2).name, equalTo(model.getObject().name));
    }

    private static class TestObject implements Serializable {
        private Long id;
        private String name;

        public TestObject(Long id, String name){
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }
    }

    private static class SearchObject {
        final String id;
        final String name;
        final String description;

        public SearchObject(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }


    private class TestPanel extends Panel{
        public TestPanel(String id) {
            super(id);
        }
    }

    private class NewItemProvider implements AddNewItemHandler<TestObject> {
        @Override
        public void onNewItem(String input, NewAutocompleteItemCallback<TestObject> callback) {

        }
    }

    private class DataProvider extends AbstractDataProvider<TestObject> {
        @Override
        public String getId(TestObject object) {
            return ""+object.getId();
        }

        @Override
        protected TestObject doLoadById(String id) {
            for (SearchObject o : list) {
                if (o.id.equals(id)) {
                    return new TestObject(Long.valueOf(id), o.name);
                }
            }
            throw new IllegalStateException("Unknown id " + id);
        }
    }

    private class SearchProvider extends AbstractSearchProvider<SearchObject> {
        @Override
        public List<SearchObject> searchItems(String query, int size) {
            return list;
        }

        @Override
        public String extractLabel(SearchObject item) {
            return item.name + " - " + item.description;
        }

        @Override
        protected String extractId(SearchObject item) {
            return item.id;
        }
    }

    public class DataLabelProvider implements AutocompleteDataLabelProvider<TestObject> {
        @Override
        public String extractLabel(TestObject item) {
            return item.name;
        }
    }
}
