package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.Component;
import org.apache.wicket.model.IModel;

interface IComponentBreadcrumbTitleProvider {
    IModel<String> getBreadcrumbTitle(Component c);
}