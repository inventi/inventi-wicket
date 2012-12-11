package lt.inventi.wicket.component.label;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.EnclosureContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.model.IModel;

/**
 * Label which isn't visible if empty (useful together with
 * {@link EnclosureContainer}).
 * 
 * @author vplatonov
 * 
 */
public class EmptyMultiLineTextLabel extends MultiLineLabel {

    public EmptyMultiLineTextLabel(String id, IModel<?> model) {
        super(id, model);
    }

    public EmptyMultiLineTextLabel(String id, String label) {
        super(id, label);
    }

    public EmptyMultiLineTextLabel(String id) {
        super(id);
    }

    @Override
    public boolean isVisible() {
        return !StringUtils.isEmpty(getDefaultModelObjectAsString());
    }
}
