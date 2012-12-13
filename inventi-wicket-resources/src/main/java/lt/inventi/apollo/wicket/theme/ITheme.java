package lt.inventi.apollo.wicket.theme;

import org.apache.wicket.markup.head.IHeaderResponse;

/**
 * A named theme which knows how to render itself into the response.
 * 
 * @author miha
 * @author vplatonov
 * @version 1.0
 */
public interface ITheme {

    /**
     * @return The unique name of this theme.
     */
    String name();

    /**
     * Print to the web response what ever the {@link ITheme} wants to contribute to the head section.
     *
     * @param response The {@link IHeaderResponse} instance
     */
    void renderHead(IHeaderResponse response);

}
