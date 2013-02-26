package lt.inventi.wicket.component.breadcrumb;

class TypeBreadcrumbEquality implements BEquality<Breadcrumb> {

    @Override
    public boolean equal(Breadcrumb a, Breadcrumb b) {
        return a.getType().equals(b.getType());
    }

}
