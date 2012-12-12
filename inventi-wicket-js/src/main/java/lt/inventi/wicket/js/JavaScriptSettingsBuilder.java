package lt.inventi.wicket.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public interface JavaScriptSettingsBuilder {

    interface JQueryUiBuilderFactory {
        JQueryUiBuilder newBuilder(JavaScriptSettingsBuilder settingsBuilder);
    }

    interface UiCoreBuilder {
        UiCoreBuilder withUiCoreWidget(JavaScriptResourceReference widget);

        UiCoreBuilder withUiCoreMouse(JavaScriptResourceReference mouse);

        UiCoreBuilder withUiCorePosition(JavaScriptResourceReference mouse);

        JQueryUiBuilder endUiCore();
    }

    interface UiWidgetsBuilder {
        UiWidgetsBuilder withUiWidgetsAutocomplete(JavaScriptResourceReference autocomplete);

        JQueryUiBuilder endUiWidgets();
    }

    interface JQueryUiBuilder {
        JQueryUiBuilder withAllUiCore(JavaScriptResourceReference core);

        UiCoreBuilder withUiCore(JavaScriptResourceReference core);

        UiWidgetsBuilder withUiWidgets();

        JavaScriptSettingsBuilder endJqueryUi();

        JQueryUiSettings build();
    }

    static class JQueryUi192Builder implements JQueryUiBuilder {
        class CoreBuilder implements UiCoreBuilder {
            @Override
            public UiCoreBuilder withUiCoreWidget(JavaScriptResourceReference widget) {
                JQueryUi192Builder.this.uiCoreWidget = widget;
                return this;
            }

            @Override
            public UiCoreBuilder withUiCorePosition(JavaScriptResourceReference position) {
                JQueryUi192Builder.this.uiCoreMouse = position;
                return this;
            }

            @Override
            public UiCoreBuilder withUiCoreMouse(JavaScriptResourceReference mouse) {
                JQueryUi192Builder.this.uiCorePosition = mouse;
                return this;
            }

            @Override
            public JQueryUiBuilder endUiCore() {
                return JQueryUi192Builder.this;
            }
        }

        class WidgetsBuilder implements UiWidgetsBuilder {
            @Override
            public UiWidgetsBuilder withUiWidgetsAutocomplete(JavaScriptResourceReference autocomplete) {
                JQueryUi192Builder.this.uiWidgetsAutocomplete = autocomplete;
                return this;
            }

            @Override
            public JQueryUiBuilder endUiWidgets() {
                return JQueryUi192Builder.this;
            }
        }

        private final JavaScriptSettingsBuilder settingsBuilder;
        private JavaScriptResourceReference uiCoreCore, uiCoreWidget, uiCorePosition, uiCoreMouse;
        private JavaScriptResourceReference uiWidgetsAutocomplete;

        public JQueryUi192Builder(JavaScriptSettingsBuilder settingsBuilder) {
            this.settingsBuilder = settingsBuilder;
        }

        @Override
        public JQueryUiBuilder withAllUiCore(JavaScriptResourceReference core) {
            throw new UnsupportedOperationException();
        }

        @Override
        public UiCoreBuilder withUiCore(JavaScriptResourceReference core) {
            this.uiCoreCore = core;
            return new CoreBuilder();
        }

        @Override
        public JavaScriptSettingsBuilder endJqueryUi() {
            return settingsBuilder;
        }

        @Override
        public UiWidgetsBuilder withUiWidgets() {
            return new WidgetsBuilder();
        }

        @Override
        public JQueryUiSettings build() {
            return new JQueryUiSettings(uiCoreCore, uiCoreWidget, uiCorePosition, uiCoreMouse, uiWidgetsAutocomplete);
        }

    }

    enum JQueryUiVersions implements JQueryUiBuilderFactory {
        v1_9_2 {
            @Override
            public JQueryUiBuilder newBuilder(final JavaScriptSettingsBuilder settingsBuilder) {
                return new JQueryUi192Builder(settingsBuilder);
            }
        }
    }

    JQueryUiBuilder withJqueryUi(JQueryUiBuilderFactory factory);

    JavaScriptSettings build();
}