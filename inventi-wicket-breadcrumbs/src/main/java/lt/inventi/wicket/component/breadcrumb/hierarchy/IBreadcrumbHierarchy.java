package lt.inventi.wicket.component.breadcrumb.hierarchy;

import java.util.List;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.collapse.DisplayedBreadcrumb;

/**
 * TODO: Needs rework, as it's not possible to reuse breadcrumb titles for the
 * restored hierarchy (as missing hierarchy is completely stateless).
 */
public interface IBreadcrumbHierarchy {

    // TODO: add current page parameters
    List<DisplayedBreadcrumb> restoreMissingHierarchy(List<Breadcrumb> originalCrumbs);

}
