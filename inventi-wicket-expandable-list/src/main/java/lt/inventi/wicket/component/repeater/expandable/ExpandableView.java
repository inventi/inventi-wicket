package lt.inventi.wicket.component.repeater.expandable;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

/**
 * A refreshing view which allows adding/removing rows on the fly.
 * <p>
 * If the model is bound to a non-random access collection (e.g. Set,
 * Collection), the view will not replace model objects inside of the collection
 * during the model update.
 * <p>
 * Ids are counted from zero (not from 1) and maintains same IDs whenever the
 * view is refreshed.
 * <p>
 * If you use add/remove links, make sure you have this view nested to parent
 * container which should include new rows. For example in case if its table,
 * then this view (which would be bound to <code>tr</code>) must be nested in
 * container, which is <code>tbody</code>.
 *
 */
public abstract class ExpandableView<T> extends RefreshingView<T> {

    private int childIdCounter = 0;

    @SuppressWarnings("unchecked")
    protected IModel<? extends Iterable<T>> getModel() {
        return (IModel<List<T>>) getDefaultModel();
    }

    public ExpandableView(String id) {
        super(id);
    }

    public ExpandableView(String id, IModel<? extends Iterable<T>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setItemReuseStrategy(new ReuseExistingItemsStrategy());
        super.onInitialize();
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {
        return modelIterator(getModel());
    }

    @Override
    public String newChildId() {
        String id = String.valueOf(childIdCounter);
        childIdCounter++;
        return id;
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

    private static <T> Iterator<IModel<T>> modelIterator(IModel<? extends Iterable<T>> model) {
        if (model == null) {
            return new Iterator<IModel<T>>() {
                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public IModel<T> next() {
                    throw new IllegalStateException("No element!");
                }

                @Override
                public void remove() {
                    unsupportedRemoval();
                }
            };
        }
        if (model.getObject() instanceof List) {
            return new ListModelIterator<T>((List<T>) model.getObject());
        }
        return new IterableModelIterator<T>(model.getObject());
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
            if (item != null) {
                event.stop();
                AddNewItemLink.generateResponse(this, item);
            }
        }
    }

    private static class ListModelIterator<T> implements Iterator<IModel<T>> {
        private transient List<T> list;
        private int index = 0;

        ListModelIterator(List<T> list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return index < list.size();
        }

        @Override
        public IModel<T> next() {
            final int currentIndex = index;
            index++;
            return new CompoundPropertyModel<T>(new IModel<T>() {
                @Override
                public void detach() {
                    // do nothing
                }

                @Override
                public T getObject() {
                    return list.get(currentIndex);
                }

                @Override
                public void setObject(T object) {
                    list.set(currentIndex, object);
                }
            });
        }

        @Override
        public void remove() {
            unsupportedRemoval();
        }
    }

    private static class IterableModelIterator<T> implements Iterator<IModel<T>> {
        private transient Iterator<T> iterator;

        public IterableModelIterator(Iterable<T> object) {
            this.iterator = object.iterator();
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
            unsupportedRemoval();
        }
    }

    private static void unsupportedRemoval() {
        throw new UnsupportedOperationException("Cannot remove items from ExpandableView!");
    }

}
