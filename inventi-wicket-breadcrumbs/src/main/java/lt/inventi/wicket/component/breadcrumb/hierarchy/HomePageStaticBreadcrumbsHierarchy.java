package lt.inventi.wicket.component.breadcrumb.hierarchy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.request.component.IRequestablePage;

import lt.inventi.wicket.component.breadcrumb.Breadcrumb;
import lt.inventi.wicket.component.breadcrumb.StatelessBreadcrumbTargetProvider;
import lt.inventi.wicket.component.breadcrumb.collapse.DisplayedBreadcrumb;
import lt.inventi.wicket.component.breadcrumb.collapse.SingleDisplayedBreadcrumb;

class HomePageStaticBreadcrumbsHierarchy implements IBreadcrumbHierarchy {
    private final HierarchyNode homePageNode;

    HomePageStaticBreadcrumbsHierarchy(HierarchyNode homePageNode) {
        this.homePageNode = homePageNode;
    }

    @Override
    public List<DisplayedBreadcrumb> restoreMissingHierarchy(List<Breadcrumb> originalCrumbs) {
        Class<? extends IRequestablePage> homePage = homePageNode.getPageClass();
        for (Breadcrumb crumb: originalCrumbs) {
            if (crumb.getType().equals(homePage)) {
                return Collections.emptyList();
            }
        }

        StatelessBreadcrumbTargetProvider target = new StatelessBreadcrumbTargetProvider(homePage);
        return Arrays.<DisplayedBreadcrumb> asList(new SingleDisplayedBreadcrumb(homePageNode.getTitle(), target));
    }
}