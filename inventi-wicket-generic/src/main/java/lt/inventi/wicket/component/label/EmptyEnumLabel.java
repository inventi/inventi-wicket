package lt.inventi.wicket.component.label;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.model.IModel;

public class EmptyEnumLabel<T extends Enum<T>> extends EnumLabel<T> {

    public EmptyEnumLabel(String id, IModel<T> model) {
        super(id, model);
    }

    public EmptyEnumLabel(String id, T value) {
        super(id, value);
    }

    public EmptyEnumLabel(String id) {
        super(id);
    }

    @Override
    public boolean isVisible() {
        return !StringUtils.isEmpty(getDefaultModelObjectAsString());
    }
}
