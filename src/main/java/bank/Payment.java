package bank;

import bank.exceptions.TransactionAttributeException;

/**
 * Die Klasse {@code Payment} ist für Einzahlungen und Auszahlungen gedacht.
 * <p>
 * Mit dieser Klasse können grundlegende Informationen über eine Zahlung
 * wie Datum, Betrag, Verwendungszweck sowie die Zinsen bei der Einzahlung
 * und Auszahlung gespeichert werden.
 * </p>
 * Sie erbt von der abstrakten Oberklasse {@link Transaction}.
 *
 * @author Burak Mavili 3714922
 */
public class Payment extends Transaction{

    //private String date;   //Datum der Zahlung(DD.MM.YY)
    //private double amount;  //Betrag der Zahlung
    //private String description;*/ //Verwendungszweck der Zahlung

    /*Zinsen bei der Einzahlung
    Fehler wenn dieser Wert nicht zwischen 0 und 1 steht
    */



    /**
     * Initialisierung der privaten Attribute incomingInterest und outgoingInterest,
     * welche die Zinsen für Einzahlungen und Auszahlungen in der Klasse Payment darstellen.
     */
    private double incomingInterest;

    /*Ziinsen bei der Auszahlung
     *     Fehler wenn dieser Wert nicht zwischen 0 und 1 steht
     * */
    private double outgoingInterest;

    /*public double get_Amount() {
        return amount;} *//*

   /*public void set_Amount(double amount) {
        this.amount = amount;  }*/
    /* public String get_Date(){return date;}*/
    /*public void set_Date(String date){
        this.date=date;}*/
    /* public  String get_Description(){return description;}*/
    /* public  void set_Description(String description){this.description=description;}*/
    /**
     * Gibt den Zins für Einzahlungen zurück.
     *
     * @return Zinswert für Einzahlungen
     */
    public double get_incomingInterest(){return incomingInterest;}

    /**
     * Setzt den Zins für Einzahlungen.
     * Wenn der Wert nicht zwischen 0 und 1 liegt, wird eine Fehlermeldung ausgegeben.
     *
     * @param incomingInterest neuer Zinswert
     */
    public void set_incomingInterest(double incomingInterest){
        if(incomingInterest>1.0||incomingInterest<0.0){throw new TransactionAttributeException("Zinssatz muss zwischen 0 und 1 liegen.");}
        else{
            this.incomingInterest=incomingInterest;}
    }
    /**
     * Gibt den Zins für Auszahlungen zurück.
     *
     * @return Zinswert für Auszahlungen
     */
    public double get_outgoingInterest(){return outgoingInterest;}

    /**
     * Setzt den Zins für Auszahlungen.
     * Wenn der Wert nicht zwischen 0 und 1 liegt, wird eine Fehlermeldung ausgegeben.
     *
     * @param outgoingInterest neuer Zinswert
     */
    public void set_outgoingInterest(double outgoingInterest){
        if (outgoingInterest<0||outgoingInterest>1){throw new TransactionAttributeException("Outgoing Interest must be between 0 and 1");}
        else{
            this.outgoingInterest=outgoingInterest;}}
    /**
     * Überschreibt die Methide(@code calculate()) der Oberklasse und berechnet den Endbetrag der Zahlung.
     * <p>
     * Der Betrag hängt von der Art der Transaktion ab
     * (Einzahlung oder Auszahlung) und berücksichtigt die Zinsen.
     * </p>
     *
     * @return berechneter Endbetrag
     */

    @Override
    public double calculate() {
        double endwert=get_Amount();
        if(get_Amount()>0){
            endwert=get_Amount()-(get_Amount()*get_incomingInterest());}
        else if (endwert<0){
            endwert=get_Amount()+(get_Amount()*get_outgoingInterest());
        }
        else {
            endwert = get_Amount();
        }

        return endwert;
    }

    /**
     * Gibt eine Textdarstellung des Payment-Objekts zurück.
     *
     * @return Text mit Datum, Betrag, Beschreibung und Zinsen
     */
    @Override
    public String toString() {
        return  "Payment ->"+
                super.toString()+
                ", incomingInterest=" + get_incomingInterest() +
                ", outgoingInterest=" + get_outgoingInterest();
    }

    /**
     * Vergleicht dieses Payment-Objekt mit einem anderen Objekt.
     * Zwei Objekte sind gleich, wenn alle geerbten Werte und die Zinsen identisch sind.
     *
     * @param obj das zu vergleichende OBERKLASSE Objekt
     * @return true, wenn beide Payments gleich sind, sonst false
     */
    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Payment pt = (Payment) obj;

        return (this.incomingInterest==pt.incomingInterest)&&(this.outgoingInterest==pt.outgoingInterest);
    }


   /* @Override
    public boolean equals(Objekt obj){
        if(!super.equals(obj)){return false;}
        if(this.getClass()!=obj.getClass()){return false}
        Payment pt=(Payment)obj;
        return this.incominginterest==onj.incominginterest&&this.outgoinginterest&&obj.outgoing intreres

    }*/

    /**
     * Erstellt ein Payment-Objekt mit Datum, Betrag und Beschreibung.
     *
     * @param date Datum der Zahlung
     * @param amount Betrag der Zahlung
     * @param description Beschreibung oder Verwendungszweck
     */
    public Payment(String date, double amount, String description){
        super(date, amount, description);

    }
    /**
     * Erstellt ein Payment-Objekt mit allen Attributen, einschließlich der Zinsen.
     *
     * @param date Datum der Zahlung
     * @param amount Betrag der Zahlung
     * @param description Beschreibung der Zahlung
     * @param incomingInterest Zins für Einzahlungen
     * @param outgoingInterest Zins für Auszahlungen
     */
    public Payment(String date,double amount,String description,double incomingInterest,double outgoingInterest) {
        this(date, amount, description);
        //set_incomingInterest(incomingInterest);
        //set_outgoingInterest(outgoingInterest);
        this.incomingInterest = incomingInterest;   // setter YOK
        this.outgoingInterest = outgoingInterest;
    }

    /**
     * Erstellt ein neues Payment-Objekt mit den Werten eines anderen Payment-Objekts.
     *
     * @param X das Payment-Objekt, das kopiert wird
     */
    public Payment(Payment X){
    /*set_Date(X.get_Date());
    set_Amount(X.get_Amount());
    set_Description(X.get_Description());*/
        super(X);
        set_incomingInterest(X.get_incomingInterest());
        set_outgoingInterest(X.get_outgoingInterest());

    }



/*public void  printObjekt(){
      System.out.println("Date ist am: "+get_Date());
    System.out.println("Betrag ist : "+get_Amount());
    System.out.println("Verwendungszweck ist : "+get_Description());
    System.out.println("Zinsen bei der Einzahlung "+get_incomingInterest());
    System.out.println("Zinsen bei der Auszahhlung"+get_outgoingInterest());
    System.out.println(" ");


}*/

}

