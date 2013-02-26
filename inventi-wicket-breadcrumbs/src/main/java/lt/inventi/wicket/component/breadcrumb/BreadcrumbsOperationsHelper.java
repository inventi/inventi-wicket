package lt.inventi.wicket.component.breadcrumb;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreadcrumbsOperationsHelper implements IBreadcrumbsOperations, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(BreadcrumbsOperationsHelper.class);

    private final Component context;

    public BreadcrumbsOperationsHelper(Component context) {
        this.context = context;
    }

    @Override
    public void setResponseToPreviousPage() {
        BreadcrumbsRedirectHelper.setResponseToPreviousPage(context);
    }

    @Override
    public void setNextResponsePage(IRequestablePage page) {
        if (context.getPage().getPageParameters() == page.getPageParameters()) {
            throw new IllegalStateException(
                "Please construct pages using cloned page parameters, otherwise your breadcrumb history will be corrupted! "
                    + "Same page parameter instance found for current page " + context.getPage() + " transitioning to next page " + page);
        }
        BreadcrumbsRedirectHelper.setNextResponsePage(context, page);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz) {
        BreadcrumbsRedirectHelper.setNextResponsePage(context, clazz);
    }

    @Override
    public void setNextResponsePage(Class<? extends IRequestablePage> clazz, PageParameters params) {
        PageParameters nextParams = params;
        if (context.getPage().getPageParameters() == params) {
            nextParams = new PageParameters(params);
            if (logger.isTraceEnabled()) {
                logger.trace("Cloning page parameters for page {} in context {}", clazz, context);
            }
        }
        BreadcrumbsRedirectHelper.setNextResponsePage(context, clazz, nextParams);
    }

}
