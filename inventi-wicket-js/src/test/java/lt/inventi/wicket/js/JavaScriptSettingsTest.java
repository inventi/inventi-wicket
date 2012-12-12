package lt.inventi.wicket.js;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
        JavaScriptResourceReference autocomplete = new JavaScriptResourceReference(getClass(), "autocomplete");

        JavaScriptSettingsBuilder builder = JavaScriptSettings.newBuilder();
        JavaScriptSettings settings = builder.withJqueryUi(JQueryUiVersions.v1_9_2)
                .withUiCore(core)
                .withUiCoreWidget(widget)
                .withUiCoreMouse(mouse)
                .withUiCorePosition(position)
            .endUiCore()
            .withUiWidgets()
                .withUiWidgetsAutocomplete(autocomplete)
            .endUiWidgets()
            .endJqueryUi().build();

        assertThat(settings.jqueryUi.uiCoreCore, is(core));
        assertThat(settings.jqueryUi.uiCorePosition, is(position));
        assertThat(settings.jqueryUi.uiCoreMouse, is(mouse));
        assertThat(settings.jqueryUi.uiCoreWidget, is(widget));
        assertThat(settings.jqueryUi.uiWidgetAutocomplete, is(autocomplete));
    }
}
