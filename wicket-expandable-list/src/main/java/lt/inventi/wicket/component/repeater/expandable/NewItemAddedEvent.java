package lt.inventi.wicket.component.repeater.expandable;

public class NewItemAddedEvent {

	private Object newItem;

	public NewItemAddedEvent(Object newItem){
		this.newItem = newItem;
	}

	public Object getNewItem() {
		return newItem;
	}
}
