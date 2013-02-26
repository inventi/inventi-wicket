package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbTitle;
import lt.inventi.wicket.component.breadcrumb.IBreadcrumbTargetProvider;

public class SingleDisplayedBreadcrumb implements DisplayedBreadcrumb {

    private final BreadcrumbTitle title;
    private final IBreadcrumbTargetProvider target;

    public SingleDisplayedBreadcrumb(BreadcrumbTitle title, IBreadcrumbTargetProvider target) {
        this.title = title;
        this.target = target;
    }

    @Override
    public IModel<String> title() {
        return title.getTitle();
    }

    @Override
    public boolean shouldEscapeTitle() {
        return title.shouldEscapeTitle();
    }

    @Override
    public IBreadcrumbTargetProvider targetProvider() {
        return target;
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