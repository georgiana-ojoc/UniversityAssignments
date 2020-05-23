package locales.commands;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class LocaleInformation implements Command {
    @Override
    public void execute(Locale locale) {
        System.out.println("Country: " + locale.getDisplayCountry());
        System.out.println("Language: " + locale.getDisplayLanguage());
        System.out.println("Currency: " + NumberFormat.getCurrencyInstance(locale).getCurrency());
        System.out.println("Week Days:");
        String[] weekDays = new DateFormatSymbols().getWeekdays();
        for (int index = 2; index < weekDays.length; ++index) {
            System.out.println(weekDays[index]);
        }
        System.out.println(weekDays[1]);
        System.out.println("Months:");
        String[] months = new DateFormatSymbols().getMonths();
        for (int index = 0; index < months.length - 1; ++index) {
            System.out.println(months[index]);
        }
        System.out.println("Today: " + LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                .withLocale(locale)));
    }
}
