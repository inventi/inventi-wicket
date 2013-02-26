package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.RequestCycle;

public interface IBreadcrumbTargetProvider {
    IRequestHandler getHandler();

    CharSequence getURL(RequestCycle rc);
}
