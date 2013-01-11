package lt.inventi.wicket.js;

import static lt.inventi.wicket.js.EmptyResourceReference.get;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

public class BootstrapJsSettings {

    public final JavaScriptResourceReference bsTransitions;
    public final JavaScriptResourceReference bsModal;
    public final JavaScriptResourceReference bsDropdown;
    public final JavaScriptResourceReference bsScrollspy;
    public final JavaScriptResourceReference bsTab;
    public final JavaScriptResourceReference bsTooltip;
    public final JavaScriptResourceReference bsPopover;
    public final JavaScriptResourceReference bsAlert;
    public final JavaScriptResourceReference bsButton;
    public final JavaScriptResourceReference bsCollapse;
    public final JavaScriptResourceReference bsCarousel;
    public final JavaScriptResourceReference bsTypeahead;
    public final JavaScriptResourceReference bsAffix;

    public BootstrapJsSettings() {
        this(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get());
    }

    public BootstrapJsSettings(JavaScriptResourceReference bsTransitions, JavaScriptResourceReference bsModal, JavaScriptResourceReference bsDropdown, JavaScriptResourceReference bsScrollspy, JavaScriptResourceReference bsTab, JavaScriptResourceReference bsTooltip, JavaScriptResourceReference bsPopover, JavaScriptResourceReference bsAlert, JavaScriptResourceReference bsButton, JavaScriptResourceReference bsCollapse, JavaScriptResourceReference bsCarousel, JavaScriptResourceReference bsTypeahead, JavaScriptResourceReference bsAffix) {
        this.bsTransitions = bsTransitions;
        this.bsModal = bsModal;
        this.bsDropdown = bsDropdown;
        this.bsScrollspy = bsScrollspy;
        this.bsTab = bsTab;
        this.bsTooltip = bsTooltip;
        this.bsPopover = bsPopover;
        this.bsAlert = bsAlert;
        this.bsButton = bsButton;
        this.bsCollapse = bsCollapse;
        this.bsCarousel = bsCarousel;
        this.bsTypeahead = bsTypeahead;
        this.bsAffix = bsAffix;
    }
}
