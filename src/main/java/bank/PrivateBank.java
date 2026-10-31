package bank;

import bank.exceptions.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Die Klasse {@code PrivateBank} implementiert das Interface {@link Bank} und
 * stellt die konkrete Abstraktion einer privaten Bank dar.
 * <p>
 * Sie verwaltet Konten mittels einer Map, die Kontonamen mit den zugehörigen
 * Transaktionslisten verknüpft. Sie wendet bankinterne Zinssätze auf {@link Payment}-Objekte an
 * und unterstützt die Berechnung des Kontostands mittels Polymorphie .
 * </p>
 * * @author Burak Mavili
 */
public class PrivateBank implements Bank {
    /**
     * der Name der privaten Bank.
     */
    private String name;
    /**
     * Der Zinssatz (0 bis 1), der bei einer Einzahlung (Deposit) anfällt.
     */
    private double incomingInterest;
    /**
     * Der Zinssatz (0 bis 1), der bei einer Auszahlung (Withdrawal) anfällt.
     */
    private double outgoingInterest;
    /**
     * Speichert die Verknüpfung von Kontonamen (String) mit den zugehörigen
     * Transaktionslisten (List<Transaction>).
     */
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    private String directoryName;

    public String getDirectoryName() {
        return directoryName;
    }


    /**
     * Gibt den Namen der Bank zurück.
     *
     * @return Der Name der Bank.
     */
    public String get_Name() {
        return name;
    }

    /**
     * Setzt den Namen der Bank.
     *
     * @param name Der neue Name der Bank.
     */
    public void set_Name(String name) {
        this.name = name;
    }

    /**
     * Gibt den Zinssatz für Einzahlungen zurück.
     *
     * @return Der Zinssatz für Einzahlungen.
     */
    public double get_incomingInterest() {
        return incomingInterest;
    }

    /**
     * Setzt den Zinssatz für Einzahlungen.
     *
     * @param incomingInterest Der neue Zinssatz (0 bis 1).
     * @throws TransactionAttributeException Wenn der Zinssatz außerhalb des gültigen Bereichs (0 bis 1) liegt.
     */
    public void set_incomingInterest(double incomingInterest) {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Incoming soll sich  zwischen 0 and 1 befinden.");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }

    /**
     * Gibt den Zinssatz für Auszahlungen zurück.
     *
     * @return Der Zinssatz für Auszahlungen.
     */
    public double get_outgoingInterest() {
        return outgoingInterest;
    }

    /**
     * Setzt den Zinssatz für Auszahlungen.
     *
     * @param outgoingInterest Der neue Zinssatz (0 bis 1).
     * @throws TransactionAttributeException Wenn der Zinssatz außerhalb des gültigen Bereichs (0 bis 1) liegt.
     */
    public void set_outgoingInterest(double outgoingInterest) {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Outgoing soll sich  zwischen 0 and 1 befinden.");
        } else {
            this.outgoingInterest = outgoingInterest;
        }
    }

    /**
     * Erstellt eine neue {@code PrivateBank}-Instanz mit dem angegebenen Namen und Zinssätzen.
     *
     * @param name             Der Name der Bank.
     * @param incomingInterest Der Zinssatz für Einzahlungen.
     * @param outgoingInterest Der Zinssatz für Auszahlungen.
     * @throws TransactionAttributeException Wenn die Zinssätze ungültig sind.
     */

    public PrivateBank(String name, double incomingInterest, double outgoingInterest, String directoryName) throws IOException {
       /*this.name=name;
       this.incomingInterest=incomingInterest;
       this.outgoingInterest=outgoingInterest;*/
        set_Name(name);
        set_incomingInterest(incomingInterest);
        set_outgoingInterest(outgoingInterest);
        this.directoryName = directoryName;
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdir();
        }

