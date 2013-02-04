package lt.inventi.wicket.component.datepicker;

import java.util.Calendar;
import java.util.Date;

import lt.inventi.wicket.component.datepicker.DateTextFieldConfig.View;

public abstract class DateTextFieldConfigs {

    private DateTextFieldConfigs() {
        // static utils
    }

    public static DateTextFieldConfig forBirthDate() {
        return new DateTextFieldConfig().withView(View.Decade).withEndDate(today());
    }

    private static Date today() {
        Calendar c = calendarWithNoTime();
        return c.getTime();
    }

    private static Calendar calendarWithNoTime() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    }

}
