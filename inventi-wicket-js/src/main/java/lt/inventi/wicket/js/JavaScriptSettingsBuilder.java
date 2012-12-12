package lt.inventi.wicket.js;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.markup.head.IReferenceHeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

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
                JQueryUi192Builder.this.uiCorePosition = position;
                return this;
            }

            @Override
            public UiCoreBuilder withUiCoreMouse(JavaScriptResourceReference mouse) {
                JQueryUi192Builder.this.uiCoreMouse = mouse;
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
                Set<ResourceReference> dependencies = toSet(autocomplete.getDependencies());
                ensureContains(dependencies, JQueryUi192Builder.this.uiCoreCore,
                                             JQueryUi192Builder.this.uiCoreWidget,
                                             JQueryUi192Builder.this.uiCorePosition);
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
            return new JQueryUiSettings(uiCoreCore, uiCoreWidget, uiCoreMouse, uiCorePosition, uiWidgetsAutocomplete);
        }

        private static void ensureContains(Set<ResourceReference> dependences, ResourceReference... expected) {
            for (ResourceReference dep : expected) {
                if (dep == null || !dependences.contains(dep)) {
                    throw new IllegalStateException("Expected dependency " + dep + " doesn't exist in " + expected + "!");
                }
            }
        }

        private static Set<ResourceReference> toSet(Iterable<? extends HeaderItem> dependencies) {
            Set<ResourceReference> result = new HashSet<ResourceReference>();
            for (HeaderItem dep : dependencies) {
                if (dep instanceof IReferenceHeaderItem) {
                    result.add(((IReferenceHeaderItem) dep).getReference());
                }
                result.addAll(dependenciesOf(dep));
            }
            return result;
        }

        private static Collection<ResourceReference> dependenciesOf(HeaderItem dep) {
            if (!dep.getDependencies().iterator().hasNext()) {
                return Collections.emptySet();
            }

            Set<ResourceReference> result = new HashSet<ResourceReference>();
            result.addAll(toSet(dep.getDependencies()));
            return result;
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