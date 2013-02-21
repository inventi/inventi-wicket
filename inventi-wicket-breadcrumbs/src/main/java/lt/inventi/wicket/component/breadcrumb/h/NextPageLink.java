package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;

public abstract class NextPageLink<T> extends Link<T> {

    protected NextPageLink(String id, IModel<T> model) {
        super(id, model);
    }

    protected NextPageLink(String id) {
        super(id);
    }

    public final void setNextResponsePage(Page page) {
        BreadcrumbPageParameters.setTrailTo(page.getPageParameters(), getPage());
        setResponsePage(page);
    }

}
