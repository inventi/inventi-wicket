package lt.inventi.wicket.component.bootstrap;

import org.apache.wicket.model.IModel;

public class ModalConfig {

    public enum Backdrop {
        TRUE, FALSE, STATIC;
    }

    private Backdrop backdrop = Backdrop.TRUE;
    private IModel<Boolean> isShown;

    public ModalConfig() {
        super();
    }

    public ModalConfig(IModel<Boolean> visibilityModel) {
        this.isShown = visibilityModel;
    }

    public ModalConfig withBackdrop(Backdrop value) {
        this.backdrop = value;
        return this;
    }

    public ModalConfig shownWhen(IModel<Boolean> value) {
        this.isShown = value;
        return this;
    }

    // default scope
    Backdrop backdrop() {
        return backdrop;
    }

    boolean isShown() {
        return isShown != null && isShown.getObject();
    }

    boolean hasVisibilityModel() {
        return isShown != null;
    }

    IModel<Boolean> visibilityModel() {
        return isShown;
    }
}
