package lt.inventi.wicket.js;


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

    public final JQueryUiSettings jqueryUi;

    public JavaScriptSettings(JQueryUiSettings jqueryUiSettings) {
        this.jqueryUi = jqueryUiSettings;
    }

    public JavaScriptSettings() {
        this.jqueryUi = new JQueryUiSettings();
    }
}
