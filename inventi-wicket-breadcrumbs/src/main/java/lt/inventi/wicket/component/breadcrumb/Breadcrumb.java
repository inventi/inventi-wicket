package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;

import org.apache.wicket.Page;
import org.apache.wicket.core.request.handler.IPageProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class Breadcrumb implements Serializable{

    private Integer id;
    private Class<? extends Page> pageClass;

    private IModel<String> titleModel;

    private PageParameters params;
    private int pageId;

    private Integer renderCount;
    private boolean stateless;

    private transient Page webPage;


    public Breadcrumb(Page breadPage) {
        webPage = breadPage;
        id = breadPage.hashCode();
        pageClass = webPage.getClass();
        params = webPage.getPageParameters();
        pageId = webPage.getPageId();
        renderCount = webPage.getRenderCount();
        stateless = webPage.isPageStateless();
        if(webPage instanceof BreadcrumbsPage){
        	titleModel = ((BreadcrumbsPage)breadPage).getTitleModel();
        }
    }

    public Breadcrumb(Class<? extends Page> pageClass, PageParameters params){
        this.pageClass = pageClass;
        this.params = params;
        this.stateless = true;
    }

    public boolean isStateless() {
        return stateless;
    }

    public IModel<String> getTitleModel() {
        return titleModel;
    }

    public Integer getId() {
        return id;
    }

    public Class<? extends Page> getPageClass() {
        return pageClass;
    }

    public PageParameters getParams() {
        return params;
    }

    public IRequestHandler getStatelessTarget() {
        IPageProvider provider = new PageProvider(pageClass, params);
        return new RenderPageRequestHandler(provider, RenderPageRequestHandler.RedirectPolicy.AUTO_REDIRECT);
    }

    public IRequestHandler getTarget(){
        IPageProvider provider = null;
        if(webPage != null){
            provider = new PageProvider(webPage);
        }else if(stateless){
            provider =  new PageProvider(pageClass, params);
        }else{
            provider = new PageProvider(pageId, pageClass, renderCount);
        }
        return new RenderPageRequestHandler(provider, RenderPageRequestHandler.RedirectPolicy.AUTO_REDIRECT);
    }
}
