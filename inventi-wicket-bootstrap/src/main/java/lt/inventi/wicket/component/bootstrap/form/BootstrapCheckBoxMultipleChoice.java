package lt.inventi.wicket.component.bootstrap.form;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.string.AppendingStringBuffer;

public class BootstrapCheckBoxMultipleChoice<T> extends CheckBoxMultipleChoice<T> {

    enum CheckboxType {
        DEFAULT, INLINE
    }

    private CheckboxType type = CheckboxType.DEFAULT;

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends Collection<T>> model, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends Collection<T>> model, IModel<? extends List<? extends T>> choices) {
        super(id, model, choices);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends Collection<T>> model, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, model, choices, renderer);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends Collection<T>> model, List<? extends T> choices) {
        super(id, model, choices);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends List<? extends T>> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, IModel<? extends List<? extends T>> choices) {
        super(id, choices);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, List<? extends T> choices, IChoiceRenderer<? super T> renderer) {
        super(id, choices, renderer);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id, List<? extends T> choices) {
        super(id, choices);
        init();
    }

    public BootstrapCheckBoxMultipleChoice(String id) {
        super(id);
        init();
    }

    private void init() {
        setSuffix("\n");
    }

    public BootstrapCheckBoxMultipleChoice<T> setType(CheckboxType type) {
        this.type = type;
        return this;
    }

    @Override
    protected void appendOptionHtml(AppendingStringBuffer buffer, T choice, int index, String selected) {
        super.appendOptionHtml(buffer, choice, index, selected);
        String cssClass = "checkbox" + (type == CheckboxType.DEFAULT ? "" : " inline");
        ChoiceUtils.moveInputInsideLabel(buffer, cssClass);
    }
}
