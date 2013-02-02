package lt.inventi.wicket.component.repeater.expandable;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.repeater.Item;

/**
 * Automatically removes item's model from the {@link ExpandableView} model.
 *
 * @author vplatonov
 *
 * @param <T>
 */
public class AutoRemoveItemLink<T> extends RemoveItemLink<T> {

    public AutoRemoveItemLink(String id, Item<T> item) {
        super(id, item);
    }

    @Override
    protected final void onRemoveItem(AjaxRequestTarget target) {
        if (item().getParent() instanceof ExpandableView) {
            Object modelObject = item().getParent().getDefaultModelObject();
            if (modelObject instanceof Collection) {
                ((Collection<?>) modelObject).remove(item().getDefaultModelObject());
            }
        }
    }

    protected void onAfterRemoveItem(AjaxRequestTarget target) {
        // do nothing
    }
}
