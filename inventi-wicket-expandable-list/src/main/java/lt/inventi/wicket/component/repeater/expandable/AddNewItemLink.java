package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.event.Broadcast;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.request.cycle.RequestCycle;

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
    protected abstract Object onAddNewItem();

    @Override
    public final void onClick(AjaxRequestTarget target) {
        Object item = onAddNewItem();
        if (item != null) {
            NewItemAddedEvent event = new NewItemAddedEvent(item);
            send(getPage(), Broadcast.BREADTH, event);
        }
    }

    /**
     * Callback method for ExpandableView, which is invoked after model was updated
     */
    static void generateResponse(ExpandableView<?> refreshingView, Item<?> item) {

        AjaxRequestTarget target = RequestCycle.get().find(AjaxRequestTarget.class);
        item.setOutputMarkupId(true);
        target.prependJavaScript(generateAddElementScript(refreshingView, item));
        target.add(item);
        Component formComponent = Repeaters.getFirstFormComponent(item);
        if (formComponent != null) {
            formComponent.setOutputMarkupId(true);
            target.focusComponent(formComponent);
        }
    }

    private static String generateAddElementScript(RefreshingView<?> refreshingView, Component itemComponent) {
        String javascript = String.format("var item=document.createElement('span');item.id='%s';" + "$('#%s').append(item);",
                itemComponent.getMarkupId(),
                refreshingView.getParent().getMarkupId());
        return javascript;
    }
}
