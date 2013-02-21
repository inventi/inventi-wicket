package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public class StatefulBreadcrumbLink extends Link<Breadcrumb> {

    public StatefulBreadcrumbLink(String id, IModel<Breadcrumb> model) {
        super(id, model);
    }

    @Override
    public void onClick() {
        getRequestCycle().scheduleRequestHandlerAfterCurrent(getModelObject().getTarget());
    }

}
