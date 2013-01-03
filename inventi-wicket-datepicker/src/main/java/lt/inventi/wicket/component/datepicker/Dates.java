package lt.inventi.wicket.component.datepicker;


/**
 * Helper class for dates.
 *
 * @author miha
 */
public final class Dates {

    /**
     * Construct.
     */
    private Dates() {
        throw new UnsupportedOperationException();
    }

    /**
     * translates a javascript date format into a java date format.
     *
     * @param javaScriptDateFormat
     *            The javascript date format as string
     * @return java date format
     */
    public static String toJavaDateFormat(final String javaScriptDateFormat) {
        return nullToEmpty(javaScriptDateFormat).replaceAll("mm", "MM");
    }

    /**
     * translates a java date format into a javascript date format.
     *
     * @param javaDateFormat
     *            The java date format as string
     * @return javascript date format
     */
    public static String toJavaScriptDateFormat(final String javaDateFormat) {
        return nullToEmpty(javaDateFormat).replaceAll("MM", "mm");
    }

    private static String nullToEmpty(String s) {
        if (s == null) {
            return "";
        }
        return s;
    }

}
