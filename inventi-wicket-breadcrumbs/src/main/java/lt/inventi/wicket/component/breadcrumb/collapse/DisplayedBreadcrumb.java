package lt.inventi.wicket.component.breadcrumb.collapse;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;

import lt.inventi.wicket.component.breadcrumb.IBreadcrumbTargetProvider;

public interface DisplayedBreadcrumb extends Serializable {
    IModel<String> title();

    boolean shouldEscapeTitle();

    IBreadcrumbTargetProvider targetProvider();

    boolean isCollapsed();

    List<DisplayedBreadcrumb> collapsedCrumbs();
}