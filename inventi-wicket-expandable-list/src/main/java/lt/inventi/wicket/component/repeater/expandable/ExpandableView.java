package lt.inventi.wicket.component.repeater.expandable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.model.ChainingModel;
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
    private List<IndexTrackingModel<T>> models = new ArrayList<IndexTrackingModel<T>>();

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
        models.clear();
        return modelIterator(getModel());
    }

    @Override
    public MarkupContainer remove(Component component) {
        if (component instanceof Item) {
            Item<T> item = (Item<T>) component;
            IndexTrackingModel<T> model = getTrackingModel(item.getModel());
            int idx = models.indexOf(model);
            if (idx < 0) {
                throw new IllegalStateException("Model doesn't exist!");
            }
            models.remove(model);
            for (IndexTrackingModel<T> m : models) {
                m.onRemove(idx);
            }
        }

        return super.remove(component);
    }

    private IndexTrackingModel<T> getTrackingModel(IModel<T> model) {
        IModel<T> current = model;
        while (!(current instanceof IndexTrackingModel)) {
            if (current instanceof ChainingModel) {
                current = ((ChainingModel) current).getChainedModel();
            } else {
                throw new IllegalStateException("Please do not unwrap ExpandableView models!");
            }
        }
        return (IndexTrackingModel<T>) current;
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

    private Iterator<IModel<T>> modelIterator(IModel<? extends Iterable<T>> model) {
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
            return new ListModelIterator();
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

    private class ListModelIterator implements Iterator<IModel<T>>, Serializable {
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < getList().size();
        }

        @Override
        public IModel<T> next() {
            IndexTrackingModel<T> model = new IndexTrackingModel<T>(index) {
                @Override
                public T getObject() {
                    return getList().get(getIndex());
                }

                @Override
                public void setObject(T object) {
                    getList().set(getIndex(), object);
                }
            };
            index++;
            models.add(model);
            return new CompoundPropertyModel<T>(model);
        }

        @Override
        public void remove() {
            unsupportedRemoval();
        }

        private List<T> getList() {
            return (List<T>) ExpandableView.this.getDefaultModelObject();
        }

    }

    private static abstract class IndexTrackingModel<T> implements IModel<T> {
        private int index;

        public IndexTrackingModel(int index) {
            this.index = index;
        }

        @Override
        public void detach() {
            // nothing
        }

        public int getIndex() {
            return index;
        }

        public void onRemove(int removedIndex) {
            if (removedIndex < index) {
                index--;
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof IndexTrackingModel) {
                return ((IndexTrackingModel<T>) obj).index == index;
            }
            return false;
        }
    }

    private static class IterableModelIterator<T> implements Iterator<IModel<T>>, Serializable {
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
