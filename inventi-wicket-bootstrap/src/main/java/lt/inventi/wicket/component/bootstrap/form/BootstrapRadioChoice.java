package lt.inventi.wicket.component.bootstrap.form;

import java.util.List;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

public class BootstrapRadioChoice<T> extends RadioChoice<T> {

    public enum Type {
        DEFAULT, INLINE
    }

    private Type type = Type.DEFAULT;

    public BootstrapRadioChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        init();
    }

    public BootstrapRadioChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
        init();
    }

    public BootstrapRadioChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        init();
    }

    public BootstrapRadioChoice(String id, IModel<T> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
        init();
    }

    public BootstrapRadioChoice(String id, IModel<T> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        init();
    }

    public BootstrapRadioChoice(String id, IModel<T> model, List<? extends T> choices) {
        super(id, model, choices);
        init();
    }

    public BootstrapRadioChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        init();
    }

    public BootstrapRadioChoice(String id, List<? extends T> choices) {
        super(id, choices);
        init();
    }

    public BootstrapRadioChoice(String id) {
        super(id);
        init();
    }

    private void init() {
        setSuffix("\n");
    }

    public final BootstrapRadioChoice<T> setType(Type type) {
        this.type = type;
        return this;
    }

    @Override
    protected void appendOptionHtml(AppendingStringBuffer buffer, T choice, int index, String selected) {
        super.appendOptionHtml(buffer, choice, index, selected);
        String cssClass = "radio" + (type == Type.DEFAULT ? "" : " inline");
        ChoiceUtils.moveInputInsideLabel(buffer, cssClass, ChoiceUtils.InputPosition.AFTER_LABEL);
    }
}
