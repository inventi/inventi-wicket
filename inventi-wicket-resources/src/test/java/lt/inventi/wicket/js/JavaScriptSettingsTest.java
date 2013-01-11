package lt.inventi.wicket.js;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.junit.Test;

import lt.inventi.wicket.js.JavaScriptSettingsBuilder.BootstrapJsVersions;
import lt.inventi.wicket.js.JavaScriptSettingsBuilder.JQueryUiVersions;

public class JavaScriptSettingsTest {

    @Test
    public void shouldSetResourcesForSeparateJQueryUiCoreFiles() {
        JavaScriptResourceReference core = new JavaScriptResourceReference(getClass(), "core");
        JavaScriptResourceReference mouse = new JavaScriptResourceReference(getClass(), "mouse");
        JavaScriptResourceReference position = new JavaScriptResourceReference(getClass(), "position");

        JavaScriptSettings settings = JavaScriptSettings.newBuilder().withJqueryUi(JQueryUiVersions.v1_9_2)
            .withUiCore(core)
            .withUiCoreMouse(mouse)
            .withUiCorePosition(position)
            .endUiCore().endJqueryUi().build();

        assertThat(settings.jqueryUi.uiCoreCore, is(core));
        assertThat(settings.jqueryUi.uiCorePosition, is(position));
        assertThat(settings.jqueryUi.uiCoreMouse, is(mouse));
    }

    @Test
    public void shouldCheckAutocompleteWidgetDependencies() {
        final JavaScriptResourceReference core = new JavaScriptResourceReference(getClass(), "core");
        final JavaScriptResourceReference widget = new JavaScriptResourceReference(getClass(), "widget") {
            @Override
            public Iterable<? extends HeaderItem> getDependencies() {
                return Arrays.asList(forReference(core));
            }
        };
        final JavaScriptResourceReference position = new JavaScriptResourceReference(getClass(), "position");
        final JavaScriptResourceReference menu = new JavaScriptResourceReference(getClass(), "menu") {
            @Override
            public Iterable<? extends HeaderItem> getDependencies() {
                return Arrays.asList(forReference(core), forReference(widget), forReference(position));
            }
        };
        JavaScriptResourceReference autocomplete = new JavaScriptResourceReference(getClass(), "autocomplete") {
            @Override
            public Iterable<? extends HeaderItem> getDependencies() {
                return Arrays.asList(forReference(core), forReference(widget), forReference(position), forReference(menu));
            }
        };

        JavaScriptSettings settings = JavaScriptSettings.newBuilder().withJqueryUi(JQueryUiVersions.v1_9_2)
                .withUiCore(core)
                .withUiCoreWidget(widget)
                .withUiCorePosition(position)
            .endUiCore()
            .withUiWidgets()
                .withUiWidgetsMenu(menu)
                .withUiWidgetsAutocomplete(autocomplete)
            .endUiWidgets()
            .endJqueryUi().build();

        assertThat(settings.jqueryUi.uiWidgetMenu, is(menu));
        assertThat(settings.jqueryUi.uiWidgetAutocomplete, is(autocomplete));
    }

    @Test
    public void usesPackagedJQueryUiCoreInsteadOfSeparateComponents() {
        JavaScriptResourceReference fullCore = new JavaScriptResourceReference(getClass(), "full-core");

        JavaScriptSettings settings = JavaScriptSettings.newBuilder().withJqueryUi(JQueryUiVersions.v1_9_2)
            .withAllUiCore(fullCore).endJqueryUi().build();

        assertThat(settings.jqueryUi.uiCoreCore, is(fullCore));
        assertThat(settings.jqueryUi.uiCorePosition, is(fullCore));
        assertThat(settings.jqueryUi.uiCoreMouse, is(fullCore));
        assertThat(settings.jqueryUi.uiCoreWidget, is(fullCore));
    }

    @Test
    public void usesPackagedBootstrapJsInsteadOfSeparateComponents() {
        JavaScriptResourceReference fullBootstrapJs = new JavaScriptResourceReference(getClass(), "full-bootstrap");

        JavaScriptSettings settings = JavaScriptSettings.newBuilder()
            .withBootstrapJs(BootstrapJsVersions.v2_x)
                .withAllBootstrapJs(fullBootstrapJs)
            .endBootstrapJs().build();

        assertThat(settings.bootstrapJs.bsTransitions, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsModal, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsDropdown, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsScrollspy, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsTab, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsTooltip, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsPopover, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsAlert, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsButton, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsCollapse, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsCarousel, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsTypeahead, is(fullBootstrapJs));
        assertThat(settings.bootstrapJs.bsAffix, is(fullBootstrapJs));
    }
}
