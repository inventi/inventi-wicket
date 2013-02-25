package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;


public class BreadcrumbsOperationsHelper implements IBreadcrumbsOperations, Serializable {

    private final Component context;

    public BreadcrumbsOperationsHelper(Component context) {
        this.context = context;
    }

    @Override
    public void setResponseToPreviousPage() {
        BreadcrumbsRedirectHelper.setResponseToPreviousPage(context);
    }

    @Override
    public void setNextResponsePage(IRequestablePage page) {
        BreadcrumbsRedirectHelper.setNextResponsePage(context, page);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz) {
        BreadcrumbsRedirectHelper.setNextResponsePage(context, clazz);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz, PageParameters params) {
        BreadcrumbsRedirectHelper.setNextResponsePage(context, clazz, params);
    }

}
