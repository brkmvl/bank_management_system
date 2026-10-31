package bank;
/**
 * Die Klasse {@code Transfer} repräsentiert eine Überweisung zwischen zwei Beteiligten.
 * <p>
 * Sie erweitert die allgemeine Transaktion ({@link Transaction}) um die Angaben
 * zum Sender und zum Empfänger. Der Betrag muss hier positiv sein.
 * </p>
 *
 * @author Burak Mavili
 */
public class Transfer extends Transaction
{
    // private String date;//Datum der Zahlung
    //private double amount;//Betrag der Zahlung
    //private String description;//Verwendungszweck der Zahlung
    /**
     * Initialisierung der spezifischen Attribute sender und recipient,
     * welche die an einer Überweisung beteiligten Personen darstellen.
     */
    private String sender;//  Name oder Kontoinhaber, der die Überweisung sendet.
    private String recipient;




    // public String get_Date(){return date;}
    //public void set_Date(String date){this.date=date;}
    //public  double get_Amount(){return super.get_Amount(); }
    /**
     * Überschreibt die Methode {@code set_Amount} der Oberklasse.
     * Der Betrag wird nur gesetzt, wenn er größer als 0 ist.
     * Andernfalls wird eine Fehlermeldung ausgegeben.
     *
     * @param amount zu überweisender Betrag (muss positiv sein)
     */
    @Override
    public  void set_Amount(double amount){
        if(amount<=0){
            System.out.println("Fehler der Betrag muss Positiv sein");}
        else{
            super.set_Amount(amount);}}
    //public String get_Description(){return description;}
    //public void set_Description(String description){this.description=description;}
    /**
     * Gibt den Sender der Überweisung zurück.
     *
     * @return Sender als String
     */
    public String get_sender(){return sender;}
    /*
     * Sender setzen
     * */
    /**
     * Setzt den Sender der Überweisung.
     *
     * @param sender Name oder Kontoinhaber.
     */
    public void set_sender(String sender){this.sender=sender;}

    /**
     * Gibt den Empfänger der Überweisung zurück.
     *
     * @return Empfänger als String
     */
    public String get_recipient(){return recipient;}
    /*
     * Empfaenger setzen
     * */

    /**
     * Setzt den Empfänger der Überweisung.
     *
     * @param recipient Name oder Kontoinhaber, der das Geld empfängt
     */
    public void set_recipient(String recipient){this.recipient=recipient;}
    /**
     * Überschreibt die Methide(@code calculate()) der Oberklasse und berechnet den Endbetrag der Zahlung.
     * <p>
     * Der Betrag hängt von der Art der Transaktion ab
     * </p>
     *
     * @return berechneter Endbetrag
     */
    @Override
    public double calculate() {
        return get_Amount();
    }
    /**
     * Überschreibt die Methode {@link Transaction#toString()}.
     * <p>
     * Gibt eine Textdarstellung des {@code Transfer}-Objekts zurück,
     * die alle relevanten Informationen wie Datum, Betrag, Beschreibung,
     * Sender und Empfänger enthält.
     * </p>
     *
     * @return Text mit allen Attributen der Überweisung
     */
    @Override
    public String toString(){return
            " Transfer ->  "+ super.toString()+" ,Sender:"+get_sender()+" ,Recipient: "+get_recipient();}


    /**
     * Erstellt ein Transfer-Objekt mit Datum, Betrag und Beschreibung.
     *
     * @param date Datum der Zahlung
     * @param amount Betrag der Zahlung
     * @param description Beschreibung oder Verwendungszweck
     */
    public Transfer(String date,double amount,String description){
        super(date,amount,description);

    }

    /**
     * Erstellt ein neues Transfer-Objekt mit allen relevanten Attributen.
     * <p>
     * Zusätzlich zu den allgemeinen Transaktionsdaten werden hier der Sender
     * und der Empfänger der Überweisung gesetzt.
     * </p>
     *
     * @param date Datum der Überweisung
     * @param amount Betrag der Überweisung
     * @param description Verwendungszweck
     * @param sender  Kontoinhaber
     * @param recipient  Kontoinhaber, der das Geld empfängt
     */
    public  Transfer(String date,double amount,String description,String sender,String recipient){

        this(date, amount, description);
        set_sender(sender);
        this.recipient=recipient;
    }
    /**
     * Erstellt ein neues Transfer-Objekt mit den Werten eines anderen Payment-Objekts.
     *
     * @param X das Transfer-Objekt, das kopiert wird
     */
    public Transfer(Transfer X){
        super(X);
        set_sender(X.get_sender());
        set_recipient(X.get_recipient());
    }
    /**
     * Vergleicht dieses Transfer-Objekt mit einem anderen Objekt.
     * Zwei Objekte sind gleich, wenn alle geerbten Werte und die Zinsen identisch sind.
     *
     * @param obj das zu vergleichende OBERKLASSE Objekt
     * @return true, wenn beide Payments gleich sind, sonst false
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (this.getClass() != obj.getClass()) return false;

        Transfer pt = (Transfer) obj;
        return sender.equals(pt.sender) && recipient.equals(pt.recipient);
    }
    /*
     * Gibt alle Attributen von dem Objekt aus.
     *
     * */

   /* public void  printObjektt(){
        System.out.println("Date ist am: "+get_Date());
        System.out.println("Betrag ist : "+get_Amount());
        System.out.println("Verwendungszweck ist : "+get_Description());
        System.out.println("Sender: "+get_sender());
        System.out.println("Empfaenger: "+get_recipient());
        System.out.println(" \n ");


    }*/




}
