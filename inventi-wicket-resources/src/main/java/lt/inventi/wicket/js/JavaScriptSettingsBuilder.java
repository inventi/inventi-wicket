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

    interface BootstrapJsBuilderFactory {
        BootstrapJsBuilder newBuilder(JavaScriptSettingsBuilder settingsBuilder);
    }

    interface BootstrapJsBuilder {
        BootstrapJsBuilder withAllBootstrapJs(JavaScriptResourceReference all);

        BootstrapJsSettings build();

        JavaScriptSettingsBuilder endBootstrapJs();
    }

    static class BootstrapJs2xBuilder implements BootstrapJsBuilder {

        private final JavaScriptSettingsBuilder settingsBuilder;
        private JavaScriptResourceReference bsTransitions, bsModal, bsDropdown, bsScrollspy, bsTab,
            bsTooltip, bsPopover, bsAlert, bsButton, bsCollapse, bsCarousel, bsTypeahead, bsAffix;

        public BootstrapJs2xBuilder(JavaScriptSettingsBuilder settingsBuilder) {
            this.settingsBuilder = settingsBuilder;
        }

        @Override
        public BootstrapJsBuilder withAllBootstrapJs(JavaScriptResourceReference all) {
            this.bsTransitions = bsModal = bsDropdown = bsScrollspy = bsTab =
                bsTooltip = bsPopover = bsAlert = bsButton = bsCollapse = bsCarousel = bsTypeahead = bsAffix = all;

            return this;
        }

        @Override
        public JavaScriptSettingsBuilder endBootstrapJs() {
            return settingsBuilder;
        }

        @Override
        public BootstrapJsSettings build() {
            return new BootstrapJsSettings(bsTransitions, bsModal, bsDropdown, bsScrollspy, bsTab,
                bsTooltip, bsPopover, bsAlert, bsButton, bsCollapse, bsCarousel, bsTypeahead, bsAffix);
        }

    }

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
        UiWidgetsBuilder withUiWidgetsMenu(JavaScriptResourceReference menu);

        UiWidgetsBuilder withUiWidgetsAutocomplete(JavaScriptResourceReference autocomplete);

        JQueryUiBuilder endUiWidgets();
    }

    interface JQueryUiBuilder {
        JQueryUiBuilder withAllUi(JavaScriptResourceReference jqueryUi);

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
                ensureContains(toSet(widget.getDependencies()), JQueryUi192Builder.this.uiCoreCore);

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
            public UiWidgetsBuilder withUiWidgetsMenu(JavaScriptResourceReference menu) {
                Set<ResourceReference> dependencies = toSet(menu.getDependencies());
                ensureContains(dependencies, JQueryUi192Builder.this.uiCoreCore,
                                             JQueryUi192Builder.this.uiCoreWidget,
                                             JQueryUi192Builder.this.uiCorePosition);

                JQueryUi192Builder.this.uiWidgetsMenu = menu;
                return this;
            }

            @Override
            public UiWidgetsBuilder withUiWidgetsAutocomplete(JavaScriptResourceReference autocomplete) {
                Set<ResourceReference> dependencies = toSet(autocomplete.getDependencies());
                ensureContains(dependencies, JQueryUi192Builder.this.uiCoreCore,
                                             JQueryUi192Builder.this.uiCoreWidget,
                                             JQueryUi192Builder.this.uiCorePosition,
                                             JQueryUi192Builder.this.uiWidgetsMenu);

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
        private JavaScriptResourceReference uiWidgetsAutocomplete, uiWidgetsMenu;

        public JQueryUi192Builder(JavaScriptSettingsBuilder settingsBuilder) {
            this.settingsBuilder = settingsBuilder;
        }

        @Override
        public JQueryUiBuilder withAllUi(JavaScriptResourceReference jqueryUi) {
            withAllUiCore(jqueryUi);
            this.uiWidgetsAutocomplete = this.uiWidgetsMenu = jqueryUi;
            return this;
        }

        @Override
        public JQueryUiBuilder withAllUiCore(JavaScriptResourceReference fullCore) {
            this.uiCoreCore = this.uiCoreWidget = this.uiCorePosition = this.uiCoreMouse = fullCore;
            return this;
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
            return new JQueryUiSettings(uiCoreCore, uiCoreWidget, uiCoreMouse, uiCorePosition,
                uiWidgetsMenu, uiWidgetsAutocomplete);
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

    public enum BootstrapJsVersions implements BootstrapJsBuilderFactory {
        v2_x {
            @Override
            public BootstrapJsBuilder newBuilder(JavaScriptSettingsBuilder settingsBuilder) {
                return new BootstrapJs2xBuilder(settingsBuilder);
            }
        }
    }

    public enum JQueryUiVersions implements JQueryUiBuilderFactory {
        v1_9_2 {
            @Override
            public JQueryUiBuilder newBuilder(final JavaScriptSettingsBuilder settingsBuilder) {
                return new JQueryUi192Builder(settingsBuilder);
            }
        }
    }

    BootstrapJsBuilder withBootstrapJs(BootstrapJsBuilderFactory factory);

    JQueryUiBuilder withJqueryUi(JQueryUiBuilderFactory factory);

    JavaScriptSettings build();
}