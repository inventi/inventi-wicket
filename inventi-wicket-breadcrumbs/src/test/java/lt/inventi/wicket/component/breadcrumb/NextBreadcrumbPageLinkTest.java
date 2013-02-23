package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsOperationsHelper;

public class NextBreadcrumbPageLinkTest extends BreadcrumbsTests {

    @Test
    public void navigatesBetweenPagesRetainingBreadcrumbsAutomatically() {
        tester.startPage(new FirstPage());
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage"));

        tester.clickLink("link");
        tester.assertRenderedPage(SecondPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage"));

        tester.clickLink("link");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage", "FirstPage"));

        tester.clickLink("crumbs:crumbs:1:link");
        tester.assertRenderedPage(SecondPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage"));

        tester.clickLink("link");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage", "FirstPage"));

        tester.clickLink("crumbs:crumbs:0:link");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage"));
    }

    @Test
    public void navigatesBetweenStatefulPages() {
        tester.startPage(new FirstPage(1));
        tester.assertLabel("label", "1");
        assertThat(breadcrumbTitles(), contains("FirstPage"));

        tester.clickLink("statefulLink");
        tester.assertRenderedPage(SecondPage.class);
        tester.assertLabel("label", "2");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage"));

        tester.clickLink("statefulLink");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "3");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage", "FirstPage"));

        tester.clickLink("crumbs:crumbs:1:link");
        tester.assertRenderedPage(SecondPage.class);
        tester.assertLabel("label", "2");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage"));

        tester.clickLink("link");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "0");
        assertThat(breadcrumbTitles(), contains("FirstPage", "SecondPage", "FirstPage"));

        tester.clickLink("crumbs:crumbs:0:link");
        tester.assertRenderedPage(FirstPage.class);
        tester.assertLabel("label", "1");
        assertThat(breadcrumbTitles(), contains("FirstPage"));
    }

    public static class APage extends AbstractBreadcrumbTestsPage {
        protected BreadcrumbsOperationsHelper helper;
        public APage() {
            super();
        }

        public APage(PageParameters parameters) {
            super(parameters);
        }

        protected int count;
        public APage(int i) {
            count = i;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            helper = new BreadcrumbsOperationsHelper(this);
            add(new Label("label", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return String.valueOf(count);
                }
            }));
        }

        @Override
        public IMarkupFragment getMarkup() {
            return Markup.of("<body><span wicket:id=\"label\" />"
                + "<div wicket:id=\"crumbs\"></div>"
                + "<a wicket:id=\"link\"></a>"
                + "<a wicket:id=\"statefulLink\"></a></body>");
        }
    }

    public static class FirstPage extends APage {
        public FirstPage() {
            super();
        }

        public FirstPage(PageParameters parameters) {
            super(parameters);
        }

        public FirstPage(int i) {
            super(i);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(new BookmarkablePageLink<SecondPage>("link", SecondPage.class));
            add(new Link<Void>("statefulLink") {
                @Override
                public void onClick() {
                    helper.setNextResponsePage(new SecondPage(count + 1));
                }
            });
        }
    }

    public static class SecondPage extends APage {
        public SecondPage() {
            super();
        }

        public SecondPage(PageParameters parameters) {
            super(parameters);
        }

        public SecondPage(int i) {
            super(i);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(new BookmarkablePageLink<FirstPage>("link", FirstPage.class));
            add(new Link<Void>("statefulLink") {
                @Override
                public void onClick() {
                    helper.setNextResponsePage(new FirstPage(count + 1));
                }
            });
        }
    }
}
