package lt.inventi.wicket.component.bootstrap.in;

import de.agilecoders.wicket.core.markup.html.bootstrap.components.TooltipBehavior;

/**
 * Specifies classes which should be imported from the <a
 * href="https://github.com/l0rdn1kk0n/wicket-bootstrap"
 * >de.agilecoders.bootstrap</a>. These classes (and all of their dependencies)
 * will be shaded into the containing jar file.
 *
 * @author vplatonov
 *
 */
public class WicketBootstrapImport {
    static {
        assert TooltipBehavior.class != null;
    }
}
