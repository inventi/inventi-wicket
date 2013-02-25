package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public interface IBreadcrumbsOperations {

    void setResponseToPreviousPage();

    void setNextResponsePage(IRequestablePage page);

    void setNextResponsePage(Class<? extends IRequestablePage> clazz);

    void setNextResponsePage(Class<? extends IRequestablePage> clazz, PageParameters params);
}
