package lt.inventi.wicket.component;

import static org.apache.wicket.markup.head.JavaScriptHeaderItem.forReference;
import net.sf.json.JSONObject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.attributes.AjaxRequestAttributes;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.util.time.Duration;

import lt.inventi.wicket.resource.ResourceSettings;

/**
 * Triggers update of provided components on keypress.
 *
 * @author zkybartas, vplatonov
 *
 */
public class KeypressUpdatingBehaviour extends AjaxFormComponentUpdatingBehavior {

    private static final long serialVersionUID = 6644786502516083070L;

    private Component[] components;

    private Duration debounceDelay;

    public KeypressUpdatingBehaviour(Component ...components) {
        super("onkeyup");
        this.components = components;
        debounceDelay = Duration.milliseconds(300);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        super.renderHead(component, response);
        renderJavaScript(response);
    }

    @Override
    protected CharSequence getCallbackScript(Component component) {
        JSONObject cfg = new JSONObject();
        cfg.put("markupId", component.getMarkupId());
        cfg.put("timeout", debounceDelay.getMilliseconds());

        AjaxRequestAttributes attributes = getAttributes();
        CharSequence attrsJson = renderAjaxAttributes(getComponent(), attributes);
        cfg.put("callback", "function() {var call = new Wicket.Ajax.Call(); call.ajax("+attrsJson+");}");
        return String.format("registerBounceEvent(%s)", cfg);
    }

    private static void renderJavaScript(IHeaderResponse response) {
        response.render(forReference(ResourceSettings.get().js().jqueryUi.uiCoreWidget));
        response.render(forReference(new JavaScriptResourceReference(KeypressUpdatingBehaviour.class, "KeypressUpdatingBehaviour.js")));

        response.render(forReference(ThrottleDebounceResourceReference.get()));
    }

    @Override
    protected void onComponentTag(ComponentTag tag) {
        //adding attribute for automated test that ajax is specified
        tag.put("onkeyup", "/*wicketAjaxPost debounce*/");
    }

    @Override
    protected final void onUpdate(AjaxRequestTarget target) {
        onUpdate();

        for(Component component : components){
            target.add(component);
        }
    }

    protected void onUpdate() {
        // do nothing by default
    }

}
