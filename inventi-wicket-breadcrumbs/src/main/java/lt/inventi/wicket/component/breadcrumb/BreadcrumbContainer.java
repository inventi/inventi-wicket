package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BreadcrumbContainer implements Serializable{

    private Map<String, List<Breadcrumb>> breadcrumbMap = new HashMap<String, List<Breadcrumb>>();

    public List<Breadcrumb> getBreadcrumbs(String id){
        List<Breadcrumb> list = breadcrumbMap.get(id);
        if(list != null){
            return new ArrayList<Breadcrumb>(list);
        }
        return null;
    }

    public void storeBreadcrumbTrail(Breadcrumb activeBreadcrumb, List<Breadcrumb> breadcrumbTrail) {

        List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>(breadcrumbTrail);
        breadcrumbs.add(activeBreadcrumb);

        breadcrumbMap.put(activeBreadcrumb.getId().toString(), breadcrumbs);
    }
}
