package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.List;

import lt.inventi.wicket.component.breadcrumb.BEquality;
import lt.inventi.wicket.component.breadcrumb.Breadcrumb;

public class RepeatingBreadcrumbCollapser implements IBreadcrumbCollapser {

    private final int timesToRepeatBeforeCollapse;
    private BEquality<Breadcrumb> collapsibilityEq;

    public RepeatingBreadcrumbCollapser(int timesToRepeatBeforeCollapse, BEquality<Breadcrumb> collapsibilityEq) {
        this.timesToRepeatBeforeCollapse = timesToRepeatBeforeCollapse;
        this.collapsibilityEq = collapsibilityEq;
    }

    @Override
    public List<DisplayedBreadcrumb> collapse(List<Breadcrumb> crumbs) {
        return CollapseUtils.collapse(crumbs, new BEquality<Breadcrumb>() {
            @Override
            public boolean equal(Breadcrumb a, Breadcrumb b) {
                return a == b;
            }
        }, collapsibilityEq, new BFun<Breadcrumb, DisplayedBreadcrumb>() {
            @Override
            public DisplayedBreadcrumb apply(Breadcrumb f) {
                return new SingleDisplayedBreadcrumb(f.title(), f);
            }
        }, new BFun<Iterable<Breadcrumb>, DisplayedBreadcrumb>() {
            @Override
            public DisplayedBreadcrumb apply(Iterable<Breadcrumb> f) {
                return new CollapsedDisplayedBreadcrumb(f);
            }
        }, timesToRepeatBeforeCollapse);
    }
}
