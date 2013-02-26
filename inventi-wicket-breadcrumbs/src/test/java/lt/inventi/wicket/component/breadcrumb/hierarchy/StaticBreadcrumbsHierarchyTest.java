package lt.inventi.wicket.component.breadcrumb.hierarchy;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.apache.wicket.model.Model;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbTitle;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsTests;

public class StaticBreadcrumbsHierarchyTest extends BreadcrumbsTests {

    @Override
    protected BreadcrumbsSettings createSettings() {
        return super.createSettings().withStaticHierarchy(
            StaticBreadcrumbHierarchies.homePageHierarchy(new BreadcrumbTitle(Model.of("Home"))));
    }

    @Test
    public void rebuildsBreadcrumbsFromTheHierarchy() {
        tester.startPage(ThirdLevelPage.class);

        assertThat(breadcrumbTitles(), contains("Home", "ThirdLevelPage"));
    }

    public static class BaseTestPage extends AbstractBreadcrumbTestsPage {
        //
    }

    public static class HierarchyRootPage extends BaseTestPage {
        //
    }

    public static class SecondLevelPage extends BaseTestPage {
        //
    }

    public static class ThirdLevelPage extends BaseTestPage {
        //
    }

    public static class AnotherRootPage extends BaseTestPage {
        //
    }
}
