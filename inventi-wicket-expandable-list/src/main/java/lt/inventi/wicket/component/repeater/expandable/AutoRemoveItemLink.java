package lt.inventi.wicket.component.repeater.expandable;

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
            ExpandableView<?> view = (ExpandableView<?>) item().getParent();
            view.getModelObject().remove(item().getDefaultModelObject());
            onAfterRemoveItem(target);
        }
    }

    protected void onAfterRemoveItem(AjaxRequestTarget target) {
        // do nothing
    }
}
