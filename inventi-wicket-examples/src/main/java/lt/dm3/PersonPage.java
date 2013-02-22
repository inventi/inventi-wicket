package lt.dm3;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

public class PersonPage extends BaseAuthenticatedPage {

    public PersonPage(IModel<Person> person) {
        super(new CompoundPropertyModel<Person>(person));

        add(new Label("name"), new Label("age"));
        add(new Link<Void>("edit") {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick() {
                setNextResponsePage(new FormPage((IModel<Person>) PersonPage.this.getDefaultModel()));
            }
        });
        add(new BookmarkablePageLink<FirstPage>("firstPage", FirstPage.class));
    }

}
