package lt.inventi.apollo.wicket.theme;

import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.request.resource.ResourceReference;


/**
 * TODO: document
 *
 * @author miha
 * @version 1.0
 */
public class Theme implements ITheme {

    private final String name;
    private final Set<ResourceReference> resourceReferences;

    public Theme(final String name, final ResourceReference... resourceReferences) {
        this.name = name;
        this.resourceReferences = new LinkedHashSet<ResourceReference>(resourceReferences.length);
        for (ResourceReference r : resourceReferences) {
            if (r != null) {
                this.resourceReferences.add(r);
            }
        }
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        for (ResourceReference resourceReference : resourceReferences) {
            if (resourceReference instanceof CssResourceReference) {
                response.render(CssHeaderItem.forReference(resourceReference));
            } else if (resourceReference instanceof JavaScriptResourceReference) {
                response.render(JavaScriptHeaderItem.forReference(resourceReference));
            }
        }
    }
}
