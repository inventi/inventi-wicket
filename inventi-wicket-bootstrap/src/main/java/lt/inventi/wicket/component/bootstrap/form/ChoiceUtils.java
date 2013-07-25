package lt.inventi.wicket.component.bootstrap.form;

import org.apache.wicket.util.string.AppendingStringBuffer;

abstract class ChoiceUtils {

    public enum InputPosition {
        BEFORE_LABEL, AFTER_LABEL
    }

    private ChoiceUtils() {
        // static utils
    }

    static void moveInputInsideLabel(AppendingStringBuffer buffer, String cssClass, InputPosition position) {
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
        String labelClass = "class=\"" + cssClass + "\" ";
        String labelWithClass = label.substring(0, 7) + labelClass + label.substring(7, label.length());
        String input = buffer.substring(inputIdx, labelIdx);

        buffer.replace(inputIdx, labelEndIdx, position == InputPosition.BEFORE_LABEL ? "<div>" + input + labelWithClass + "</div>" : labelWithClass + input);
    }
}
