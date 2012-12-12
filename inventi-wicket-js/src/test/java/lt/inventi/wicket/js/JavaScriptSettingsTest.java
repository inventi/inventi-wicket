package lt.inventi.wicket.js;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.apache.wicket.markup.head.HeaderItem;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.junit.Test;

import lt.inventi.wicket.js.JavaScriptSettingsBuilder.JQueryUiVersions;

public class JavaScriptSettingsTest {

    @Test
    public void shouldSetResourcesForSeparateJQueryUiCoreFiles() {
        JavaScriptResourceReference core = new JavaScriptResourceReference(getClass(), "core");
        JavaScriptResourceReference widget = new JavaScriptResourceReference(getClass(), "widget");
        JavaScriptResourceReference mouse = new JavaScriptResourceReference(getClass(), "mouse");
        JavaScriptResourceReference position = new JavaScriptResourceReference(getClass(), "position");

        JavaScriptSettings settings = JavaScriptSettings.newBuilder().withJqueryUi(JQueryUiVersions.v1_9_2)
            .withUiCore(core)
            .withUiCoreWidget(widget)
            .withUiCoreMouse(mouse)
            .withUiCorePosition(position)
            .endUiCore().endJqueryUi().build();

        assertThat(settings.jqueryUi.uiCoreCore, is(core));
        assertThat(settings.jqueryUi.uiCorePosition, is(position));
        assertThat(settings.jqueryUi.uiCoreMouse, is(mouse));
        assertThat(settings.jqueryUi.uiCoreWidget, is(widget));
    }

    @Test
    public void shouldCheckAutocompleteWidgetDependencies() {
        final JavaScriptResourceReference core = new JavaScriptResourceReference(getClass(), "core");
        final JavaScriptResourceReference widget = new JavaScriptResourceReference(getClass(), "widget");
        final JavaScriptResourceReference position = new JavaScriptResourceReference(getClass(), "position");
        JavaScriptResourceReference autocomplete = new JavaScriptResourceReference(getClass(), "autocomplete") {
            @Override
            public Iterable<? extends HeaderItem> getDependencies() {
                return Arrays.asList(forReference(core), forReference(widget), forReference(position));
            }
        };

        JavaScriptSettings settings = JavaScriptSettings.newBuilder().withJqueryUi(JQueryUiVersions.v1_9_2)
                .withUiCore(core)
                .withUiCoreWidget(widget)
                .withUiCorePosition(position)
            .endUiCore()
            .withUiWidgets()
                .withUiWidgetsAutocomplete(autocomplete)
            .endUiWidgets()
            .endJqueryUi().build();

        assertThat(settings.jqueryUi.uiWidgetAutocomplete, is(autocomplete));
    }
}
