package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.request.cycle.RequestCycle;

public interface IBreadcrumbUrlProvider {

    CharSequence getURL(RequestCycle cycle);

}
