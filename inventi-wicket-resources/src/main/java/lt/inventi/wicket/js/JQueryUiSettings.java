package lt.inventi.wicket.js;

import static lt.inventi.wicket.js.EmptyResourceReference.get;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class JQueryUiSettings {

    public final JavaScriptResourceReference uiCoreCore;
    public final JavaScriptResourceReference uiCoreWidget;
    public final JavaScriptResourceReference uiCoreMouse;
    public final JavaScriptResourceReference uiCorePosition;

    public final JavaScriptResourceReference uiWidgetMenu;
    public final JavaScriptResourceReference uiWidgetAutocomplete;

    JQueryUiSettings() {
        this(get(), get(), get(), get(), get(), get());
    }

    public JQueryUiSettings(JavaScriptResourceReference uiCoreCore, JavaScriptResourceReference uiCoreWidget,
        JavaScriptResourceReference uiCoreMouse, JavaScriptResourceReference uiCorePosition, JavaScriptResourceReference uiWidgetMenu, JavaScriptResourceReference uiWidgetAutocomplete) {
        this.uiCoreCore = uiCoreCore;
        this.uiCoreWidget = uiCoreWidget;
        this.uiCoreMouse = uiCoreMouse;
        this.uiCorePosition = uiCorePosition;
        this.uiWidgetMenu = uiWidgetMenu;
        this.uiWidgetAutocomplete = uiWidgetAutocomplete;
    }


}
