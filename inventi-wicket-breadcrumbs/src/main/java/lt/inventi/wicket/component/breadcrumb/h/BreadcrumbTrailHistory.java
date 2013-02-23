package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;


class BreadcrumbTrailHistory implements Serializable {

    private static final MetaDataKey<BreadcrumbTrailHistory> CONTAINER = new MetaDataKey<BreadcrumbTrailHistory>(){ /* empty */ };

    private static BreadcrumbTrailHistory get() {
        Session session = Session.get();
        BreadcrumbTrailHistory container = session.getMetaData(CONTAINER);
        if (container == null) {
            container = new BreadcrumbTrailHistory();
            session.setMetaData(CONTAINER, container);
        }

        if (session.isTemporary()) {
            session.bind();
        }
        return container;
    }

    static void extendTrail(String maybeTrailId, Breadcrumb newCrumb) {
        BreadcrumbTrailHistory history = get();
        Breadcrumb crumb = history.useExistingCrumbIfPossible(newCrumb);

        PersistentList previousTrail = history.breadcrumbMap.get(maybeTrailId);
        history.breadcrumbMap.put(crumb.getId(), new PersistentList(previousTrail, crumb));
    }

    /**
     * Tries to get the nth breadcrumb from the end for the provided trail id.
     * <p>
     * In case provided trail id is null or no breadcrumb history exists for the
     * id, nothing is returned.
     *
     * @param maybeTrailId
     * @return (trail size - n)th breadcrumb from the history associated with
     *         the provided trail or null if no trail exists
     */
    static Breadcrumb getLastBreadcrumbFor(String maybeTrailId) {
        BreadcrumbTrailHistory history = get();
        if (maybeTrailId != null && history.breadcrumbMap.containsKey(maybeTrailId)) {
            PersistentList trail = history.breadcrumbMap.get(maybeTrailId);
            return trail.tail;
        }
        return null;
    }

    static Breadcrumb getPenultimateBreadcrumbFor(String maybeTrailId) {
        BreadcrumbTrailHistory history = get();
        if (maybeTrailId != null && history.breadcrumbMap.containsKey(maybeTrailId)) {
            PersistentList trail = history.breadcrumbMap.get(maybeTrailId);
            if (trail.first != null) {
                return trail.first.tail;
            }
        }
        return null;
    }

    static List<Breadcrumb> getTrail(String trailId) {
        return get().breadcrumbMap.get(trailId).toList();
    }

    private final Map<String, Breadcrumb> crumbsByPageIdClass = new HashMap<String, Breadcrumb>();
    private final Map<String, PersistentList> breadcrumbMap = new LinkedHashMap<String, PersistentList>();

    /**
     * This allows us to update breadcrumb titles in the existing trails. We
     * also share breadcrumb objects across trails instead of always storing new
     * ones on each render.
     */
    private Breadcrumb useExistingCrumbIfPossible(Breadcrumb newCrumb) {
        Breadcrumb crumb = crumbsByPageIdClass.get(newCrumb.getStableId());
        if (crumb != null) {
            crumb.updateWith(newCrumb);
        } else {
            crumb = newCrumb;
            crumbsByPageIdClass.put(crumb.getStableId(), crumb);
        }
        return crumb;
    }

    private static class PersistentList implements Serializable {
        final PersistentList first;
        final Breadcrumb tail;

        PersistentList(PersistentList first, Breadcrumb tail) {
            this.first = first;
            this.tail = tail;
        }

        public List<Breadcrumb> toList() {
            List<Breadcrumb> result = new ArrayList<Breadcrumb>();
            result.add(tail);
            if (first == null) {
                return result;
            }

            PersistentList prev = first;
            while (prev != null) {
                result.add(prev.tail);
                prev = prev.first;
            }
            Collections.reverse(result);
            return result;
        }
    }

}
