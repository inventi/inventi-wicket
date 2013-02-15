package lt.inventi.wicket.component.autocomplete;

import java.io.Serializable;

/**
 * Provides and maps data for autocomplete.
 *
 * @param <T>
 */
public interface AutocompleteDataProvider<T> extends Serializable {

    /**
     * Returns new item for the specified data.
     * <p>
     * This method is invoked during input conversion.
     *
     * @param id
     * @param value
     *            input of the value field which may or may not match the actual
     *            search result
     * @param oldItem
     * @return new item for the specified data
     */
    T getObject(String id, String value, T oldItem);

    /**
     * Returns id for the given object, which will be then set to the hidden
     * field.
     *
     * @param object
     * @return id for the given object
     */
    String getId(T object);

}