package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbPageParameters;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsSettings;
import lt.inventi.wicket.component.breadcrumb.h.IProvideTitle;

public class BreadcrumbsPanelTest {

    private WicketTester tester = new WicketTester();

    @Before
    public void setUp() {
        new BreadcrumbsSettings().install(tester.getApplication());
    }

    @Test
    public void populatesPanelWithBreadcrumbs() {
        tester.startPage(new TestPage());
        tester.assertRenderedPage(TestPage.class);

        tester.assertLabel("crumbs:crumbs:0:link:title", "TestPage");
        tester.assertDisabled("crumbs:crumbs:0:link:title");

        // completely new breadcrumb trail created for another page
        TestPage lastPage = tester.startPage(new TestPage());

        tester.assertLabel("crumbs:crumbs:0:link:title", "TestPage");
        tester.assertDisabled("crumbs:crumbs:0:link:title");

        PageParameters params = BreadcrumbPageParameters.setTrailTo(new PageParameters(), lastPage);
        tester.startPage(new TestPage(params));

        tester.assertLabel("crumbs:crumbs:0:link:title", "TestPage");
        tester.assertEnabled("crumbs:crumbs:0:link:title");
        tester.assertLabel("crumbs:crumbs:1:link:title", "TestPage");
        tester.assertDisabled("crumbs:crumbs:1:link:title");
    }

    private static class TestPage extends WebPage implements IProvideTitle {
        public TestPage() {
            super();
        }

        public TestPage(PageParameters parameters) {
            super(parameters);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(new BreadcrumbsPanel("crumbs"));
        }

        @Override
        public IMarkupFragment getMarkup() {
            return Markup.of("<div wicket:id=\"crumbs\"></div>");
        }

        @Override
        public IModel<String> getTitle() {
            return Model.of(getClass().getSimpleName());
        }
    }
}
