package lt.inventi.wicket.component.bootstrap;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;

/**
 * Java:
 * 
 * <pre>
 * new Accordion(&quot;accordion&quot;).add(
 *     new AccordionGroup(&quot;group1&quot;, new ResourceModel(&quot;group1Label&quot;)).add(
 *         new TextField&lt;String&gt;(&quot;id&quot;)),
 *     new AccordionGroup(&quot;group2&quot;, new ResourceModel(&quot;group2Label&quot;)).add(
 *         new TextField&lt;String&gt;(&quot;name&quot;)));
 * </pre>
 * 
 * HTML:
 * 
 * <pre>
 * &lt;div wicket:id="accordion"&gt;
 *      &lt;div wicket:id="group1"&gt;
 *          &lt;input type="text" wicket:id="id" /&gt;
 *      &lt;/div&gt;
 *      &lt;div wicket:id="group2"&gt;
 *          &lt;input type="text" wicket:id="name" /&gt;
 *      &lt;/div&gt;
 * &lt;/div&gt;
 * </pre>
 * 
 * Result:
 *
 * <pre>
 * &lt;div class="accordion" id="accordiona"&gt;
 *        &lt;div class="accordion-group"&gt;
 *             &lt;div class="accordion-heading"&gt;
 *                 &lt;a href="#bodyb" data-parent="#accordiona" data-toggle="collapse" class="accordion-toggle collapsed"&gt;Group 1&lt;/a&gt;
 *             &lt;/div&gt;
 *             &lt;div id="bodyb" class="accordion-body collapse" style="height: 0px;"&gt;
 *                 &lt;div class="accordion-inner"&gt;
 *                     &lt;input type="text" name="accordion:group1:group1_body:id" value=""&gt;&lt;/input&gt;
 *                 &lt;/div&gt;
 *             &lt;/div&gt;
 *         &lt;/div&gt;
 *        &lt;div class="accordion-group"&gt;
 *             &lt;div class="accordion-heading"&gt;
 *                 &lt;a href="#bodyc" data-parent="#accordiona" data-toggle="collapse" class="accordion-toggle collapsed"&gt;Group 2&lt;/a&gt;
 *             &lt;/div&gt;
 *             &lt;div id="bodyc" class="accordion-body collapse" style="height: 0px;"&gt;
 *                 &lt;div class="accordion-inner"&gt;
 *                     &lt;input type="text" name="accordion:group2:group2_body:name" value=""&gt;&lt;/input&gt;
 *                 &lt;/div&gt;
 *             &lt;/div&gt;
 *        &lt;/div&gt;
 * &lt;/div&gt;
 * </pre>
 *
 * @author vplatonov
 *
 */
public class Accordion extends WebMarkupContainer {

    public Accordion(String id) {
        super(id);
        setOutputMarkupId(true);
        add(new AttributeModifier("class", "accordion"));
    }

}
