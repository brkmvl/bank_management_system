package ui;

/**
 * Startklasse für die IDE.
 * <p>
 * Wird eine Klasse, die {@link javafx.application.Application} erweitert, direkt
 * gestartet, prüft Java, ob JavaFX als Modul geladen ist, und bricht sonst ab.
 * Diese Klasse erweitert {@code Application} nicht und umgeht so die Prüfung.
 * </p>
 *
 * @author Burak Mavili
 */
public class Launcher {
    public static void main(String[] args) {
        FxApplication.main(args);
    }
}
