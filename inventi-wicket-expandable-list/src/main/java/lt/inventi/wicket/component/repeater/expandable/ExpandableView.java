package lt.inventi.wicket.component.repeater.expandable;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * This view adds following extra features: Counts ID from zero, not from 1. Maintains same IDs
 * whenever view is refreshed. Provides links to add/remove extra rows.
 * If you use add/remove links, make sure you have this view nested to parent container which should include new rows.
 * For example in case if its table, then this view must be nested in container, which is tbody.
 * 
 */
public abstract class ExpandableView<T> extends RefreshingView<T> {

    private int childIdCounter = 0;

    @SuppressWarnings("unchecked")
    protected IModel<List<T>> getModel() {
        return (IModel<List<T>>) getDefaultModel();
    }

    public ExpandableView(String id) {
        super(id);
    }

    public ExpandableView(String id, IModel<List<T>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setItemReuseStrategy(new ReuseExistingItemsStrategy());
        super.onInitialize();
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {
        return new ModelIterator(getModel());
    }

    @Override
    public String newChildId() {
        String id = String.valueOf(childIdCounter);
        childIdCounter++;
        return id;
    }

    private class ModelIterator implements Iterator<IModel<T>> {

        private transient Iterator<T> iterator;

        public ModelIterator(IModel<? extends Iterable<T>> model) {
            if (model != null) {
                iterator = model.getObject().iterator();
            } else {
                iterator = new Iterator<T>() {
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException("Cannot remove from empty iterator!");
                    }

                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public T next() {
                        throw new NoSuchElementException();
                    }
                };
            }
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public IModel<T> next() {
            return new CompoundPropertyModel<T>(iterator.next());
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }

    private static <T> Item<?> findComponent(RefreshingView<T> refreshingView, Object modelObject) {
        Iterator<Item<T>> items = refreshingView.getItems();
        while (items.hasNext()) {
            Item<T> item = items.next();
            if (modelObject.equals(item.getDefaultModelObject())) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void onEvent(IEvent<?> event) {

        if (event.getPayload() instanceof NewItemAddedEvent) {
            Object newItem = ((NewItemAddedEvent) event.getPayload()).getNewItem();
            // update model and make a callback to the new item link.
            this.onPopulate();
            Item<?> item = findComponent(this, newItem);
            //item can be null if its not our event
            //in that case pass it through
            if(item != null){
                event.stop();
                AddNewItemLink.generateResponse(this, item);
            }
        }
    }
}
