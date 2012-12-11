package lt.inventi.wicket.component.repeater.expandable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Utilities for repeater components
 */
public abstract class Repeaters {

    private Repeaters() {
        // static utils
    }

    public static FormComponent<?> getFirstFormComponent(Item<?> item){
        return item.visitChildren(new IVisitor<Component, FormComponent<?>>() {
            @Override
            public void component(Component object, IVisit<FormComponent<?>> visit) {
                if(object instanceof FormComponent){
                    visit.stop((FormComponent<?>)object);
                }
            }
        });
    }
}