        readAccounts();
    }

    /**
     * Kopierkonstruktor: Erstellt eine neue {@code PrivateBank}-Instanz basierend auf einer vorhandenen.
     *
     * @param X Die zu kopierende {@code PrivateBank}-Instanz.
     * @throws TransactionAttributeException Wenn die Zinssätze des Quellobjekts ungültig sind.
     */
    public PrivateBank(PrivateBank X) throws IOException {
        this(X.name, X.incomingInterest, X.outgoingInterest, X.directoryName);
    }

    /**
     * Gibt eine Textdarstellung der Bank zurück.
     *
     * @return Text mit Datum,Incoming,oUTGOING UND tRANSACTION
     */
    @Override
    public String toString() {
        return "PrivateBank ->  " + "Name: " + this.get_Name() + ", IncomingInterest: " + this.get_incomingInterest() + ", OutgoingInterest: " + this.get_outgoingInterest() + ", Transactionen: " + accountsToTransactions;

    }

    /**
     * Vergleicht dieses bANK-Objekt mit einem anderen Objekt.
     * Zwei Objekte sind gleich, wenn alle geerbten Werte und die Zinsen identisch sind.
     *
     * @param obj das zu vergleichende OBERKLASSE Objekt
     * @return true, wenn beide Payments gleich sind, sonst false
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        PrivateBank other = (PrivateBank) obj;

        return this.name.equals(other.name) &&
                this.incomingInterest == other.incomingInterest &&
                this.outgoingInterest == other.outgoingInterest &&
                this.accountsToTransactions.equals(other.accountsToTransactions);
    }


    /**
     * Erstellt ein neues Konto ohne Starttransaktionen.
     *
     * @param account Der Name des zu erstellenden Kontos.
     * @throws AccountAlreadyExistsException Wenn ein Konto mit diesem Namen bereits existiert.
     */

    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException, IOException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Konto  schon existiert");
        } else {
            accountsToTransactions.put(account, new ArrayList<>());
        }
        writeAccount(account);
    }

    /**
     * Erstellt ein neues Konto und fügt eine Liste von Starttransaktionen hinzu.
     * Überprüft Transaktionen auf Duplikate und Attributgültigkeit und wendet die Bankzinsen an.
     *
     * @param account      Der Name des zu erstellenden Kontos.
     * @param transactions Die Liste der Starttransaktionen.
     * @throws AccountAlreadyExistsException    Wenn das Konto bereits existiert.
     * @throws TransactionAlreadyExistException Wenn Duplikate in der Transaktionsliste gefunden werden.
     * @throws TransactionAttributeException    Wenn Transaktionsattribute (Zinsen, Betrag) ungültig sind.
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {

        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Konto ist schon vorhanden!" + account);
        }
        List<Transaction> neueTransaction = new ArrayList<>();
//for (Transaction current : transactions)
        for (int i = 0; i < transactions.size(); i++) {
            Transaction current = transactions.get(i);

            if (neueTransaction.contains(current)) {
                throw new TransactionAlreadyExistException("Transaktion ist schon in der Liste " + account);


            }

            if (current instanceof Payment paymentObject) {


                if (paymentObject.get_incomingInterest() < 0.0 || paymentObject.get_incomingInterest() > 1.0 ||
                        paymentObject.get_outgoingInterest() < 0.0 || paymentObject.get_outgoingInterest() > 1.0) {
                    throw new TransactionAttributeException("Ungültige Zinssätze im Payment!");
                }

                paymentObject.set_incomingInterest(this.incomingInterest);
                paymentObject.set_outgoingInterest(this.outgoingInterest);

            } else if (current instanceof Transfer transferRef) {
                if (transferRef.get_Amount() <= 0.0) {
                    throw new TransactionAttributeException("Ungültiger Betrag im Transfer!");
                }
            }

            neueTransaction.add(current);
        }

        // Wenn alle Transactionen erfolgreich sind danach werden die Liste und account hinzugefuegt
        accountsToTransactions.put(account, neueTransaction);
        writeAccount(account);
    }

    /**
     * Fügt eine einzelne Transaktion zu einem bereits existierenden Konto hinzu.
     * Überprüft Kontenexistenz, Duplikate und Attributgültigkeit. Wendet Bankzinsen auf Payments an.
     *
     * @param account     Der Name des Kontos.
     * @param transaction Die hinzuzufügende Transaktion.
     * @throws TransactionAlreadyExistException Wenn die Transaktion bereits auf dem Konto existiert.
     * @throws AccountDoesNotExistException     Wenn das angegebene Konto nicht existiert.
     * @throws TransactionAttributeException    Wenn Transaktionsattribute ungültig sind.
     */


    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Konto ist nicht schon vorhanden!(addTransaction)");
        }

        List<Transaction> neueaccountT = accountsToTransactions.get(account);
        if (neueaccountT.contains(transaction)) {
            throw new TransactionAlreadyExistException("Transaction ist schon vorhanden bei der addTransaction");
        }

        if (transaction instanceof Payment payment) {
            if (payment.get_incomingInterest() < 0 || payment.get_incomingInterest() > 1 || payment.get_outgoingInterest() < 0 || payment.get_outgoingInterest() > 1) {
                throw new TransactionAttributeException("Ungültige Zinssätze bei der AddTransaction");
            }
            payment.set_incomingInterest(this.incomingInterest);
            payment.set_outgoingInterest(this.outgoingInterest);
        } /*else if (transaction instanceof Transfer transferRef) {
            if (transferRef.get_Amount() < 0) {
                throw new TransactionAttributeException("Ungiltige Betrag im Transfer!(ADDTRANSACTION)");
            }

        }*/
        neueaccountT.add(transaction);
        writeAccount(account);
    }

    /**
     * Entfernt eine Transaktion von einem Konto.
     *
     * @param account     Der Name des Kontos.
     * @param transaction Die zu entfernende Transaktion.
     * @throws AccountDoesNotExistException     Wenn das Konto nicht existiert.
     * @throws TransactionDoesNotExistException Wenn die Transaktion auf dem Konto nicht gefunden wurde.
     */
    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {
        if (!(accountsToTransactions.containsKey(account))) {
            throw new AccountDoesNotExistException("Es gibt keine Konto mit diesem Name->" + account);
        }
        List<Transaction> removeT = accountsToTransactions.get(account);
        if (!(removeT.contains(transaction))) {
            throw new TransactionDoesNotExistException("Die  Transaktion wurde in diesem Konto nicht gefunden!");
        }
        removeT.remove(transaction);
        writeAccount(account);

    }

    /**
     * Prüft, ob eine spezifische Transaktion auf einem Konto existiert.
     *
     * @param account     Der Name des Kontos.
     * @param transaction Die zu prüfende Transaktion.
     * @return {@code true}, wenn die Transaktion gefunden wird, ansonsten {@code false}.
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        if (!accountsToTransactions.containsKey(account)) {
            return false;
        } else {
            List<Transaction> containsList = accountsToTransactions.get(account);
            if ((containsList.contains(transaction))) {
                return true;
            } else {
                return false;
            }

        }
    }

    /**
     * Berechnet den aktuellen Kontostand für das angegebene Konto (Variante 1).
     * Nutzt Polimorphie über {@code calculate()} zur Unterscheidung von Ein- und Ausgängen.
     *
     * @param account Der Name des Kontos.
     * @return Der aktuelle Kontostand.
     */
    @Override
    public double getAccountBalance(String account) {
        if (!accountsToTransactions.containsKey(account)) {
            return 0;
        }
        List<Transaction> transactions = accountsToTransactions.get(account);
        double accountsumme = 0.0;
        for (Transaction transactiontt : transactions) {
            accountsumme = accountsumme + transactiontt.calculate();


        }
        return accountsumme;


    }

    /**
     * Gibt die vollständige, ungefilterte Liste aller Transaktionen für das angegebene Konto zurück.
     *
     * @param account Der Name des Kontos.
     * @return Die Liste aller Transaktionen, oder eine leere Liste, wenn das Konto nicht existiert.
     */
    @Override

    public List<Transaction> getTransactions(String account) {
        if (!accountsToTransactions.containsKey(account)) {
            return new ArrayList<>();
        }

        return accountsToTransactions.get(account);

    }
