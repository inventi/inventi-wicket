package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsSettings;
import lt.inventi.wicket.component.breadcrumb.h.IProvideTitle;
import lt.inventi.wicket.component.breadcrumb.h.NextPageLink;

public class AutomaticNextBreadcrumbPageLinkTest {

    private WicketTester tester = new WicketTester();

    @Before
    public void setUp() {
        new BreadcrumbsSettings().install(tester.getApplication());
    }

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

    @SuppressWarnings("unchecked")
    private Iterable<? extends String> breadcrumbTitles() {
        Component c = tester.getComponentFromLastRenderedPage("crumbs:crumbs");
        ListView<Breadcrumb> view = ((ListView<Breadcrumb>) c);
        List<String> titles = new ArrayList<String>();
        for (Breadcrumb b : view.getList()) {
            titles.add(b.getTitleModel().getObject());
        }
        return titles;
    }

    public static class APage extends WebPage implements IProvideTitle {
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
            add(new Label("label", new AbstractReadOnlyModel<String>() {
                @Override
                public String getObject() {
                    return String.valueOf(count);
                }
            }));
            add(new BreadcrumbsPanel("crumbs"));
        }

        @Override
        public IMarkupFragment getMarkup() {
            return Markup.of("<body><span wicket:id=\"label\" />"
                + "<div wicket:id=\"crumbs\"></div>"
                + "<a wicket:id=\"link\"></a>"
                + "<a wicket:id=\"statefulLink\"></a></body>");
        }

        @Override
        public IModel<String> getTitle() {
            return Model.of(getClass().getSimpleName());
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
            add(new NextPageLink<Void>("statefulLink") {
                @Override
                public void onClick() {
                    setNextResponsePage(new SecondPage(count + 1));
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
            add(new NextPageLink<Void>("statefulLink") {
                @Override
                public void onClick() {
                    setNextResponsePage(new FirstPage(count + 1));
                }
            });
        }
    }
}
