package lt.inventi.wicket.js;

public class JavaScriptSettings {

    public static final JavaScriptSettingsBuilder newBuilder() {
        return new JavaScriptSettingsBuilder() {
            private JQueryUiBuilder jqueryUiBuilder;
            private BootstrapJsBuilder bootstrapJsBuilder;

            @Override
            public JQueryUiBuilder withJqueryUi(JQueryUiBuilderFactory factory) {
                jqueryUiBuilder = factory.newBuilder(this);
                return jqueryUiBuilder;
            }

            @Override
            public BootstrapJsBuilder withBootstrapJs(BootstrapJsBuilderFactory factory) {
                bootstrapJsBuilder = factory.newBuilder(this);
                return bootstrapJsBuilder;
            }

            @Override
            public JavaScriptSettings build() {
                return new JavaScriptSettings(
                    jqueryUiBuilder == null ? new JQueryUiSettings() : jqueryUiBuilder.build(),
                    bootstrapJsBuilder == null ? new BootstrapJsSettings() : bootstrapJsBuilder.build());
            }
        };
    }

    public final JQueryUiSettings jqueryUi;
    public final BootstrapJsSettings bootstrapJs;

    public JavaScriptSettings(JQueryUiSettings jqueryUiSettings, BootstrapJsSettings bootstrapJsSettings) {
        this.jqueryUi = jqueryUiSettings;
        this.bootstrapJs = bootstrapJsSettings;
    }

    public JavaScriptSettings() {
        this.jqueryUi = new JQueryUiSettings();
        this.bootstrapJs = new BootstrapJsSettings();
    }
}
