package lt.inventi.apollo.wicket.theme.settings;

import lt.inventi.apollo.wicket.theme.ActiveSessionThemeProvider;
import lt.inventi.apollo.wicket.theme.ActiveThemeProvider;
import lt.inventi.apollo.wicket.theme.DefaultThemeRepository;
import lt.inventi.apollo.wicket.theme.ITheme;
import lt.inventi.apollo.wicket.theme.none.EmptyTheme;

public final class ThemeSettings {

    private final ActiveThemeProvider provider;

    public ThemeSettings() {
        this(new EmptyTheme());
    }

    public ThemeSettings(ITheme theme) {
        this.provider = new ActiveSessionThemeProvider(new DefaultThemeRepository(theme));
    }

    public ThemeSettings(ITheme... themes) {
        DefaultThemeRepository repo = new DefaultThemeRepository(themes[0]);
        for (int i = 1; i < themes.length; i++) {
            repo.add(themes[i]);
        }
        this.provider = new ActiveSessionThemeProvider(repo);
    }

    public ActiveThemeProvider getActiveThemeProvider() {
        return provider;
    }

}
