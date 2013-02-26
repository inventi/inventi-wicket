package lt.inventi.wicket.component.breadcrumb.hierarchy;

import org.apache.wicket.markup.html.WebPage;

class BreadcrumbHierarchyBuilder {
    static class TreeBuilder {
        public static TreeBuilder of(Class<? extends WebPage> root) {
            return new TreeBuilder();
        }

        public TreeBuilder then(TreeBuilder child) {
            // TODO Auto-generated method stub
            return null;
        }
    }

    public static BreadcrumbHierarchyBuilder hierarchy(TreeBuilder tree) {
        return new BreadcrumbHierarchyBuilder().andHierarchy(tree);
    }

    public BreadcrumbHierarchyBuilder andHierarchy(TreeBuilder tree) {
        return this;
    }

}
