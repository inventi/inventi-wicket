package lt.inventi.wicket.component.breadcrumb.h;

import org.apache.wicket.model.IDetachable;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;

public class Breadcrumb implements IDetachable {

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

    private final String id;
    private final BreadcrumbPageProvider pageAndUrlProvider;
    private final IModel<String> titleModel;

    Breadcrumb(IRequestablePage page, IModel<String> title) {
        this.id = constructIdFrom(page);
        this.pageAndUrlProvider = new BreadcrumbPageProvider(page);
        this.titleModel = title;
    }

    public IModel<String> getTitleModel() {
        return titleModel;
    }

    @Override
    public void detach() {
        //pageAndUrlProvider.detach();
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

    @Override
    public String toString() {
        return "Crumb<" + id + ", " + pageAndUrlProvider + ">";
    }

}
