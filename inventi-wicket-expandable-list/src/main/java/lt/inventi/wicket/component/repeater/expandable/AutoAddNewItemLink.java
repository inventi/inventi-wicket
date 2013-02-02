package lt.inventi.wicket.component.repeater.expandable;

import java.util.Collection;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.IModel;

public class AutoAddNewItemLink<T> extends AddNewItemLink {

    private final NewItemFactory<T> factory;

    public AutoAddNewItemLink(String id, NewItemFactory<T> factory) {
        this(id, null, factory);
    }

    public AutoAddNewItemLink(String id, IModel<? extends Collection<T>> model) {
        this(id, model, null);
    }

    public AutoAddNewItemLink(String id, IModel<? extends Collection<T>> model, NewItemFactory<T> factory) {
        super(id);
        setDefaultModel(model);
        this.factory = factory;
    }

    @Override
    protected final T onAddNewItem(AjaxRequestTarget target) {
        Object modelObject = getDefaultModelObject();
        if (!(modelObject instanceof Collection)) {
            throw new IllegalStateException("Model is not a collection, but <" + modelObject + ">!");
        }
        @SuppressWarnings("unchecked")
        Collection<T> coll = (Collection<T>) modelObject;
        T newItem = createNewItem();
        coll.add(newItem);
        onNewItemAddedToModel(newItem, target);
        return newItem;
    }

    protected T createNewItem() {
        if (factory == null) {
            throw new UnsupportedOperationException("Impossible to create a new item! " +
                "Either provide a NewItemFactory or override the AutoAddNewItemLink#createNewItem!");
        }
        return factory.createNewItem();
    }

    protected void onNewItemAddedToModel(T item, AjaxRequestTarget target) {
        // do nothing
    }

}
