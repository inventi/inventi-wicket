package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Provides and maps data for autocomplete.
 *
 * @param <T>
 */
public interface AutocompleteDataProvider<T> extends Serializable {

    /**
     * Returns parameters for json response.
     * Some expected parameter keys are:
     * <ul>
     * <li>label - item label</li>
     * <li>value - value, which will be set to the input field (if not specified, then label will be used)</li>
     * <li>id - hidden value to identify specific object</li>
     * </ul>
     * @param object
     * @return
     */
    Map<String, String> getJsonParameters(T item);

    /**
     * Returns new item for the speficied data.
     * This method is invoked on input conversion.
     *
     * @param id
     * @param value
     * @param oldItem
     * @return
     */
    T getObject(String id, String value, T oldItem);

    /**
     * Returns value for the given object, which will be then set to the input field.
     *
     * @param object
     * @return
     */
    String getValue(T object);


    /**
     * Returns id for the given object, which will be then set to the hidden field.
     *
     * @param object
     * @return
     */
    String getId(T object);


    /**
     * Returns list of items for the specified query.
     *
     * Note when implementing this method that the following expressions,
     * should work and return results:
     *
     * doSearch(getLabel(object));
     * getObject(getKey(object));
     *
     * @param query
     * @return
     */
    List<T> searchItems(String query, int size);

}