package bank;

/**
 * Die abstrakte Klasse {@code Transaction} ist die Oberklasse für alle Transaktionen
 * im Bankprojekt (z. B. {@link Payment} oder {@link Transfer}).
 * <p>
 * Diese Klasse enthält die gemeinsamen Attribute wie Datum, Betrag und Beschreibung
 * und stellt die abstrakte Methode {@link #calculate()} bereit, die in den Unterklassen
 * individuell implementiert werden muss.
 * </p>
 *
 * @author Burak Mavili
 */
public abstract class Transaction implements CalculateBill{
    /**
     * Initialisierung aller grundlegenden Attribute
     * */
    private String date;
    private double amount;
    private String description;


    /**
     * Gibt das Datum der Transaktion zurück.
     * @return Datum als String
     */
    public String get_Date(){return date;}

    /**
     * Setzt das Datum der Transaktion.
     *
     * @param date neues Datum im Format TT.MM.JJJJ
     */
    public void set_Date(String date){this.date=date;}
    /**
     * Gibt den (ursprünglichen) Betrag der Transaktion zurück.
     *
     * @return Betrag als double
     */
    public double get_Amount(){return amount;}
    /**
     * Setzt den Betrag der Transaktion.
     *
     * @param amount neuer Betrag
     */
    public void set_Amount(double amount){this.amount=amount;}
    /**
     * Gibt die Beschreibung der Transaktion zurück.
     *
     * @return Beschreibung / Verwendungszweck
     */
    public String get_Description(){return description;}

    /**
     * Setzt die Beschreibung der Transaktion.
     *
     * @param description neue Beschreibung
     */
    public void set_Description(String description){this.description=description;}

    /**
     * Erstellt eine Transaktion mit Datum, Betrag und Beschreibung.
     *
     * @param date Datum der Transaktion
     * @param amount Betrag der Transaktion
     * @param description Beschreibung der Transaktion
     */
    protected Transaction(String date,double amount,String description){
        this.date=date;
        set_Amount(amount);
        this.description=description;
    }



    /**
     * Gibt eine Textdarstellung der Transaktion zurück.
     *   Der angezeigte Betrag entspricht dem Wert aus {@link #calculate()}.
     * @return Text mit Datum,berechneter Betrag und Beschreibung
     */
    @Override
    public String toString(){return
            " Date: "+this.get_Date()+ "  ,Amount: "+ calculate()+" ,Description: "+this.get_Description();
    }
    /**
     * Vergleicht diese Transaktion mit einem anderen Objekt.
     * Zwei Transaktionen gelten als gleich, wenn Datum, Betrag
     * und Beschreibung übereinstimmen.
     *
     * @param obj das zu vergleichende Objekt
     * @return {@code true}, wenn beide Transaktionen gleich sind, sonst {@code false}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction pt= (Transaction) obj;
        return this.amount==pt.amount&&this.date.equals(pt.date) && this.description.equals(pt.description);
    }
    // Objects.equals(this.date, pt.date) &&
    //           Objects.equals(this.description, pt.description);
    /**
     * Copy-Konstruktor: erstellt eine neue Transaktion mit den Werten
     * einer bereits vorhandenen Transaktion.
     *
     * @param X die zu kopierende Transaktion
     */
    protected Transaction(Transaction X){
        set_Date(X.get_Date());
        set_Amount(X.get_Amount());
        set_Description(X.get_Description());

    }


}
