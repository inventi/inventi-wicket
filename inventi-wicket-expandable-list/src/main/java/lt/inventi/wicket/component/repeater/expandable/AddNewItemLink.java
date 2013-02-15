package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.repeater.Item;

/**
 * Intended to be used together with ExpandableView. Fires special NewItem event once clicked, which
 * triggers ExpandableView to generate special ajax response and update view.
 */
public abstract class AddNewItemLink<T> extends AjaxLink<Void> {

    private ExpandableView<T> relatedView;

    public AddNewItemLink(String id) {
        super(id);
    }

    public AddNewItemLink(String id, ExpandableView<T> relatedView) {
        super(id);
        this.relatedView = relatedView;
    }

    public AddNewItemLink<T> setRelatedView(ExpandableView<T> relatedView) {
        this.relatedView = relatedView;
        return this;
    }

    /**
     * Return a new object to be placed in the backing collection.
     * <p>
     * Also use this method to notify listeners or to add extra components to
     * the ajax request if needed.
     *
     * @return
     */
    protected abstract T createNewItem(AjaxRequestTarget target);

    /**
     * This method is called after the newly created item has been added to the
     * backing collection and the {@link ExpandableView} has been repopulated.
     * <p>
     * Also use this method to notify listeners or to add extra components to
     * the ajax request if needed.
     */
    protected void onNewItemAdded(Item<T> newItem, AjaxRequestTarget target) {
        // do nothing
    }

    @Override
    public final void onClick(AjaxRequestTarget target) {
        T item = createNewItem(target);
        Item<T> newItem = relatedView.appendAndGetNewItem(item);
        onNewItemAdded(newItem, target);
        generateResponse(target, newItem);
    }

    private void generateResponse(AjaxRequestTarget target, Item<?> item) {
        item.setOutputMarkupId(true);
        target.prependJavaScript(generateAddElementScript(item));
        target.add(item);
        Component formComponent = Repeaters.getFirstFormComponent(item);
        if (formComponent != null) {
            formComponent.setOutputMarkupId(true);
            target.focusComponent(formComponent);
        }
    }

    private String generateAddElementScript(Component itemComponent) {
        return String.format("var item=document.createElement('span');item.id='%s';$('#%s').append(item);",
            itemComponent.getMarkupId(), relatedView.getParent().getMarkupId());
    }

}
