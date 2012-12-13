package lt.inventi.apollo.wicket.theme;

import java.util.List;

/**
 * @author miha
 * @version 1.0
 */
public interface ThemeRepository {

    /**
     * returns a theme by its name. If
     *
     * @param name The name of the theme
     * @return the theme according to given name
     */
    ITheme byName(final String name);

    /**
     * @return a list of all available themes
     */
    List<ITheme> available();

    /**
     * @return the default theme
     */
    ITheme defaultTheme();
}
