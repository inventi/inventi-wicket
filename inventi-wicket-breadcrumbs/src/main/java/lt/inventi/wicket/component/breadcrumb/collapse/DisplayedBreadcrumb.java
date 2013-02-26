package lt.inventi.wicket.component.breadcrumb.collapse;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;

public interface DisplayedBreadcrumb extends Serializable {
    IModel<String> title();

    boolean shouldEscapeTitle();

    boolean isCollapsed();

    List<DisplayedBreadcrumb> collapsedCrumbs();
}