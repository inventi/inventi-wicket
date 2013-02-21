package lt.inventi.wicket.component.breadcrumb.h;

import static lt.inventi.wicket.component.breadcrumb.h.BreadcrumbPageParameters.getOptionalTrailId;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;

public class BreadcrumbTrailOnBeforeRenderListener implements IComponentOnBeforeRenderListener {

    private final IBreadcrumbPageFilter pageFilter;

    public BreadcrumbTrailOnBeforeRenderListener(IBreadcrumbPageFilter pageFilter) {
        this.pageFilter = pageFilter;
    }

    @Override
    public void onBeforeRender(Component component) {
        if (component instanceof Page && pageFilter.shouldCreateBreadcrumbFor((IRequestablePage) component)) {
            Page p = (Page) component;
            Breadcrumb crumb = createBreadcrumbFrom(p);
            String maybeTrailId = getOptionalTrailId(p.getPageParameters());
            BreadcrumbTrailHistory.extendTrail(maybeTrailId, crumb);
        }
    }

    private static Breadcrumb createBreadcrumbFrom(Page p) {
        final IModel<String> titleModel;
        if (p instanceof IProvideTitle) {
            titleModel = ((IProvideTitle) p).getTitle();
        } else {
            titleModel = Model.of(((Component) p).getString("breadcrumb"));
        }
        return new Breadcrumb(p, titleModel);
    }

}
