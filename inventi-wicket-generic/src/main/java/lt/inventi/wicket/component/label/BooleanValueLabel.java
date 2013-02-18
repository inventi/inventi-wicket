package lt.inventi.wicket.component.label;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.convert.converter.BooleanConverter;

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
