package lt.inventi.wicket.component.breadcrumb.h;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

    public static void extendTrail(String maybeTrailId, Breadcrumb crumb) {
        BreadcrumbTrailHistory container = get();
        final List<Breadcrumb> trail;
        if (maybeTrailId == null || !container.breadcrumbMap.containsKey(maybeTrailId)) {
            trail = Arrays.asList(crumb);
        } else {
            List<Breadcrumb> previousTrail = container.breadcrumbMap.get(maybeTrailId);
            trail = new ArrayList<Breadcrumb>(previousTrail);
            trail.add(crumb);
        }
        container.breadcrumbMap.put(crumb.getId().toString(), Collections.unmodifiableList(trail));
    }

    public static List<Breadcrumb> getTrail(String trailId) {
        return get().breadcrumbMap.get(trailId);
    }

    private final Map<String, List<Breadcrumb>> breadcrumbMap = new HashMap<String, List<Breadcrumb>>();

    public static String getFullHistory() {
        return get().breadcrumbMap.toString();
    }

}
