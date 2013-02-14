package lt.inventi.wicket.component.bootstrap;

import java.io.Serializable;

import org.apache.wicket.model.IModel;

public class ModalBehaviorConfig implements Serializable {

    public enum Backdrop {
        TRUE, FALSE, STATIC;
    }

    private Backdrop backdrop = Backdrop.TRUE;
    private IModel<Boolean> isShown;

    public ModalBehaviorConfig() {
        super();
    }

    public ModalBehaviorConfig(IModel<Boolean> visibilityModel) {
        this.isShown = visibilityModel;
    }

    public ModalBehaviorConfig withBackdrop(Backdrop value) {
        this.backdrop = value;
        return this;
    }

    public ModalBehaviorConfig shownWhen(IModel<Boolean> value) {
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
