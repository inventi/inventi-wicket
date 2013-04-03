package lt.inventi.wicket;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public class FormPage extends BaseAuthenticatedPage {

    public FormPage() {
        this(Model.of(new Person()));
    }

    public FormPage(IModel<Person> person) {
        super(person);

        IModel<Person> personModel = CompoundPropertyModel.<Person> of(getPersonModel());
        add(new Form<Person>("form", personModel).add(
            new TextField<String>("name"), new TextField<Integer>("age"),
            new PersonAutocomplete("bestFriend"),
            new SubmitLink("submit") {
                @Override
                public void onSubmit() {
                    setNextResponsePage(new PersonPage(getPersonModel()));
                }
            }
        ));

        add(new Link<Void>("back") {
            @Override
            public void onClick() {
                setResponseToPreviousPage();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private IModel<Person> getPersonModel() {
        return (IModel<Person>) getDefaultModel();
    }

}
