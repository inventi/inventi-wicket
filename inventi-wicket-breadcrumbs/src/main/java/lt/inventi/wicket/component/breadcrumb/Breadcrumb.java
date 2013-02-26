package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;

import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;


public class Breadcrumb implements IBreadcrumbTargetProvider, Serializable {

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
    private BreadcrumbTitle title;
    private final BreadcrumbPageProvider pageAndUrlProvider;

    Breadcrumb(IRequestablePage page, BreadcrumbTitle title) {
        this.id = constructIdFrom(page);
        this.pageAndUrlProvider = new BreadcrumbPageProvider(page);
        this.title = title;
    }

    public BreadcrumbTitle title() {
        return title;
    }

    public Class<?> getType() {
        return pageAndUrlProvider.getPageType();
    }

    @Override
    public IRequestHandler getHandler() {
        return pageAndUrlProvider.getHandler();
    }

    @Override
    public CharSequence getURL(RequestCycle rc) {
        return pageAndUrlProvider.getURL(rc);
    }

    void updateWith(Breadcrumb newCrumb) {
        this.id = newCrumb.id;
        this.title = newCrumb.title;
    }

    String getId() {
        return id;
    }

    String getStableId() {
        return pageAndUrlProvider.getId();
    }

    @Override
    public String toString() {
        return "Crumb<" + id + ", " + pageAndUrlProvider + ">";
    }

}
