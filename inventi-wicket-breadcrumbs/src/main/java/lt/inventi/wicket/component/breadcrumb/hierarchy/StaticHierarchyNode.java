package lt.inventi.wicket.component.breadcrumb.hierarchy;

import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbTitle;

class StaticHierarchyNode implements HierarchyNode {
    private final Class<? extends IRequestablePage> pageClass;
    private final PageParameters parameters;
    private final BreadcrumbTitle title;

    private StaticHierarchyNode(Class<? extends IRequestablePage> pageClass, PageParameters parameters, BreadcrumbTitle title) {
        this.pageClass = pageClass;
        this.parameters = parameters;
        this.title = title;
    }

    @Override
    public Class<? extends IRequestablePage> getPageClass() {
        return pageClass;
    }

    @Override
    public PageParameters getPageParameters() {
        return parameters;
    }

    @Override
    public BreadcrumbTitle getTitle() {
        return title;
    }

}
