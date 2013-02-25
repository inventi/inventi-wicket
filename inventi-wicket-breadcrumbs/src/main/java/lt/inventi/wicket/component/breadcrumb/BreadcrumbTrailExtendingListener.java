package lt.inventi.wicket.component.breadcrumb;

import static lt.inventi.wicket.component.breadcrumb.BreadcrumbPageParameters.getOptionalTrailId;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.application.IComponentOnBeforeRenderListener;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;


public class BreadcrumbTrailExtendingListener implements IComponentOnBeforeRenderListener {
    /**
     * We cannot use {@link IComponentInitializationListener} or
     * {@link IComponentInstantiationListener} because navigation to the
     * breadcrumbs in the trail won't always create/initialize pages (in case
     * they're stateful and still in page store). We construct unique trail ids
     * based on the page's hashCode and a deserialized page's hashCode doesn't
     * have to match the hashCode of the serialized one, thus we cannot reliably
     * find the previous page trail without extending it on each render.
     * <p>
     * Possible improvement: store the newly created breadcrumb in the session's
     * metadata and use it in all further next page links. This way we can use
     * stable breadcrumb ids (page id + page class) and avoid creating new
     * breadcrumbs when the same page is rerendered (same page id + class but
     * different page object and thus hashCode).
     */

    private final IBreadcrumbPageFilter pageFilter;

    public BreadcrumbTrailExtendingListener(IBreadcrumbPageFilter pageFilter) {
        this.pageFilter = pageFilter;
    }

    @Override
    public void onBeforeRender(Component component) {
        if (component instanceof Page && pageFilter.shouldCreateBreadcrumbFor((IRequestablePage) component)) {
            Page p = (Page) component;
            Breadcrumb crumb = createBreadcrumbFrom(p);
            String maybeTrailId = getOptionalTrailId(p.getPageParameters());
            BreadcrumbTrailHistory.extendTrail(maybeTrailId, crumb);
            System.out.println("Session: " + Session.get().getSizeInBytes() + " b.");
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
