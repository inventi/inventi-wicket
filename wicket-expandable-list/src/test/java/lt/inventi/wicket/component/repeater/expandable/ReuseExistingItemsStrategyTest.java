package lt.inventi.wicket.component.repeater.expandable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lt.inventi.wicket.test.BaseNonInjectedTest;

import org.apache.wicket.markup.repeater.IItemFactory;
import org.apache.wicket.markup.repeater.IItemReuseStrategy;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;

public class ReuseExistingItemsStrategyTest extends BaseNonInjectedTest {

    @Test
    public void testReuse(){
        IItemReuseStrategy strategy = new ReuseExistingItemsStrategy();

        IItemFactory<String> factory = mock(IItemFactory.class);

        Item newItem = new Item<String>("0", 0, new Model("newString"));
        when(factory.newItem(anyInt(), any(IModel.class))).thenReturn(newItem);

        List<IModel<String>> newModels = new ArrayList<IModel<String>>();
        List<Item<String>> existingItems = new ArrayList<Item<String>>();

        newModels.add(new Model(new String("oldString")));
        newModels.add(new Model(new String("newString")));


        existingItems.add(new Item<String>("0", 0, new Model("oldString")));

        Iterator<Item<String>> iterator = strategy.getItems(factory,
                newModels.iterator(),
                existingItems.iterator());

        assertEquals("oldString", iterator.next().getModelObject());
        assertEquals("newString", iterator.next().getModelObject());
    }

}
