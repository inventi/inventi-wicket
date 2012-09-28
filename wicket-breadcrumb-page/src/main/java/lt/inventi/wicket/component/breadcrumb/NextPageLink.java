package lt.inventi.wicket.component.breadcrumb;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class NextPageLink extends BookmarkablePageLink<Void>{

	public <C extends Page> NextPageLink(String id, Class<C> pageClass,
			PageParameters parameters) {
		super(id, pageClass, parameters);
	}

	public <C extends Page> NextPageLink(String id, Class<C> pageClass) {
		super(id, pageClass);
	}
	
	protected void onInitialize(){
		Breadcrumbs.getContainer().storeBreadcrumbTrail(new Breadcrumb(getPage()), 
				Breadcrumbs.getBreadcrumbTrail(getPage().getPageParameters()));
		parameters.set(Breadcrumbs.BRD, new Breadcrumb(getPage()).getId());
		
		super.onInitialize();
	}
}
