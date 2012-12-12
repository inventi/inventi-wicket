package lt.inventi.wicket.js;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class JQueryUiSettings {

    public final JavaScriptResourceReference uiCoreCore;
    public final JavaScriptResourceReference uiCoreWidget;
    public final JavaScriptResourceReference uiCoreMouse;
    public final JavaScriptResourceReference uiCorePosition;

    public JQueryUiSettings(JavaScriptResourceReference uiCoreCore, JavaScriptResourceReference uiCoreWidget, JavaScriptResourceReference uiCoreMouse, JavaScriptResourceReference uiCorePosition) {
        this.uiCoreCore = uiCoreCore;
        this.uiCoreWidget = uiCoreWidget;
        this.uiCoreMouse = uiCoreMouse;
        this.uiCorePosition = uiCorePosition;
    }

}
