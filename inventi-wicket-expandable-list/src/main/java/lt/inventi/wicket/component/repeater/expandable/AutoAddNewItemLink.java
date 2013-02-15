package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.ajax.AjaxRequestTarget;

public class AutoAddNewItemLink<T> extends AddNewItemLink<T> {

    private final NewItemFactory<T> factory;

    public AutoAddNewItemLink(String id) {
        this(id, null, null);
    }

    public AutoAddNewItemLink(String id, NewItemFactory<T> factory) {
        this(id, factory, null);
    }

    public AutoAddNewItemLink(String id, ExpandableView<T> view) {
        this(id, null, view);
    }

    public AutoAddNewItemLink(String id, NewItemFactory<T> factory, ExpandableView<T> view) {
        super(id, view);
        this.factory = factory;
    }

    @Override
    protected T createNewItem(AjaxRequestTarget target) {
        return createNewItem();
    }

    protected T createNewItem() {
        if (factory == null) {
            return null;
        }
        return factory.createNewItem();
    }

}
