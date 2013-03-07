package lt.inventi.wicket.component.bootstrap;

import java.io.Serializable;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class ModalBehaviorConfig implements Serializable {

    public enum Backdrop {
        TRUE, FALSE, STATIC;
    }

    private Backdrop backdrop = Backdrop.TRUE;
    private IModel<Boolean> isShown = Model.of(false);

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

    void onClose() {
        isShown.setObject(false);
    }

    void onShow() {
        isShown.setObject(true);
    }
}
