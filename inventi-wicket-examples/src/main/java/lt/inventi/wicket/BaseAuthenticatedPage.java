package lt.inventi.wicket;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbsOperationsHelper;
import lt.inventi.wicket.component.breadcrumb.BreadcrumbsPanel;
import lt.inventi.wicket.component.breadcrumb.IBreadcrumbsOperations;
import lt.inventi.wicket.component.breadcrumb.IBreadcrumbTitleProvider;

public class BaseAuthenticatedPage extends WebPage implements IBreadcrumbsOperations, IBreadcrumbTitleProvider {

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
    public IModel<String> getBreadcrumbTitle() {
        return Model.of(getClass().getSimpleName());
    }

}
