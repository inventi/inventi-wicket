package lt.inventi.apollo.wicket.theme;

import org.apache.wicket.Session;

/**
 * An {@link ActiveThemeProvider} implementation that stores the active theme in
 * the user session as style.
 * <p>
 * Uses {@link Session#getStyle()} to get the name of the theme.
 *
 * @author miha
 * @author vplatonov
 * @version 1.0
 */
public class ActiveSessionThemeProvider implements ActiveThemeProvider {

    private final ThemeRepository themes;

    public ActiveSessionThemeProvider(ThemeRepository themes) {
        this.themes = themes;
    }

    @Override
    public ITheme getActiveTheme() {
        String style = Session.get().getStyle();

        if (style == null || style.isEmpty()) {
            return themes.defaultTheme();
        }
        return themes.byName(style);
    }

    @Override
    public void setActiveTheme(String themeName) {
        setActiveTheme(themes.byName(themeName));
    }

    @Override
    public void setActiveTheme(ITheme theme) {
        assertBoundSession();

        if (theme != null) {
            Session.get().setStyle(theme.name());
        } else {
            Session.get().setStyle(null);
        }
    }

    /**
     * checks on existing session, if there isn't one it will be created.
     */
    private static void assertBoundSession() {
        Session session = Session.get();

        if (session.isTemporary()) {
            session.bind();
        }
    }
}
