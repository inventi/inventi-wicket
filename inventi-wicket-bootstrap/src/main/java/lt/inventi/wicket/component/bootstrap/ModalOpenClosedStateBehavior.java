package lt.inventi.wicket.component.bootstrap;

import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.IBehaviorListener;
import org.apache.wicket.core.request.handler.EmptyAjaxRequestHandler;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Behavior which can track state of the modal across page loads given its model
 * properly persists state.
 *
 * @author vplatonov
 *
 */
public class ModalOpenClosedStateBehavior extends AbstractAjaxBehavior {
    private final IModel<Boolean> model;

    public ModalOpenClosedStateBehavior(IModel<Boolean> modalStateModel) {
        this.model = modalStateModel;
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        response.render(scriptFor(component, "show", getOnOpenUrl(component)));
        response.render(scriptFor(component, "hide", getOnCloseUrl(component)));
    }

    @Override
    public void onRequest() {
        Request r = RequestCycle.get().getRequest();
        Set<String> params = r.getQueryParameters().getParameterNames();
        if (params.contains("open")) {
            model.setObject(true);
        } else if (params.contains("close")) {
            model.setObject(false);
        }

        RequestCycle requestCycle = RequestCycle.get();
        requestCycle.scheduleRequestHandlerAfterCurrent(EmptyAjaxRequestHandler.getInstance());
    }

    private static OnDomReadyHeaderItem scriptFor(Component component, String event, CharSequence url) {
        return OnDomReadyHeaderItem.forScript("$('#" + component.getMarkupId() + "').on('" + event + "', function(e) {\n" +
                "  $.ajax('" + url + "');\n" +
                "});\n");
    }

    private CharSequence getOnOpenUrl(Component component) {
        PageParameters params = new PageParameters();
        params.add("open", "1");
        return component.urlFor(this, IBehaviorListener.INTERFACE, params);
    }

    private CharSequence getOnCloseUrl(Component component) {
        PageParameters params = new PageParameters();
        params.add("close", "1");
        return component.urlFor(this, IBehaviorListener.INTERFACE, params);
    }
}
