package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public abstract class BreadcrumbPageParameters {

    private static final String TRAIL_ID_PARAM = "trail";

    private BreadcrumbPageParameters() {
        // static utils
    }

    public static String getOptionalTrailId(PageParameters params) {
        return params.get(TRAIL_ID_PARAM).toOptionalString();
    }

    public static PageParameters setTrailTo(PageParameters params, IRequestablePage page) {
        params.set(TRAIL_ID_PARAM, Breadcrumb.constructIdFrom(page));
        return params;
    }

}
