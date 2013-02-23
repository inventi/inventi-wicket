package lt.inventi.wicket.component.breadcrumb;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;

import lt.inventi.wicket.component.breadcrumb.h.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsSettings;
import lt.inventi.wicket.component.breadcrumb.h.IProvideTitle;

public class BreadcrumbsTests {

    protected WicketTester tester = new WicketTester();

    @Before
    public void setUp() {
        createSettings().install(tester.getApplication());
    }

    protected BreadcrumbsSettings createSettings() {
        return new BreadcrumbsSettings()
            .withStatefulBreadcrumbLinks()
            .withDecoratedBookmarkableLinks();
    }

    @SuppressWarnings("unchecked")
    protected Iterable<? extends String> breadcrumbTitles() {
        Component c = tester.getComponentFromLastRenderedPage("crumbs:crumbs");
        ListView<Breadcrumb> view = ((ListView<Breadcrumb>) c);
        List<String> titles = new ArrayList<String>();
        for (Breadcrumb b : view.getList()) {
            titles.add(b.getTitleModel().getObject());
        }
        return titles;
    }

    protected abstract static class AbstractBreadcrumbTestsPage extends WebPage implements IProvideTitle {
        protected AbstractBreadcrumbTestsPage() {
            super();
        }

        protected AbstractBreadcrumbTestsPage(PageParameters parameters) {
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
