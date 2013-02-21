package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.cycle.RequestCycle;

public class Breadcrumb implements Serializable, IBreadcrumbUrlProvider {

    static String constructIdFrom(IRequestablePage page) {
        return page.getPageId() + "-" + page.getRenderCount() + "-" + page.getClass().getSimpleName();
    }

    private final String id;
    private final BreadcrumbPageProvider pageAndUrlProvider;
    private final IModel<String> titleModel;

    public Breadcrumb(IRequestablePage page, IModel<String> title) {
        this.id = constructIdFrom(page);
        this.pageAndUrlProvider = new BreadcrumbPageProvider(page);
        this.titleModel = title;
    }

    public String getId() {
        return id;
    }

    public IModel<String> getTitleModel() {
        return titleModel;
    }

    public IRequestHandler getTarget() {
        return pageAndUrlProvider.getHandler();
    }

    @Override
    public CharSequence getURL(RequestCycle rc) {
        return pageAndUrlProvider.getURL(rc);
    }

    @Override
    public String toString() {
        return "Crumb<" + id + ">";
    }
}
