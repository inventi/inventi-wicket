package lt.inventi.wicket.component.autocomplete;

public abstract class AbstractSearchResultLabelProvider<T, S> implements AutocompleteDataLabelProvider<T> {

    private final AutocompleteDataLabelProvider<S> underlying;

    protected AbstractSearchResultLabelProvider(AutocompleteDataLabelProvider<S> underlying) {
        this.underlying = underlying;
    }

    @Override
    public String extractLabel(T item) {
        return underlying.extractLabel(findUsing(item));
    }

    protected abstract S findUsing(T object);
}
