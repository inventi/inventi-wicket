package lt.dm3;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

public class LoginPage extends WebPage {

    public LoginPage() {
        add(new BookmarkablePageLink<FirstPage>("login", FirstPage.class));
    }
}
