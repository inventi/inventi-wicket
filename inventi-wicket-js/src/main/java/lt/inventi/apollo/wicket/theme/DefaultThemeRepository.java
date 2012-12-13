package lt.inventi.apollo.wicket.theme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.WicketRuntimeException;

/**
 * Uses the theme provided during construction as the default.
 *
 * @author miha
 * @author vplatonov
 * @version 1.0
 */
public class DefaultThemeRepository implements ThemeRepository {

    private final List<ITheme> themes = new ArrayList<ITheme>();
    private ITheme defaultTheme;

    /**
     * Construct.
     */
    public DefaultThemeRepository(ITheme theme) {
        addDefaultTheme(theme);
    }

    /**
     * adds a new theme.
     *
     * @param newTheme
     *            The new theme
     * @return This instance
     */
    public DefaultThemeRepository add(ITheme newTheme) {

        this.themes.add(newTheme);
        return this;
    }

    /**
     * adds an array of new themes.
     *
     * @param newThemes
     *            The new themes
     * @return This instance
     */
    public DefaultThemeRepository add(ITheme... newThemes) {
        assertNoDuplicateNames(newThemes);

        this.themes.addAll(Arrays.asList(newThemes));
        return this;
    }

    private void assertNoDuplicateNames(ITheme... newThemes) {
        if (newThemes == null) {
            throw new WicketRuntimeException("list of themes is null");
        }

        for (ITheme newTheme : newThemes) {
            if (newTheme == null) {
                throw new WicketRuntimeException("theme is null");
            }

            for (ITheme existingTheme : this.themes) {
                if (existingTheme.equals(newTheme)) {
                    throw new WicketRuntimeException("duplicated theme entry: " + newTheme.name());
                }

                if (newTheme.name().equalsIgnoreCase(existingTheme.name())) {
                    throw new WicketRuntimeException("duplicated theme name: " + newTheme.name());
                }
            }
        }
    }

    /**
     * adds a new theme and sets it as default theme.
     *
     * @param theme The new default theme
     * @return This instance
     */
    public DefaultThemeRepository addDefaultTheme(ITheme theme) {
        add(theme);
        return defaultTheme(theme);
    }

    /**
     * sets the default theme.
     *
     * @param theme The new default theme
     * @return This instance
     */
    public DefaultThemeRepository defaultTheme(ITheme theme) {
        return defaultTheme(theme.name());
    }

    /**
     * sets the default theme.
     *
     * @param themeName The new default theme
     * @return This instance
     */
    public DefaultThemeRepository defaultTheme(String themeName) {
        ITheme newDefaultTheme = byName(themeName);

        if (defaultTheme != newDefaultTheme) {
            defaultTheme = newDefaultTheme;
        }

        return this;
    }

    @Override
    public ITheme byName(String name) {
        if (name != null && !name.isEmpty()) {
            for (ITheme theme : themes) {
                if (name.equalsIgnoreCase(theme.name())) {
                    return theme;
                }
            }
        }

        throw new WicketRuntimeException("theme does not exists: " + name);
    }

    @Override
    public List<ITheme> available() {
        return Collections.unmodifiableList(themes);
    }

    @Override
    public ITheme defaultTheme() {
        return defaultTheme;
    }
}
