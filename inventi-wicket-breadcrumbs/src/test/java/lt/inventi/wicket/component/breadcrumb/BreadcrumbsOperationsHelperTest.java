package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;
import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsOperationsHelper;
import lt.inventi.wicket.component.breadcrumb.h.IBreadcrumbsOperations;

public class BreadcrumbsOperationsHelperTest extends BreadcrumbsTests {

    @Test
    public void goesToNextAndPreviousPages() {
        tester.startPage(OperationsTestPage.class, new PageParameters().add("count", 10));

        assertThat(breadcrumbTitles(), contains("10"));

        tester.clickLink("statelessLinkClassOnly");

        assertThat(breadcrumbTitles(), contains("10", "0"));

        tester.clickLink("statefulLink");

        assertThat(breadcrumbTitles(), contains("10", "0", "1"));

        tester.clickLink("statelessLinkClassWithParams");

        assertThat(breadcrumbTitles(), contains("10", "0", "1", "1"));

        tester.clickLink("previousPageLink");

        // TODO: Why?
        /*
        assertThat(breadcrumbTitles(), contains("10", "0", "1"));

        tester.clickLink("previousPageLink");

        assertThat(breadcrumbTitles(), contains("10", "0"));
        */
    }

    public static class OperationsTestPage extends AbstractBreadcrumbTestsPage implements IBreadcrumbsOperations {
        private final BreadcrumbsOperationsHelper helper;

        private int count;

        public OperationsTestPage() {
            super();
            this.helper = new BreadcrumbsOperationsHelper(this);
        }

        public OperationsTestPage(PageParameters parameters) {
            super(parameters);
            StringValue countParam = parameters.get("count");
            this.count = countParam.isEmpty() ? 0 : countParam.toInt();
            this.helper = new BreadcrumbsOperationsHelper(this);
        }

        public OperationsTestPage(int i) {
            super();
            this.helper = new BreadcrumbsOperationsHelper(this);
            count = i;
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();

            add(new Link<Void>("statelessLinkClassOnly") {
                @Override
                public void onClick() {
                    setNextResponsePage(OperationsTestPage.class);
                }
            });
            add(new Link<Void>("statelessLinkClassWithParams") {
                @Override
                public void onClick() {
                    setNextResponsePage(OperationsTestPage.class, getPage().getPageParameters().set("count", count));
                }
            });
            add(new Link<Void>("statefulLink") {
                @Override
                public void onClick() {
                    setNextResponsePage(new OperationsTestPage(count + 1));
                }
            });
            add(new Link<Void>("previousPageLink") {
                @Override
                public void onClick() {
                    setResponseToPreviousPage();
                }
            });
        }

        @Override
        public IMarkupFragment getMarkup() {
            return Markup.of("<body><div wicket:id=\"crumbs\" />"
                + "<a wicket:id=\"statelessLinkClassWithParams\" />"
                + "<a wicket:id=\"statelessLinkClassOnly\" />"
                + "<a wicket:id=\"statefulLink\" />"
                + "<a wicket:id=\"previousPageLink\" />"
                + "</body>");
        }

        @Override
        public IModel<String> getTitle() {
            return Model.of(String.valueOf(count));
        }

        @Override
        public void setResponseToPreviousPage() {
            helper.setResponseToPreviousPage();
        }

        @Override
        public void setNextResponsePage(IRequestablePage page) {
            helper.setNextResponsePage(page);
        }

        @Override
        public void setNextResponsePage(Class<? extends IRequestablePage> clazz) {
            helper.setNextResponsePage(clazz);
        }

        @Override
        public void setNextResponsePage(Class<? extends IRequestablePage> clazz, PageParameters params) {
            helper.setNextResponsePage(clazz, params);
        }
    }
}
