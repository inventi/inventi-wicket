package lt.inventi.wicket.component.breadcrumb;

public class TypeBreadcrumbEquality implements BEquality<Breadcrumb> {

    @Override
    public boolean equal(Breadcrumb a, Breadcrumb b) {
        return a.getType().equals(b.getType());
    }

}
