package lt.inventi.wicket.resource;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;

import lt.inventi.apollo.wicket.theme.settings.ThemeSettings;
import lt.inventi.wicket.js.JavaScriptSettings;

public class ResourceSettings {

    private static final MetaDataKey<ResourceSettings> KEY = new MetaDataKey<ResourceSettings>() { /* empty */
    };

    public static void installEmpty(Application app) {
        app.setMetaData(KEY, new ResourceSettings(new ThemeSettings(), new JavaScriptSettings()));
    }

    public static void install(Application app, ResourceSettings settings) {
        app.setMetaData(KEY, settings);
    }

    public static ResourceSettings get(Application app) {
        ResourceSettings settings = app.getMetaData(KEY);
        return settings;
    }

    public static ResourceSettings get() {
        return get(Application.get());
    }

    private final ThemeSettings theme;
    private final JavaScriptSettings js;

    public ResourceSettings(ThemeSettings theme, JavaScriptSettings js) {
        this.theme = theme;
        this.js = js;
    }

    public ThemeSettings themeSettings() {
        return theme;
    }

    public JavaScriptSettings js() {
        return js;
    }
}
