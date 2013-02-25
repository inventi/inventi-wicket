package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.ArrayList;
import java.util.List;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;

public class NoopCollapser implements IBreadcrumbCollapser {
    @Override
    public List<DisplayedBreadcrumb> collapse(List<Breadcrumb> crumbs) {
        List<DisplayedBreadcrumb> result = new ArrayList<DisplayedBreadcrumb>();
        for (Breadcrumb b : crumbs) {
            result.add(new SingleDisplayedBreadcrumb(b));
        }
        return result;
    }
}