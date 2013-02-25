package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.model.Model;


public class PreviousPageLink extends BreadcrumbLink {

    public PreviousPageLink(String id) {
        super(id, null);
    }

    @Override
    protected void onBeforeRender() {
        String trailId = Breadcrumb.constructIdFrom(getPage());
        Breadcrumb lastCrumb = BreadcrumbTrailHistory.getPenultimateBreadcrumbFor(trailId);
        if (lastCrumb == null) {
            setEnabled(false);
        } else {
            setDefaultModel(Model.of(lastCrumb));
        }
        super.onBeforeRender();
    }
}
