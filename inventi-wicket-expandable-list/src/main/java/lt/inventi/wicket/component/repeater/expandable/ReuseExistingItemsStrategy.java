package lt.inventi.wicket.component.repeater.expandable;

import java.util.Iterator;
import java.util.Map;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.lang.Generics;

/**
 * This strategy reuses existing items, instead of just creating new ones on every request.
 */
public class ReuseExistingItemsStrategy implements IItemReuseStrategy {

    private static final long serialVersionUID = -7916698829634080493L;

    @Override
    public <T> Iterator<Item<T>> getItems(IItemFactory<T> factory,
            Iterator<IModel<T>> newModels, Iterator<Item<T>> existingItems) {

        Map<T, Item<T>> existingItemsMap = Generics.newHashMap();
        while (existingItems.hasNext()) {
            Item<T> item = existingItems.next();
            existingItemsMap.put(item.getModelObject(), item);
        }

        return new ReusingIterator<T>(factory, newModels, existingItemsMap);
    }

    private static class ReusingIterator<T> implements Iterator<Item<T>> {

        private int index = 0;
        private IItemFactory<T> factory;
        private Iterator<IModel<T>> newModels;
        private Map<T, Item<T>> existingItemsMap;

        public ReusingIterator(IItemFactory<T> factory,
                Iterator<IModel<T>> newModels,  Map<T, Item<T>> existingItemsMap){
            this.factory = factory;
            this.newModels = newModels;
            this.existingItemsMap = existingItemsMap;
        }

        @Override
        public boolean hasNext() {
            return newModels.hasNext();
        }

        @Override
        public Item<T> next() {
            IModel<T> model = newModels.next();
            Item<T> existingItem = existingItemsMap.get(model.getObject());
            Item<T> item;

            if (existingItem == null) {
                item = factory.newItem(index, model);
            } else {
                existingItem.setIndex(index);
                item = existingItem;
            }

            index++;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
