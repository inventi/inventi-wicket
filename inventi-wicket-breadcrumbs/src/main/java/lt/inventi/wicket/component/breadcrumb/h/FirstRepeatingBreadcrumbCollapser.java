package lt.inventi.wicket.component.breadcrumb.h;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

class FirstRepeatingBreadcrumbCollapser implements IBreadcrumbCollapser {

    @Override
    public List<DisplayedBreadcrumb> collapse(List<Breadcrumb> crumbs) {
        ArrayList<Breadcrumb> reversedCrumbs = new ArrayList<Breadcrumb>(crumbs);
        Collections.reverse(reversedCrumbs);

        List<DisplayedBreadcrumb> acc = new ArrayList<DisplayedBreadcrumb>();

        Iterator<Breadcrumb> forward = crumbs.iterator();
        Iterator<Breadcrumb> backwards = reversedCrumbs.iterator();

        Breadcrumb lastFound = null;

        while (forward.hasNext()) {
            Breadcrumb f = forward.next();
            while (backwards.hasNext()) {
                Breadcrumb b = backwards.next();
                if (f == b) {
                    // nothing to collapse
                    break;
                } else if (f.getType().equals(b.getType())) {
                    lastFound = b;
                    break;
                }
            }
            if (lastFound == null) {
                backwards = reversedCrumbs.iterator();
                acc.add(new SingleDisplayedBreadcrumb(f));
            } else {
                List<Breadcrumb> collapsed = new ArrayList<Breadcrumb>();
                collapsed.add(f);
                while (forward.hasNext()) {
                    Breadcrumb nextCollapsed = forward.next();
                    if (nextCollapsed == lastFound) {
                        break;
                    }
                    collapsed.add(nextCollapsed);
                }
                acc.add(new CollapsedDisplayedBreadcrumb(collapsed));

                acc.add(new SingleDisplayedBreadcrumb(lastFound));
                while (forward.hasNext()) {
                    acc.add(new SingleDisplayedBreadcrumb(forward.next()));
                }
            }
        }

        return acc;
    }

    private static class CollapsedDisplayedBreadcrumb implements DisplayedBreadcrumb {
        private final List<DisplayedBreadcrumb> collapsed;

        public CollapsedDisplayedBreadcrumb(List<Breadcrumb> crumbs) {
            if (crumbs == null || crumbs.isEmpty()) {
                throw new IllegalArgumentException("Must contain at least one collapsed breadcrumb!");
            }

            List<DisplayedBreadcrumb> result = new ArrayList<DisplayedBreadcrumb>();
            for (Breadcrumb b : crumbs) {
                result.add(new SingleDisplayedBreadcrumb(b));
            }
            collapsed = Collections.unmodifiableList(result);
        }

        @Override
        public IModel<String> title() {
            return Model.of("...");
        }

        @Override
        public boolean isCollapsed() {
            return true;
        }

        @Override
        public List<DisplayedBreadcrumb> collapsedCrumbs() {
            return collapsed;
        }

    }

    private static class SingleDisplayedBreadcrumb implements DisplayedBreadcrumb {
        private final Breadcrumb crumb;

        public SingleDisplayedBreadcrumb(Breadcrumb crumb) {
            this.crumb = crumb;
        }

        @Override
        public IModel<String> title() {
            return crumb.getTitleModel();
        }

        @Override
        public boolean isCollapsed() {
            return false;
        }

        @Override
        public List<DisplayedBreadcrumb> collapsedCrumbs() {
            return Collections.emptyList();
        }
    }
}
