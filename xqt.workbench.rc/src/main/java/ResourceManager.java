



import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceManager {
    static final String BASENAME = "Messages";

    public static final ResourceBundle RB = ResourceBundle.getBundle(BASENAME);

    public static ResourceBundle getMessages() {
        return ResourceBundle.getBundle(BASENAME);
    }

    public static ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale);
    }
}
