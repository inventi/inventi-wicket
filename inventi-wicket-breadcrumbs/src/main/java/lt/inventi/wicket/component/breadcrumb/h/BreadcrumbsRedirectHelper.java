package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class BreadcrumbsRedirectHelper {

    private BreadcrumbsRedirectHelper() {
        // static utils
    }

    public static void setResponseToPreviousPage(Component component) {
        Page page = component.getPage();
        String optionalTrailId = BreadcrumbPageParameters.getOptionalTrailId(page.getPageParameters());
        Breadcrumb previousBreadcrumb = BreadcrumbTrailHistory.getLastBreadcrumbFor(optionalTrailId);
        if (previousBreadcrumb == null) {
            component.getRequestCycle().setResponsePage(page);
        } else {
            component.getRequestCycle().scheduleRequestHandlerAfterCurrent(previousBreadcrumb.getTarget());
        }
    }

    public static void setNextResponsePage(Component component, IRequestablePage nextPage) {
        Page page = component.getPage();
        BreadcrumbPageParameters.setTrailTo(nextPage.getPageParameters(), page);
        component.getRequestCycle().setResponsePage(nextPage);
    }

    public static void setNextResponsePage(Component component, Class<? extends IRequestablePage> nextPageType) {
        setNextResponsePage(component, nextPageType, new PageParameters());
    }

    public static void setNextResponsePage(Component component, Class<? extends IRequestablePage> nextPageType, PageParameters params) {
        Page page = component.getPage();
        BreadcrumbPageParameters.setTrailTo(params, page);
        component.getRequestCycle().setResponsePage(nextPageType, params);
    }
}
