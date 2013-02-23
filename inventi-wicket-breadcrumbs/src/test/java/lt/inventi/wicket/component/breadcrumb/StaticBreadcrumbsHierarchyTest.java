package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.markup.html.WebPage;
import org.junit.Test;

public class StaticBreadcrumbsHierarchyTest extends BreadcrumbsTests {

    static class BreadcrumbsHierarchyBuilder {
        static class TreeBuilder {
            public static TreeBuilder of(Class<? extends WebPage> root) {
                return new TreeBuilder();
            }

            public TreeBuilder then(TreeBuilder child) {
                // TODO Auto-generated method stub
                return null;
            }
        }

        public static BreadcrumbsHierarchyBuilder hierarchy(TreeBuilder tree) {
            return new BreadcrumbsHierarchyBuilder().andHierarchy(tree);
        }

        public BreadcrumbsHierarchyBuilder andHierarchy(TreeBuilder tree) {
            return this;
        }

        public BreadcrumbsHierarchy build() {
            return new BreadcrumbsHierarchy(this);
        }
    }

    static class BreadcrumbsHierarchy {
        public BreadcrumbsHierarchy(BreadcrumbsHierarchyBuilder builder) {
            // TODO Auto-generated constructor stub
        }
    }

    @Test
    public void rebuildsBreadcrumbsFromTheHierarchy() {
        tester.startPage(ThirdLevelPage.class);

        tester.assertLabel("crumbs:crumbs:0:link:title", "ThirdLevelPage");
        tester.assertDisabled("crumbs:crumbs:0:link:title");
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
