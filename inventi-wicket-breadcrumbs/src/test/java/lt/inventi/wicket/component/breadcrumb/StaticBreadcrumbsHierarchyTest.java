package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.Page;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsSettings;
import lt.inventi.wicket.component.breadcrumb.h.IProvideTitle;

public class StaticBreadcrumbsHierarchyTest {

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

    static class HierarchyApplication extends WebApplication {
        @Override
        protected void init() {
            super.init();

            /*BreadcrumbsHierarchy hierarchy = BreadcrumbsHierarchyBuilder
                .hierarchy(of(HierarchyRootPage.class).then(of(SecondLevelPage.class)).then(of(ThirdLevelPage.class)))
                .andHierarchy(of(AnotherRootPage.class)).build();*/

            new BreadcrumbsSettings().install(this);
        }

        @Override
        public Class<? extends Page> getHomePage() {
            return HierarchyRootPage.class;
        }
    }

    private WicketTester tester = new WicketTester(new HierarchyApplication());

    @Test
    public void rebuildsBreadcrumbsFromTheHierarchy() {
        tester.startPage(ThirdLevelPage.class);

        tester.assertLabel("crumbs:crumbs:0:link:title", "ThirdLevelPage");
        tester.assertDisabled("crumbs:crumbs:0:link:title");
    }

    static class BaseTestPage extends WebPage implements IProvideTitle {
        @Override
        public IMarkupFragment getMarkup() {
            return Markup
                .of("<html><body><div wicket:id=\"crumbs\"></div></body></html>");
        }

        @Override
        public IModel<String> getTitle() {
            return Model.of(getClass().getSimpleName());
        }
    }

    static class HierarchyRootPage extends BaseTestPage {
        public HierarchyRootPage() {
            add(new BreadcrumbsPanel("crumbs"));
        }
    }

    static class SecondLevelPage extends BaseTestPage {
        public SecondLevelPage() {
            add(new BreadcrumbsPanel("crumbs"));
        }
    }

    public static class ThirdLevelPage extends BaseTestPage {
        public ThirdLevelPage() {
            add(new BreadcrumbsPanel("crumbs"));
        }
    }

    static class AnotherRootPage extends BaseTestPage {
        public AnotherRootPage() {
            add(new BreadcrumbsPanel("crumbs"));
        }
    }
}
