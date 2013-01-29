package lt.inventi.wicket.component.bootstrap.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.util.iterator.ComponentHierarchyIterator;

public class ControlGroup extends Border {

    private final Label label = new Label("label");

    public ControlGroup(String id) {
        super(id);

        addToBorder(label);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();
        setRenderBodyOnly(true);
        String resourcePrefix = "";

        Form<?> form = findParentForm();
        if (form != null) {
            resourcePrefix = form.getId() + ".";
        }
        label.setDefaultModel(new ResourceModel(resourcePrefix + getLabelModelId()));
    }

    private String getLabelModelId() {
        List<FormComponent<?>> formComponents = findFormComponents();
        if (formComponents.size() == 1) {
            return formComponents.get(0).getId();
        }
        return getId();
    }

    private Form<?> findParentForm() {
        MarkupContainer parent = getParent();
        while (parent != null && !(parent instanceof Form<?>)) {
            parent = parent.getParent();
        }
        return (Form<?>) parent;
    }

    private List<FormComponent<?>> findFormComponents() {
        ComponentHierarchyIterator it = getBodyContainer().visitChildren(FormComponent.class);

        List<FormComponent<?>> components = new ArrayList<FormComponent<?>>();
        while (it.hasNext()) {
            components.add((FormComponent<?>) it.next());
        }

        return components;
    }
}