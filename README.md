# PrivateBank – OOS Praktikum

Java-Projekt aus dem Modul **Objektorientierte Systeme (OOS)** an der FH Aachen.
Es modelliert eine einfache Privatbank mit Konten und Transaktionen, speichert die Daten als JSON und hat eine JavaFX-Oberfläche.

## Funktionen

- **Transaktionen:** `Payment` (Ein-/Auszahlung mit Zinsen), `Transfer`, `IncomingTransfer`, `OutgoingTransfer`
- **Bank:** Konten anlegen/löschen, Transaktionen hinzufügen/entfernen, Kontostand berechnen, Sortieren und Filtern
- **Persistenz:** Jedes Konto wird als JSON-Datei gespeichert (Gson mit eigenem Serializer/Deserializer)
- **GUI:** JavaFX mit FXML (Kontoübersicht und Kontodetailansicht)
- **Exceptions:** eigene Exceptions für ungültige Konten, Transaktionen und Attribute
- **Tests:** JUnit 5 (48 Tests)

## Projektstruktur

```
src/main/java/bank/        Fachlogik (Bank, PrivateBank, Transaktionen)
src/main/java/bank/exceptions/
src/main/java/ui/          JavaFX-Controller und Startklasse
src/main/resources/ui/     FXML-Views
src/test/java/             JUnit-Tests
testdata/                  Beispielkonto als JSON
```

## Voraussetzungen

- Java 17 oder neuer
- Maven 3.9+

## Starten

```bash
mvn javafx:run
```

In IntelliJ die Klasse `ui.Launcher` starten (nicht `ui.FxApplication`).

## Tests ausführen

```bash
mvn test
```

## Autor

Burak Mavili
