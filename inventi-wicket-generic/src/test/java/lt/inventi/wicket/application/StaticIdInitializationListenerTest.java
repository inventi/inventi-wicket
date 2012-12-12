package lt.inventi.wicket.application;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.mock.MockServletContext;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class StaticIdInitializationListenerTest {

    @Test
    public void testStaticId(){

        WebApplication app = new WebApplication() {
            @Override
            public Class<? extends Page> getHomePage() {
                return null;
            }
        };
        app.getComponentInitializationListeners()
        .add(new StaticIdInitializationListener());

        MockServletContext context = new MockServletContext(app , "");
        WicketTester test = new WicketTester(app, context);

        Panel panel = test.startComponentInPage(StaticIdPanel.class);
        Assert.assertTrue(panel.getOutputMarkupId());

        Component textField = panel.get("textField");
        Assert.assertTrue(textField.getOutputMarkupId());
        Assert.assertEquals("textField", textField.getMarkupId());

        Component list = panel.get("list");
        Assert.assertTrue(list.getOutputMarkupId());
        Assert.assertEquals("list", list.getMarkupId());

        Component field1 = panel.get("list:0:textField");
        Assert.assertTrue( field1.getOutputMarkupId());
        Assert.assertEquals("list_0_textField", field1.getMarkupId());

        Component field2 = panel.get("list:1:textField");
        Assert.assertTrue( field2.getOutputMarkupId());
        Assert.assertEquals("list_1_textField", field2.getMarkupId());

        Component field3 = panel.get("list:1:list2:1:textField2");
        Assert.assertTrue(field3.getOutputMarkupId());
        Assert.assertEquals("list_1_list2_1_textField2", field3.getMarkupId());
    }

    public static class StaticIdPanel extends Panel{

        public StaticIdPanel(String id) {
            super(id);
            add(new TextField<String>("textField"));

            final List<String> list = new ArrayList<String>();
            list.add("test1");
            list.add("test2");
            add(new ListView<String>("list", list) {
                @Override
                protected void populateItem(ListItem<String> item) {
                    item.add(new TextField<String>("textField"));
                    item.add(new ListView<String>("list2", list) {
                        @Override
                        protected void populateItem(ListItem<String> item2) {
                            item2.add(new TextField<String>("textField2"));
                        }
                    });
                }
            });
        }
    }
}
