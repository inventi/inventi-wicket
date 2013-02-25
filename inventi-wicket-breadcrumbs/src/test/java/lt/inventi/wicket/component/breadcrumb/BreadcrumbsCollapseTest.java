package lt.inventi.wicket.component.breadcrumb;

import org.junit.Test;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbsSettings;

public class BreadcrumbsCollapseTest extends BreadcrumbsTests {

    @Override
    protected BreadcrumbsSettings createSettings() {
        return super.createSettings().collapseWhenRepeated(2);
    }

    @Test
    public void collapsesBreadcrumbsAccordingToTheSpecifiedLevel() {

    }
}
