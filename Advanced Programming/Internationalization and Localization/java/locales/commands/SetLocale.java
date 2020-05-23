package locales.commands;

import java.util.Locale;

public class SetLocale implements Command {
    @Override
    public void execute(Locale locale) {
        Locale.setDefault(locale);
    }
}
