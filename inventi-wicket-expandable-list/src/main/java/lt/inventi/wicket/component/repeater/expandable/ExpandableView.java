package lt.inventi.wicket.component.repeater.expandable;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.IGenericComponent;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RefreshingView;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.model.ChainingModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Generics;

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
public abstract class ExpandableView<T> extends RefreshingView<T> implements IGenericComponent<List<T>> {

    private int childIdCounter = 0;

    public ExpandableView(String id) {
        super(id);
    }

    public ExpandableView(String id, IModel<? extends List<T>> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        setItemReuseStrategy(ExpandableReuseIfModelsEqualStrategy.instance);
        super.onInitialize();
    }

    @Override
    protected Iterator<IModel<T>> getItemModels() {
        return modelIterator(getModel());
    }

    @Override
    public MarkupContainer remove(Component component) {
        if (component instanceof Item) {
            @SuppressWarnings("unchecked")
            Item<T> item = (Item<T>) component;

            Iterator<Item<T>> allItems = getItems();
            while (allItems.hasNext()) {
                Item<T> nextItem = allItems.next();
                getTrackingModel(nextItem.getModel()).onRemove(item.getIndex());
            }
        }

        return super.remove(component);
    }

    @Override
    public String newChildId() {
        String id = String.valueOf(childIdCounter);
        childIdCounter++;
        return id;
    }

    @Override
    @SuppressWarnings("unchecked")
    public IModel<List<T>> getModel() {
        return (IModel<List<T>>) getDefaultModel();
    }

    @Override
    public void setModel(IModel<List<T>> model) {
        setDefaultModel(model);
    }

    @Override
    public void setModelObject(List<T> object) {
        setModelObject(object);
    }

    @Override
    public List<T> getModelObject() {
        return getModel().getObject();
    }

    @Override
    protected Item<T> newItem(String id, int index, IModel<T> model) {
        Item<T> newItem = super.newItem(id, index, model);
        newItem.setOutputMarkupId(true);
        return newItem;
    }

    @SuppressWarnings("unchecked")
    // internal use from AddNewItemLink
    Item<T> appendAndGetNewItem(T newItem) {
        getModel().getObject().add(newItem);
        onPopulate();
        return (Item<T>) get(getModel().getObject().size() - 1);
    }

    private Iterator<IModel<T>> modelIterator(IModel<? extends List<T>> model) {
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
        return new ListModelIterator();
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
            return model;
        }

        @Override
        public void remove() {
            unsupportedRemoval();
        }

        @SuppressWarnings("unchecked")
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
        public int hashCode() {
            return index;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof IModel)) {
                return false;
            }
            try {
                @SuppressWarnings("unchecked")
                IndexTrackingModel<T> model = getTrackingModel((IModel<T>) obj);
                return model.index == this.index;
            } catch (IllegalStateException e) {
                return false;
            }
        }
    }

    /**
     * Adapted for expandable view.
     *
     * @see ReuseIfModelsEqualStrategy
     */
    private static class ExpandableReuseIfModelsEqualStrategy implements IItemReuseStrategy {
        private static IItemReuseStrategy instance = new ExpandableReuseIfModelsEqualStrategy();

        @Override
        public <T> Iterator<Item<T>> getItems(final IItemFactory<T> factory, final Iterator<IModel<T>> newModels,
            Iterator<Item<T>> existingItems) {
            final Map<IModel<T>, Item<T>> modelToItem = Generics.newHashMap();
            while (existingItems.hasNext()) {
                final Item<T> item = existingItems.next();
                modelToItem.put(getTrackingModel(item.getModel()), item);
            }

            return new Iterator<Item<T>>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return newModels.hasNext();
                }

                @Override
                public Item<T> next() {
                    final IModel<T> model = newModels.next();
                    final Item<T> oldItem = modelToItem.get(model);

                    final Item<T> item;
                    if (oldItem == null) {
                        item = factory.newItem(index, model);
                    } else {
                        oldItem.setIndex(index);
                        item = oldItem;
                    }
                    index++;

                    return item;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

    }

    private static void unsupportedRemoval() {
        throw new UnsupportedOperationException("Cannot remove items from ExpandableView!");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static <T> IndexTrackingModel<T> getTrackingModel(IModel<T> model) {
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

}
