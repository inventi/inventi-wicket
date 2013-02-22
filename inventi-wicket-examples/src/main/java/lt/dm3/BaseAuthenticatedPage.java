package lt.dm3;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsOperationsHelper;
import lt.inventi.wicket.component.breadcrumb.h.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.h.IBreadcrumbsOperations;
import lt.inventi.wicket.component.breadcrumb.h.IProvideTitle;

public class BaseAuthenticatedPage extends WebPage implements IBreadcrumbsOperations, IProvideTitle {

    private BreadcrumbsOperationsHelper helper;

    public BaseAuthenticatedPage() {
        super();
    }

    public BaseAuthenticatedPage(IModel<?> model) {
        super(model);
    }

    public BaseAuthenticatedPage(PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        helper = new BreadcrumbsOperationsHelper(this);
        add(new BreadcrumbsPanel("crumbs"));
    }

    @Override
    public void setResponseToPreviousPage() {
        helper.setResponseToPreviousPage();
    }

    @Override
    public void setNextResponsePage(IRequestablePage page) {
        helper.setNextResponsePage(page);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz) {
        helper.setNextResponsePage(clazz);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz, PageParameters params) {
        helper.setNextResponsePage(clazz, params);
    }

    @Override
    public IModel<String> getTitle() {
        return Model.of(getClass().getSimpleName());
    }

}
