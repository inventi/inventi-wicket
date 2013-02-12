package lt.inventi.wicket.component.bootstrap.tab;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.extensions.markup.html.tabs.AbstractTab;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.StringValue;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

public class RememberingTabbedPanelTest {

    private static final List<AbstractTab> TABS = Arrays.asList(new AbstractTab(Model.of("First")) {
        @Override
        public WebMarkupContainer getPanel(String panelId) {
            return new TestPanel(panelId);
        }
    }, new AbstractTab(Model.of("Second")) {
        @Override
        public WebMarkupContainer getPanel(String panelId) {
            return new TestPanel(panelId);
        }
    });

    private WicketTester tester = new WicketTester();
    private Markup markup = Markup.of("<html><head></head><body><div wicket:id=\"tabs\"></div></body></html>");

    @Test
    public void setsCurrentTabIndexIntoPageParameters() {
        RememberingTabbedPanel<AbstractTab> panel = new RememberingTabbedPanel<AbstractTab>("tabs", TABS);
        tester.startComponentInPage(panel, markup);

        StringValue tabParam = tester.getLastRenderedPage().getPageParameters().get("tabs");
        assertThat(tabParam.toString(), is(nullValue()));
        assertThat(panel.getSelectedTab(), is(0));

        tester.clickLink("tabs:tabs-container:tabs:1:link");

        tabParam = tester.getLastRenderedPage().getPageParameters().get("tabs");
        assertThat(tabParam.toString(), equalTo("1"));
        assertThat(panel.getSelectedTab(), is(1));
    }

    private static class TestPanel extends Panel {
        public TestPanel(String id) {
            super(id);
        }
    }
}
