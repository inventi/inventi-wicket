package lt.inventi.wicket.component.breadcrumb.collapse;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lt.inventi.wicket.component.breadcrumb.BEquality;

abstract class CollapseUtils {

    private CollapseUtils() {
        // static utils
    }

    static <F, T> List<T> collapse(List<F> source, BEquality<F> sameEq, BEquality<F> collapseEq,
        BFun<F, T> transformSingle, BFun<Iterable<F>, T> transformCollapsed, int times) {
        List<T> acc = new ArrayList<T>();

        Iterator<F> forward = source.iterator();
        Iterator<F> forward2 = source.iterator();

        F collapseTo = null;

        while (forward.hasNext()) {
            F f = forward.next();
            int timesFound = 0;
            while (forward2.hasNext()) {
                F b = forward2.next();
                if (sameEq.equal(f, b)) {
                    continue;
                } else if (collapseEq.equal(f, b) && ++timesFound == times) {
                    collapseTo = b;
                    break;
                }
            }
            if (collapseTo == null) {
                forward2 = source.iterator();
                acc.add(transformSingle.apply(f));
            } else {
                List<F> collapsed = new ArrayList<F>();
                collapsed.add(f);
                while (forward.hasNext()) {
                    F nextCollapsed = forward.next();
                    if (nextCollapsed == collapseTo) {
                        break;
                    }
                    collapsed.add(nextCollapsed);
                }
                acc.add(transformCollapsed.apply(collapsed));

                acc.add(transformSingle.apply(collapseTo));
                while (forward.hasNext()) {
                    acc.add(transformSingle.apply(forward.next()));
                }
            }
        }

        return acc;
    }
}
