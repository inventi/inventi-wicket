package lt.inventi.wicket.component.repeater.expandable;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.repeater.Item;

/**
 * Intended to be used together with ExpandableView.
 * Removes item via ajax.
 */
public abstract class RemoveItemLink<T> extends AjaxLink<T> {

    private final Item<T> item;

    public RemoveItemLink(String id, Item<T> item) {
        super(id);
        this.item = item;
    }

    @Override
    public void onClick(AjaxRequestTarget target) {
        String markupId = item.getMarkupId();
        Item<?> sibling = getSiblingItem(item);
        onRemoveItem(target);
        target.prependJavaScript("$('#"+markupId+"').remove();");
        item.remove();
        if (sibling != null) {
            Component cmp = Repeaters.getFirstFormComponent(sibling);
            if (cmp != null) {
                cmp.setOutputMarkupId(true);
                target.focusComponent(cmp);
            }
        }
    }

    /**
     * In this method remove object from the model
     * Also use this method to notify listeners or to add extra components to ajax request if needed.
     */
    protected abstract void onRemoveItem(AjaxRequestTarget target);

    /**
     * @return the item to be removed by this link
     */
    protected Item<T> item() {
        return item;
    }

    @SuppressWarnings("unchecked")
    private static Item<?> getSiblingItem(Item<?> item) {
        if (item.getParent() instanceof ExpandableView) {
            ExpandableView<Object> listView = (ExpandableView<Object>) item.getParent();

            if (listView.size() <= 1) {
                return null;
            }
            Iterator<Item<Object>> items = listView.getItems();
            Item<Object> prev = null;
            while (items.hasNext()) {
                Item<Object> next = items.next();
                if (next.getIndex() == item.getIndex()) {
                    if (items.hasNext()) {
                        return items.next();
                    }
                } else {
                    prev = next;
                }
            }
            return prev;
        }
        return null;
    }
}
