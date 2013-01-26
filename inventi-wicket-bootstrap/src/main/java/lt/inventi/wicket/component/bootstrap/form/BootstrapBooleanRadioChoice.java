package lt.inventi.wicket.component.bootstrap.form;

import java.util.Arrays;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * Radio choice which creates two values for a boolean-backed model.
 * <p>
 * This control serves as an alternative to checkboxes, especially in
 * bootstrap's horizontal forms. See <a href=
 * "http://ux.stackexchange.com/questions/22532/laying-out-checkboxes-in-forms"
 * >this UX Stackexchange question</a> for more information and supporting
 * ideas.
 * <p>
 * You can localize the labels for true/false options by providing localization
 * properties for:
 * <ol>
 * <li>BooleanRadioChoice.YES</li>
 * <li>BooleanRadioChoice.NO</li>
 * </ol>
 * 
 * @author vplatonov
 * 
 */
public class BootstrapBooleanRadioChoice extends BootstrapRadioChoice<Boolean> {

    public BootstrapBooleanRadioChoice(String id) {
        super(id, Arrays.asList(Boolean.TRUE, Boolean.FALSE), new BooleanRadioChoiceRenderer());
    }

    public BootstrapBooleanRadioChoice(String id, IModel<Boolean> model) {
        super(id, model, Arrays.asList(Boolean.TRUE, Boolean.FALSE), new BooleanRadioChoiceRenderer());
    }

    private static class BooleanRadioChoiceRenderer implements IChoiceRenderer<Boolean> {
        @Override
        public Object getDisplayValue(Boolean object) {
            return Boolean.TRUE.equals(object) ? "BooleanRadioChoice.YES" : "BooleanRadioChoice.NO";
        }

        @Override
        public String getIdValue(Boolean object, int index) {
            return String.valueOf(object);
        }
    }

    @Override
    protected boolean localizeDisplayValues() {
        return true;
    }
}
