package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.request.component.IRequestablePage;

public interface IBreadcrumbPageFilter {

    boolean shouldCreateBreadcrumbFor(IRequestablePage page);

}
