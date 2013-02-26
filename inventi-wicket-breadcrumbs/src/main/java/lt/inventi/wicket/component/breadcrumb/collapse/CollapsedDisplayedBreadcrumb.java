package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.IBreadcrumbTargetProvider;

class CollapsedDisplayedBreadcrumb implements DisplayedBreadcrumb {
    private final List<DisplayedBreadcrumb> collapsed;

    public CollapsedDisplayedBreadcrumb(Iterable<Breadcrumb> crumbs) {
        if (crumbs == null || !crumbs.iterator().hasNext()) {
            throw new IllegalArgumentException("Must contain at least one collapsed breadcrumb!");
        }

        List<DisplayedBreadcrumb> result = new ArrayList<DisplayedBreadcrumb>();
        for (Breadcrumb b : crumbs) {
            result.add(new SingleDisplayedBreadcrumb(b.title(), b));
        }
        collapsed = Collections.unmodifiableList(result);
    }

    @Override
    public IModel<String> title() {
        return Model.of("...");
    }

    @Override
    public IBreadcrumbTargetProvider targetProvider() {
        return null;
    }

    @Override
    public boolean shouldEscapeTitle() {
        return true;
    }

    @Override
    public boolean isCollapsed() {
        return true;
    }

    @Override
    public List<DisplayedBreadcrumb> collapsedCrumbs() {
        return collapsed;
    }

}