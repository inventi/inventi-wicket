package lt.inventi.wicket.component.breadcrumb.hierarchy;

import org.apache.wicket.Application;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import lt.inventi.wicket.component.breadcrumb.BreadcrumbTitle;

public abstract class StaticBreadcrumbHierarchies {

    private StaticBreadcrumbHierarchies() {
        // static utils
    }

    public static IBreadcrumbHierarchy homePageHierarchy(final BreadcrumbTitle title) {
        return new HomePageStaticBreadcrumbsHierarchy(new HierarchyNode() {
            @Override
            public BreadcrumbTitle getTitle() {
                return title;
            }

            @Override
            public PageParameters getPageParameters() {
                return null;
            }

            @Override
            public Class<? extends IRequestablePage> getPageClass() {
                return Application.get().getHomePage();
            }
        });
    }
}
