package lt.inventi.apollo.wicket.theme;


/**
 * The {@code ActiveThemeProvider} interface
 *
 * @author miha
 * @version 1.0
 */
public interface ActiveThemeProvider {

    /**
     * returns the current active theme (can be user/session scoped). If none is
     * set a default theme should be returned (implementation specific).
     * There is a session scoped implementation: {@code SessionThemeProvider}
     *
     * @return the current active theme
     */
    ITheme getActiveTheme();

    /**
     * sets the active theme by its name.
     *
     * @param themeName the theme name
     */
    void setActiveTheme(String themeName);

    /**
     * sets the active theme
     *
     * @param theme the theme to set
     */
    void setActiveTheme(ITheme theme);
}
