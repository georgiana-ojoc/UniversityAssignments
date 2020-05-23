package locales.application;

import locales.commands.Command;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    public static void main(String[] args) {
        ResourceBundle messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
        ResourceBundle commands = ResourceBundle.getBundle("Commands");
        Scanner scanner = new Scanner(System.in);
        String request;
        while (true) {
            System.out.println(messages.getString("prompt"));
            request = scanner.nextLine();
            String[] parameters = request.split("\\s+");
            if (parameters[0].equals(commands.getString("display-locales.command"))) {
                try {
                    Command command = (Command) Class.forName(commands.getString("display-locales.implementation"))
                            .getConstructor().newInstance();
                    System.out.println(messages.getString("display-locales"));
                    command.execute();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (parameters[0].equals(commands.getString("set-locale.command"))) {
                try {
                    Command command = (Command) Class.forName(commands.getString("set-locale.implementation"))
                            .getConstructor().newInstance();
                    Locale locale = Locale.forLanguageTag(parameters[1]);
                    command.execute(locale);
                    messages = ResourceBundle.getBundle("Messages", Locale.getDefault());
                    Object[] arguments = {locale.getDisplayCountry()};
                    System.out.println(new MessageFormat(messages.getString("set-locale"))
                            .format(arguments));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (parameters[0].equals(commands.getString("locale-information.command"))) {
                try {
                    Command command = (Command) Class.forName(commands.getString("locale-information.implementation"))
                            .getConstructor().newInstance();
                    Locale locale = Locale.forLanguageTag(parameters[1]);
                    Object[] arguments = {locale.getDisplayCountry()};
                    System.out.println(new MessageFormat(messages.getString("locale-information"))
                            .format(arguments));
                    command.execute(locale);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            } else if (parameters[0].equals(commands.getString("locale-flag.command"))) {
                try {
                    Command command = (Command) Class.forName(commands.getString("locale-flag.implementation"))
                            .getConstructor().newInstance();
                    Locale locale = new Locale("en", parameters[1]);
                    Object[] arguments = {locale.getDisplayCountry()};
                    System.out.println(new MessageFormat(messages.getString("locale-flag"))
                            .format(arguments));
                    command.execute(locale);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            } else if (request.equals("exit")) {
                System.out.println(messages.getString("bye"));
                break;
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
    }
}
