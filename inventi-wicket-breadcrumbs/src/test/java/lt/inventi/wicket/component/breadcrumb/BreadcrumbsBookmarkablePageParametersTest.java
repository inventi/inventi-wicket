package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.junit.Test;

public class BreadcrumbsBookmarkablePageParametersTest extends BreadcrumbsTests {

    @Test
    public void copiesPageParametersOfABookmarkableLink() {
        APage page = tester.startPage(APage.class);
        page = tester.startPage(APage.class, BreadcrumbPageParameters.setTrailTo(new PageParameters(), page));

        @SuppressWarnings("unchecked")
        BookmarkablePageLink<APage> link = (BookmarkablePageLink<APage>) tester.getComponentFromLastRenderedPage("link");
        assertThat(link.getPageParameters(), is(not(page.getPageParameters())));
    }

    public static class APage extends AbstractBreadcrumbTestsPage {
        public APage(PageParameters parameters) {
            super(parameters);
        }

        @Override
        protected void onInitialize() {
            super.onInitialize();
            add(new BookmarkablePageLink<APage>("link", APage.class, getPage().getPageParameters()));
        }

        @Override
        public IMarkupFragment getMarkup() {
            return Markup.of("<body>"
                + "<div wicket:id=\"crumbs\"></div>"
                + "<a wicket:id=\"link\"></a>"
                + "</body>");
        }
    }
}
