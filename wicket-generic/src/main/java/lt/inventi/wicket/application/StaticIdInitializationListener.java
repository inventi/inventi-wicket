package lt.inventi.wicket.application;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.application.IComponentInitializationListener;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.repeater.AbstractRepeater;
import org.apache.wicket.markup.resolver.IComponentResolver;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * Generates permanent IDs for wicket components.
 */
public class StaticIdInitializationListener implements IComponentInitializationListener {

    private static final MetaDataKey<List<String>> ID_CACHE = new MetaDataKey<List<String>>() {};
    private static final String SEPARATOR = "_";

    @Override
    public void onInitialize(Component component) {
        if(needMarkupId(component)){
            component.setOutputMarkupId(true);
            component.setMarkupId(getMarkupId(component));
        }
    }

    private static String getMarkupId(Component component) {
        if(component instanceof WebPage){
            getCache().clear();
        }

        String id = null;
        if (isInRepeater(component)) {
            id = getRepeatersComponentId(component);
        } else {
            List<String> idCache = getCache();
            id = component.getId();
            Component parent = component;
            while (idCache.contains(id)) {
                parent = parent.getParent();
                id = parent.getId() + SEPARATOR + id;
            }
            idCache.add(id);
        }
        //there might be situation that ID contains dots.
        id = id.replace(".", SEPARATOR);
        return id;
    }

    private static List<String> getCache() {
        List<String> idCache = RequestCycle.get().getMetaData(ID_CACHE);
        if(idCache == null){
            idCache = new ArrayList<String>();
            RequestCycle.get().setMetaData(ID_CACHE, idCache);
        }
        return idCache;
    }

    private static String getRepeatersComponentId(final Component component) {
        Component parent = component;
        Component lastRepeater = null;
        while (!(parent instanceof WebPage)) {
            if (parent instanceof AbstractRepeater) {
                lastRepeater = parent;
            }
            parent = parent.getParent();
        }

        String id = component.getId();
        if (lastRepeater == null) {
            return id;
        }

        parent = component;
        while (!(parent == lastRepeater)) {
            parent = parent.getParent();
            id = parent.getId() + SEPARATOR + id;
        }
        return id;
    }

    private static boolean isInRepeater(Component component) {
        Component parent = component.findParent(AbstractRepeater.class);
        return parent != null;
    }

    private static boolean needMarkupId(Component component) {
        if(component instanceof IComponentResolver && !(component instanceof Border)){
            return false;
        }

        if(component instanceof WebComponent && !(component instanceof Label)){
            return false;
        }

        return true;
    }

}
