package lt.inventi.wicket.component.bootstrap.form;

import org.apache.wicket.markup.IMarkupFragment;
import org.apache.wicket.markup.Markup;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.FormComponent;

/**
 * Adds a bit of magic to bootstrap's horizontal forms.
 * <p>
 * If SingleControlGroup is placed inside of a form you MUST have a localized
 * property with key equal to <code>formId.formComponentId</code>, otherwise
 * with <code>formComponentId</code>.
 * <p>
 * Java:
 * 
 * <pre>
 * new SingleControlGroup(new TextField&lt;String&gt;(&quot;input&quot;));
 * </pre>
 *
 * HTML:
 * 
 * <pre>
 * &lt;input type="text" wicket:id="input" /&gt;
 * </pre>
 *
 * Properties:
 *
 * <pre>
 * input = Label
 * </pre>
 * 
 * Result:
 *
 * <pre>
 * &lt;div class="control-group"&gt;
 *         &lt;label class="control-label"&gt;Label&lt;/label&gt;
 *
 *         &lt;div class="controls"&gt;
 *             &lt;input type="text" name="input:input:input_body:input" value="" /&gt;
 *         &lt;/div&gt;
 * &lt;/div&gt;
 * </pre>
 * 
 * @author vplatonov
 * 
 */
public class SingleControlGroup extends WebMarkupContainer {

    private final ControlGroup group;

    public SingleControlGroup(FormComponent<?> formComponent) {
        super(formComponent.getId());
        this.group = new ControlGroup(formComponent.getId());
        this.group.add(formComponent);
        add(group);
    }

    @Override
    public IMarkupFragment getMarkup() {
        IMarkupFragment markup = super.getMarkup();
        return Markup.of("<div wicket:id=\"" + getId() + "\">" + markup.toString(true) + "</div>");
    }
}
