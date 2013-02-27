package lt.inventi.wicket.component.label;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.converter.BooleanConverter;

/**
 * A label with a boolean model which is localized using the following keys:
 * <ul>
 * <li>When <b>null</b>: BooleanValueLabel.UNDEFINED</li>
 * <li>When <b>true</b>: BooleanValueLabel.TRUE</li>
 * <li>When <b>false</b>: BooleanValueLabel.FALSE</li>
 * </ul>
 * <p>
 * If no values are provided for the keys, default boolean localization rules
 * apply.
 *
 * @author vplatonov
 *
 */
public class BooleanValueLabel extends Label {

    public BooleanValueLabel(String id) {
        this(id, null);
    }

    public BooleanValueLabel(String id, IModel<Boolean> model) {
        super(id, model);
    }

    @Override
    public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
        replaceComponentTagBody(markupStream, openTag, getBooleanValue());
    }

    private CharSequence getBooleanValue() {
        // we cannot provide a special boolean converter as it will never get called if the model is null
        Boolean value = (Boolean) getDefaultModelObject();
        String defaultValue = BooleanConverter.INSTANCE.convertToString(value, getLocale());
        if (value == null) {
            return getString("BooleanValueLabel.UNDEFINED", null, defaultValue);
        }
        if (value) {
            return getString("BooleanValueLabel.TRUE", null, defaultValue);
        }
        return getString("BooleanValueLabel.FALSE", null, defaultValue);
    }
}
