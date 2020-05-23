package locales.commands;

import java.util.Locale;

public class DisplayLocales implements Command {
    @Override
    public void execute() {
        System.out.println("Default locale:");
        System.out.println(Locale.getDefault().getDisplayCountry() + "\t" + Locale.getDefault().getDisplayLanguage());
        System.out.print("Available locales:");
        Locale[] available = Locale.getAvailableLocales();
        for (Locale locale : available) {
            System.out.println(locale.getDisplayCountry() + "\t" + locale.getDisplayLanguage(locale));
        }
    }
}
