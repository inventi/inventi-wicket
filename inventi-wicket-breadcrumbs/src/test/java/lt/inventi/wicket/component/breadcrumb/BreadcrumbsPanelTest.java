package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbPageParameters;

public class BreadcrumbsPanelTest extends BreadcrumbsTests {

    @Test
    public void populatesPanelWithBreadcrumbs() {
        tester.startPage(new PanelTestPage());
        tester.assertRenderedPage(PanelTestPage.class);

        assertThat(breadcrumbTitles(), contains("PanelTestPage"));
        tester.assertDisabled("crumbs:crumbs:0:link:title");

        // completely new breadcrumb trail created for another page
        PanelTestPage lastPage = tester.startPage(new PanelTestPage());

        assertThat(breadcrumbTitles(), contains("PanelTestPage"));
        tester.assertDisabled("crumbs:crumbs:0:link:title");

        PageParameters params = BreadcrumbPageParameters.setTrailTo(new PageParameters(), lastPage);
        tester.startPage(new PanelTestPage(params));

        assertThat(breadcrumbTitles(), contains("PanelTestPage", "PanelTestPage"));
        tester.assertEnabled("crumbs:crumbs:0:link:title");
        tester.assertDisabled("crumbs:crumbs:1:link:title");
    }

    private static class PanelTestPage extends AbstractBreadcrumbTestsPage {
        public PanelTestPage() {
            super();
        }

        public PanelTestPage(PageParameters parameters) {
            super(parameters);
        }
    }
}
