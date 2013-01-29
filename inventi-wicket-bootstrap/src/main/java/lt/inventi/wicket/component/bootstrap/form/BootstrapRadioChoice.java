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

        int inputIdx = buffer.lastIndexOf("<input");
        if (inputIdx == -1) {
            throw new IllegalStateException("Must contain an input!");
        }
        int labelIdx = buffer.indexOf("<label", inputIdx);
        if (labelIdx == -1) {
            throw new IllegalStateException("Must contain a label!");
        }
        int labelEndIdx = buffer.indexOf("</label>", labelIdx);
        if (labelEndIdx == -1) {
            throw new IllegalStateException("Must contain label's end!");
        }

        String label = buffer.substring(labelIdx, labelEndIdx);
        String labelClass = "class=\"radio" + (type == Type.DEFAULT ? "" : " inline") + "\" ";
        String labelWithClass = label.substring(0, 7) + labelClass + label.substring(7, label.length());
        String input = buffer.substring(inputIdx, labelIdx);

        buffer.replace(inputIdx, labelEndIdx, labelWithClass + input);
    }
}