/*
 List<Transaction> sorted = new ArrayList<>(accountsToTransactions.get(account));
  for (int i = 0; i < sorted.size() - 1; i++) {

        int minIndex = i;  // şu ana kadar bulunan en küçük elemanın index'i

        for (int j = i + 1; j < sorted.size(); j++) {

            double valJ = sorted.get(j).calculate();
            double valMin = sorted.get(minIndex).calculate();

            // daha küçük bir değer bulunduysa minIndex güncellenir
            if (valJ < valMin) {
                minIndex = j;
            }
        }

        // 4. Swap: i ile minIndex'teki eleman yer değiştirir
        Transaction temp = sorted.get(i);
        sorted.set(i, sorted.get(minIndex));
        sorted.set(minIndex, temp);
    }*/

    /**
     * Gibt eine sortierte Liste aller Transaktionen für das angegebene Konto zurück.
     * Die Sortierung basiert auf dem berechneten Betrag (calculate()).
     *
     * @param account Der Name des Kontos.
     * @param asc     {@code true} für aufsteigende (ascending) Sortierung, {@code false} für absteigende (descending).
     * @return Die sortierte Liste.
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {

        if (!accountsToTransactions.containsKey(account)) {
            return new ArrayList<>();
        }

        List<Transaction> originalList = accountsToTransactions.get(account);
        List<Transaction> sortedList = new ArrayList<>(originalList);
        sortedList.sort(Comparator.comparing(Transaction::calculate));
        if (!asc) {
            Collections.reverse(sortedList);
        }
        return sortedList;
    }

    /**
     * Gibt eine gefilterte Liste von Transaktionen zurück, basierend auf deren Vorzeichen (Eingang/Ausgang).
     *
     * @param account  Der Name des Kontos.
     * @param positive {@code true} für positive (Eingänge) Transaktionen, {@code false} für negative (Ausgänge).
     * @return Die gefilterte Liste.
     */
    @Override

    public List<Transaction> getTransactionsByType(String account, boolean positive) {
        if (!accountsToTransactions.containsKey(account)) {
            return new ArrayList<>();
        }

        List<Transaction> transactions = accountsToTransactions.get(account);
        List<Transaction> result = new ArrayList<>();
        for (Transaction transactiontt : transactions) {
            double wert = transactiontt.calculate();
            if ((wert > 0) && (positive == true)) {
                result.add(transactiontt);
            } else if ((wert < 0) && (positive == false)) {
                result.add(transactiontt);
            }

        }
        return result;

    }

    /**
     * Gson-Objekt, das für die Serialisierung (Java → JSON)
     * und Deserialisierung (JSON → Java) der Transaktionen zuständig ist.
     * <p>
     * Da die Klasse Transaction abstrakt ist, wird ein eigener
     * TypeAdapter (Transactiondeserial) registriert, damit Gson
     * zur Laufzeit erkennt, ob es sich um Payment, IncomingTransfer
     * oder OutgoingTransfer handelt.
     * <p>
     * setPrettyPrinting() sorgt für eine schön formatierte JSON-Datei.
     */


    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(Transaction.class, new Transactiondeserial())
            .setPrettyPrinting()
            .create();

    /**
     * Schreibt alle Transaktionen eines bestimmten Kontos in eine JSON-Datei.
     *
     * <p>Die Datei wird im Arbeitsverzeichnis der Bank gespeichert und heißt:
     * <b>"Konto &lt;account&gt;.json"</b></p>
     *
     * <p>Dabei wird die aktuelle Transaktionsliste des Kontos in ein JSON-Array
     * umgewandelt (Serialisierung). Gson übernimmt die Umwandlung der einzelnen
     * Transaction-Objekte.</p>
     *
     * @param account Name des Kontos, dessen Transaktionen gespeichert werden sollen.
     * @throws IOException Wenn das Konto nicht existiert oder die Datei nicht geschrieben werden kann.
     */
    private void writeAccount(String account) throws IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new IOException("Account " + account + " nicht existiert!");
        }

        File dir = new File(directoryName);
        if (!dir.exists()) {
            dir.mkdir();
        }

        String filee = directoryName + "/Konto " + account + ".json";

        List<Transaction> list = accountsToTransactions.get(account);
        Transaction[] arr = list.toArray(new Transaction[0]);

        FileWriter file = new FileWriter(filee);
        gson.toJson(arr, file);               //nimmt das Array wandelt zu Json um
        file.close();
    }


    /**
     * Liest alle JSON-Kontodateien aus dem Bankverzeichnis ein und lädt deren
     * Transaktionen in die interne Map {@code accountsToTransactions}.
     *
     * <p>Dies wird beim Erstellen einer neuen Bankinstanz automatisch im Konstruktor
     * ausgeführt. Jede Datei, die den Namensaufbau "Konto &lt;name&gt;.json" besitzt,
     * wird als eigenes Konto erkannt.</p>
     *
     * <p>Gson wandelt den JSON-Inhalt wieder in die entsprechenden Transaction-Objekte
     * um (Deserialisierung). Dank des registrierten {@link Transactiondeserial}
     * TypeAdapters erkennt Gson automatisch, ob es sich um Payment,
     * IncomingTransfer oder OutgoingTransfer handelt.</p>
     *
     * @throws IOException Wenn eine Datei nicht gelesen werden kann.
     */

    private void readAccounts() throws IOException {

        File read = new File(directoryName);
        if (!read.exists()) {
            return;
        }

        File[] allefiles = read.listFiles();
        if (allefiles == null) {
            return;
        }
        for (File file : allefiles) {
            String fileName = file.getName();
            if (fileName.startsWith("Konto ") && fileName.endsWith(".json")) {
                String account = fileName
                        .replace("Konto ", "")
                        .replace(".json", "");

                FileReader reader = new FileReader(file);
                Transaction[] arr = gson.fromJson(reader, Transaction[].class); // nehmen wir einzelne Klasse von reader
                reader.close();
                List<Transaction> transactions =
                        arr != null ? new ArrayList<>(Arrays.asList(arr)) : new ArrayList<>();

                accountsToTransactions.put(account, transactions);
            }


        }

    }

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Account " + account + " nicht existiert!");
        }
        accountsToTransactions.remove(account);

        File f = new File(directoryName + "/Konto " + account + ".json");

        if (f.exists()) {

            if (!f.delete()) {
                throw new IOException("Datei konnte nicht gelöscht werden: " + f.getAbsolutePath());
            }
        }


    }

    @Override
    public List<String> getAllAccounts(){

        return new ArrayList<>(accountsToTransactions.keySet());

    }


}







