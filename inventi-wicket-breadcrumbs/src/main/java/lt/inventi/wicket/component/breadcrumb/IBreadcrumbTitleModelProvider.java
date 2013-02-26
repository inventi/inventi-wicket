package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.model.IModel;

public interface IBreadcrumbTitleModelProvider {

    IModel<String> getBreadcrumbTitleModel();

}