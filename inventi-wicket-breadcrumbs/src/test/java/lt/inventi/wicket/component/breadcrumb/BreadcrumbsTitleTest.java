package lt.inventi.wicket.component.breadcrumb;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;

public class BreadcrumbsTitleTest extends BreadcrumbsTests {
    private static final Model<String> HTML_MARKUP_MODEL = Model.of("<span></span>");

    @Test
    public void escapesMarkupByDefault() {
        tester.startPage(new DefaultModelPage());

        assertThat(breadcrumbTitles(), contains("&lt;span&gt;&lt;/span&gt;"));
    }

    @Test
    public void escapesMarkupWhenTitleSaysSo() {
        tester.startPage(new EscapedModelPage());

        assertThat(breadcrumbTitles(), contains("&lt;span&gt;&lt;/span&gt;"));
    }

    @Test
    public void doesNotEscapeMarkupWhenTitleSaysSo() {
        tester.startPage(new UnescapedModelPage());

        assertThat(breadcrumbTitles(), contains("<span></span>"));
    }

    public static class DefaultModelPage extends AbstractBreadcrumbTestsPage {
        @Override
        public IModel<String> getBreadcrumbTitleModel() {
            return HTML_MARKUP_MODEL;
        }
    }

    public static class EscapedModelPage extends AbstractBreadcrumbTestsPage implements IBreadcrumbTitleProvider {
        @Override
        public BreadcrumbTitle getBreadcrumbTitle() {
            return new BreadcrumbTitle(HTML_MARKUP_MODEL, true);
        }
    }

    public static class UnescapedModelPage extends AbstractBreadcrumbTestsPage implements IBreadcrumbTitleProvider {
        @Override
        public BreadcrumbTitle getBreadcrumbTitle() {
            return new BreadcrumbTitle(HTML_MARKUP_MODEL, false);
        }
    }
}
