package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;


public class Breadcrumb implements Serializable {

    /**
     * We cannot use page ids because they might change after page is loaded,
     * e.g. when the model is updated before navigating to the next page.
     *
     * @param page
     * @return
     */
    static String constructIdFrom(IRequestablePage page) {
        return String.valueOf(page.hashCode());
    }

    private String id;
    private IModel<String> titleModel;
    private final BreadcrumbPageProvider pageAndUrlProvider;

    Breadcrumb(IRequestablePage page, IModel<String> title) {
        this.id = constructIdFrom(page);
        this.pageAndUrlProvider = new BreadcrumbPageProvider(page);
        this.titleModel = title;
    }

    public IModel<String> getTitleModel() {
        return titleModel;
    }

    String getId() {
        return id;
    }

    IRequestHandler getTarget() {
        return pageAndUrlProvider.getHandler();
    }

    CharSequence getURL(RequestCycle rc) {
        return pageAndUrlProvider.getURL(rc);
    }

    void updateWith(Breadcrumb newCrumb) {
        this.id = newCrumb.id;
        this.titleModel = newCrumb.titleModel;
    }

    String getStableId() {
        return pageAndUrlProvider.getId();
    }

    Class<?> getType() {
        return pageAndUrlProvider.getPageType();
    }

    @Override
    public String toString() {
        return "Crumb<" + id + ", " + pageAndUrlProvider + ">";
    }

}
