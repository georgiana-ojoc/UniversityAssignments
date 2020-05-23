package locales.commands;

import java.util.Locale;

public interface Command {
    default void execute() {

    }

    default void execute(Locale locale) {

    }
}
