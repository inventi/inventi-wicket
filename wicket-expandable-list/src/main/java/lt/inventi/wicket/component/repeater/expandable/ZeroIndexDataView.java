package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * The only difference this view have comparing with its super class,
 * its that it counts itemId from zero, instead of 1,
 * which is more convinient as that is the same index as in list.
 * Also this class resets childIdCounter back to zero after component was rendered.
 *
 */
public abstract class ZeroIndexDataView<T> extends DataView<T>{

    private transient int childIdCounter = 0;

    public ZeroIndexDataView(String id, IDataProvider<T> dataProvider) {
        this(id, dataProvider, 20);
    }

    public ZeroIndexDataView(String id, IDataProvider<T> dataProvider,
            int itemsPerPage) {
        super(id, dataProvider, itemsPerPage);
    }

    @Override
    protected void onBeforeRender(){
        childIdCounter = 0;
        super.onBeforeRender();
    }

    @Override
    public String newChildId(){
        String id=String.valueOf(childIdCounter);
        childIdCounter++;
        return id;
    }

    @Override
    protected Item<T> newItem(String id, int index, IModel<T> model) {
        Item<T> item = super.newItem(id, index, model);
        if(index % 2 != 0){
            item.add(new AttributeAppender("class", new Model<String>("odd"), " "));
        }
        return item;
    }
}
