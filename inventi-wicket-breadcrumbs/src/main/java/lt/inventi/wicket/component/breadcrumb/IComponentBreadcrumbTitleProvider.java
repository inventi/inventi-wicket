package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.Component;

interface IComponentBreadcrumbTitleProvider {
    BreadcrumbTitle getBreadcrumbTitle(Component c);
}