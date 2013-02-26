package lt.inventi.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import lt.inventi.wicket.component.breadcrumb.PreviousPageLink;

public class PersonPage extends BaseAuthenticatedPage {

    public PersonPage(IModel<Person> person) {
        super(new CompoundPropertyModel<Person>(person));

        add(new Label("name"), new Label("age").setOutputMarkupId(true));
        add(new Link<Void>("edit") {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick() {
                setNextResponsePage(new FormPage((IModel<Person>) PersonPage.this.getDefaultModel()));
            }
        });
        add(new AjaxLink<Void>("addAge") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                ((Person) PersonPage.this.getDefaultModelObject()).age += 10;
                target.add(PersonPage.this.get("age"));
            }
        });
        add(new BookmarkablePageLink<FirstPage>("firstPage", FirstPage.class));
        add(new PreviousPageLink("back"));
    }

    @Override
    public IModel<String> getBreadcrumbTitleModel() {
        return Model.of("PersonPage " + getDefaultModelObjectAsString());
    }
}
