package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;

/**
 * Intended to be used together with ExpandableView. Fires special NewItem event once clicked, which
 * triggers ExpandableView to generate special ajax response and update view.
 */
public abstract class AddNewItemLink extends AjaxLink<Void> {


    public AddNewItemLink(String id) {
        super(id);
    }

    /**
     * In this method update your model and return newly created object. Also use this method to
     * notify listeners or to add extra components to ajax request if needed.
     *
     * @return
     */
    protected abstract Object onAddNewItem(AjaxRequestTarget target);

    @Override
    public final void onClick(AjaxRequestTarget target) {
        Object item = onAddNewItem(target);
        if (item != null) {
            NewItemAddedEvent event = new NewItemAddedEvent(item);
            send(getPage(), Broadcast.BREADTH, event);
        }
    }

}
