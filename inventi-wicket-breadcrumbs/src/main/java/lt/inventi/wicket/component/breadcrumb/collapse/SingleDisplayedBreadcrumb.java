package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;

class SingleDisplayedBreadcrumb implements DisplayedBreadcrumb {
    private final Breadcrumb crumb;

    public SingleDisplayedBreadcrumb(Breadcrumb crumb) {
        this.crumb = crumb;
    }

    @Override
    public IModel<String> title() {
        return crumb.getTitleModel();
    }

    @Override
    public boolean isCollapsed() {
        return false;
    }

    @Override
    public List<DisplayedBreadcrumb> collapsedCrumbs() {
        return Collections.emptyList();
    }
}