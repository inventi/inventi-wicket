package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.List;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;

public interface IBreadcrumbCollapser {

    List<DisplayedBreadcrumb> collapse(List<Breadcrumb> crumbs);

}
