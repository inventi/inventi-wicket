package lt.inventi.wicket.component.repeater;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;

public class ClickableRow extends Behavior {

    private final String exceptionClass;

    public ClickableRow(String exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public ClickableRow() {
        this.exceptionClass = "not-clickable";
    }

    @Override
    public void bind(Component component) {
        component.setOutputMarkupId(true);
    }

    @Override
    public void renderHead(Component component, IHeaderResponse response) {
        StringBuilder script = new StringBuilder();

        script.append("$(document).ready(function() {\n");
        script.append(" var trs = $('tr[id^=" + component.getMarkupId() + "] td[class!=\"" + exceptionClass + "\"]');\n");
        script.append(" trs.parent().addClass('clickable');\n");
        script.append("	trs.click(function(event) {\n");
        script.append("		var viewLink = $(this).parent().find('a')[0];\n");
        script.append("		var targetParent = $(event.target).parent()[0];\n");
        script.append("		if (viewLink != event.target && viewLink != targetParent) {\n");
        script.append("         if ($(viewLink).attr('href') != '#')\n");
        script.append("			  document.location = $(viewLink).attr('href');\n");
        script.append("         else\n");
        script.append("           $(viewLink).click();\n");
        script.append("		}\n");
        script.append("	})\n");
        script.append("});");

        response.render(OnDomReadyHeaderItem.forScript(script.toString()));
    }
}
