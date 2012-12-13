package lt.inventi.apollo.wicket.theme.behavior;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;

import lt.inventi.apollo.wicket.theme.ITheme;
import lt.inventi.apollo.wicket.theme.settings.ThemeSettings;
import lt.inventi.wicket.resource.ResourceSettings;

/**
 * Renders the current active theme into the response.
 *
 * @author miha
 * @author vplatonov
 * @version 1.0
 */
public class ActiveThemeBehavior extends Behavior {

    @Override
    public void renderHead(Component component, IHeaderResponse headerResponse) {
        ThemeSettings settings = getCurrentSettings(component);

        renderHead(settings, headerResponse);
    }

    private static void renderHead(ThemeSettings settings, IHeaderResponse headerResponse) {
        ITheme theme = settings.getActiveThemeProvider().getActiveTheme();
        theme.renderHead(headerResponse);
    }

    private static ThemeSettings getCurrentSettings(Component component) {
        ThemeSettings settings = ResourceSettings.get(component.getApplication()).themeSettings();
        if (settings == null) {
            throw new WicketRuntimeException("No Theme settings associated with this Application. Did you call ResourceSettings.install()?");
        }
        return settings;
    }

}
