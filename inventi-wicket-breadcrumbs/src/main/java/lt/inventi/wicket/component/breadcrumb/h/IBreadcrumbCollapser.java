package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;

public interface IBreadcrumbCollapser {

    public interface DisplayedBreadcrumb extends Serializable {
        IModel<String> title();

        boolean isCollapsed();

        List<DisplayedBreadcrumb> collapsedCrumbs();
    }

    List<DisplayedBreadcrumb> collapse(List<Breadcrumb> crumbs);

}
