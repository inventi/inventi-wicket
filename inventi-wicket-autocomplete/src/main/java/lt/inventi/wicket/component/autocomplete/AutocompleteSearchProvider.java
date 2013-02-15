package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Provides search results for the autocomplete.
 *
 * @param <S>
 *            type of search results returned by this provider.
 */
public interface AutocompleteSearchProvider<S> extends Serializable {

    /**
     * Returns parameters for json response. Some expected parameter keys are:
     * <ul>
     * <li>label - item label</li>
     * <li>value - value, which will be set to the input field (if not
     * specified, then label will be used)</li>
     * <li>id - hidden value to identify specific object</li>
     * </ul>
     *
     * @param object
     * @return parameters for json response
     */
    Map<String, String> getJsonParameters(S item);

    /**
     * Returns list of items for the specified query.
     *
     * @param query
     * @return list of items for the specified query
     */
    List<S> searchItems(String query, int size);

}