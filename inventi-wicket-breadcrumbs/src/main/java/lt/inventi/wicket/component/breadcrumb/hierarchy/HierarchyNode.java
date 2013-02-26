package lt.inventi.wicket.component.breadcrumb.hierarchy;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbTitle;

interface HierarchyNode {

    Class<? extends IRequestablePage> getPageClass();

    PageParameters getPageParameters();

    BreadcrumbTitle getTitle();

}
