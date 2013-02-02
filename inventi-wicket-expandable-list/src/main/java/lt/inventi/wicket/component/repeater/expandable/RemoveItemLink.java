package lt.inventi.wicket.component.repeater.expandable;

import java.util.Iterator;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;

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
        Item<?> sibling = getSiblingObject(item);
        onRemoveItem(target);
        target.prependJavaScript("$('#"+markupId+"').remove();");
        item.remove();
        if(sibling != null){
            Component cmp = Repeaters.getFirstFormComponent(sibling);
            if(cmp != null){
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
    private static Item<?> getSiblingObject(Item<?> item) {
        Object siblingObject = null;
        if(item.getParent() instanceof ExpandableView){
            ExpandableView<?> listView = (ExpandableView<?>)item.getParent();
            IModel<Iterable<?>> model =  (IModel<Iterable<?>>)listView.getDefaultModel();

            Iterator<?> modelObjects = model.getObject().iterator();
            while(modelObjects.hasNext()){
                Object tmpObject = modelObjects.next();
                if(tmpObject.equals(item.getModelObject())){
                    if(modelObjects.hasNext()){
                        siblingObject = modelObjects.next();
                    }
                    break;
                }
                siblingObject = tmpObject;
            }

            if(siblingObject != null){
                Iterator<? extends Item<?>> items = listView.getItems();
                while(items.hasNext()){
                    Item<?> listItem = items.next();
                    if(siblingObject.equals(listItem.getDefaultModelObject())){
                        return listItem;
                    }
                }
            }
        }
        return null;
    }
}
