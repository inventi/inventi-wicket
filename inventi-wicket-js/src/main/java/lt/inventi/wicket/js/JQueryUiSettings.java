package lt.inventi.wicket.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class JQueryUiSettings {

    public final JavaScriptResourceReference uiCoreCore;
    public final JavaScriptResourceReference uiCoreWidget;
    public final JavaScriptResourceReference uiCoreMouse;
    public final JavaScriptResourceReference uiCorePosition;

    public final JavaScriptResourceReference uiWidgetAutocomplete;

    public JQueryUiSettings(JavaScriptResourceReference uiCoreCore, JavaScriptResourceReference uiCoreWidget,
        JavaScriptResourceReference uiCoreMouse, JavaScriptResourceReference uiCorePosition, JavaScriptResourceReference uiWidgetAutocomplete) {
        this.uiCoreCore = uiCoreCore;
        this.uiCoreWidget = uiCoreWidget;
        this.uiCoreMouse = uiCoreMouse;
        this.uiCorePosition = uiCorePosition;
        this.uiWidgetAutocomplete = uiWidgetAutocomplete;
    }


}
