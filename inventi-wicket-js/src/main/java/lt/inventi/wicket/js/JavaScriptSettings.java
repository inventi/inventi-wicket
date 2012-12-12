package lt.inventi.wicket.js;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;

public class JavaScriptSettings {

    public static final JavaScriptSettingsBuilder newBuilder() {
        return new JavaScriptSettingsBuilder() {
            private JQueryUiBuilder jqueryUiBuilder;

            @Override
            public JQueryUiBuilder withJqueryUi(JQueryUiBuilderFactory factory) {
                jqueryUiBuilder = factory.newBuilder(this);
                return jqueryUiBuilder;
            }

            @Override
            public JavaScriptSettings build() {
                return new JavaScriptSettings(jqueryUiBuilder.build());
            }
        };
    }

    private static final MetaDataKey<JavaScriptSettings> KEY = new MetaDataKey<JavaScriptSettings>() { /* empty */ };

    public static void install(Application app, JavaScriptSettings settings) {
        app.setMetaData(KEY, settings);
    }

    public static JavaScriptSettings get(Application app) {
        return app.getMetaData(KEY);
    }

    public static JavaScriptSettings get() {
        return get(Application.get());
    }

    public final JQueryUiSettings jqueryUi;

    public JavaScriptSettings(JQueryUiSettings jqueryUiSettings) {
        this.jqueryUi = jqueryUiSettings;
    }
}
