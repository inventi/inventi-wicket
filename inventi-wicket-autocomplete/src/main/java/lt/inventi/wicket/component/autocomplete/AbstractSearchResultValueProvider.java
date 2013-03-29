package lt.inventi.wicket.component.autocomplete;

public abstract class AbstractSearchResultValueProvider<T, S> implements AutocompleteDataValueProvider<T> {

    private final AutocompleteDataValueProvider<S> underlying;

    protected AbstractSearchResultValueProvider(AutocompleteDataValueProvider<S> underlying) {
        this.underlying = underlying;
    }

    @Override
    public String extractValue(T item) {
        return underlying.extractValue(findUsing(item));
    }

    protected abstract S findUsing(T object);
}
