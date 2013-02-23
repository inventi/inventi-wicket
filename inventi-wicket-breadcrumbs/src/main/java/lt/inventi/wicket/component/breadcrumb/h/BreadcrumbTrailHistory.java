package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;

public class BreadcrumbTrailHistory implements Serializable {

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

    public static void extendTrail(String maybeTrailId, Breadcrumb newCrumb) {
        BreadcrumbTrailHistory history = get();
        Breadcrumb crumb = history.useExistingCrumbIfPossible(newCrumb);

        final List<Breadcrumb> trail;
        if (maybeTrailId == null || !history.breadcrumbMap.containsKey(maybeTrailId)) {
            trail = Arrays.asList(crumb);
        } else {
            List<Breadcrumb> previousTrail = history.breadcrumbMap.get(maybeTrailId);
            trail = new ArrayList<Breadcrumb>(previousTrail);
            trail.add(crumb);
        }
        history.breadcrumbMap.put(crumb.getId(), Collections.unmodifiableList(trail));
    }

    public static Breadcrumb getLastBreadcrumbFor(String maybeTrailId) {
        return getNthBreadcrumbFromEndFor(maybeTrailId, 1);
    }

    public static Breadcrumb getPenultimateBreadcrumbFor(String maybeTrailId) {
        return getNthBreadcrumbFromEndFor(maybeTrailId, 2);
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
    private static Breadcrumb getNthBreadcrumbFromEndFor(String maybeTrailId, int n) {
        BreadcrumbTrailHistory history = get();
        if (maybeTrailId != null && history.breadcrumbMap.containsKey(maybeTrailId)) {
            List<Breadcrumb> trail = history.breadcrumbMap.get(maybeTrailId);
            if (trail.size() > n - 1) {
                return trail.get(trail.size() - n);
            }
        }
        return null;
    }

    public static List<Breadcrumb> getTrail(String trailId) {
        return get().breadcrumbMap.get(trailId);
    }

    public static String getFullHistory() {
        return get().breadcrumbMap.toString();
    }

    private final Map<String, Breadcrumb> crumbsByPageIdClass = new HashMap<String, Breadcrumb>();
    private final Map<String, List<Breadcrumb>> breadcrumbMap = new LinkedHashMap<String, List<Breadcrumb>>();

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

}
