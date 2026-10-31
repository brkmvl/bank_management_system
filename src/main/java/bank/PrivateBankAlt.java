package bank;

import bank.exceptions.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class PrivateBankAlt implements Bank {

    private String name;
    private double incomingInterest;
    private double outgoingInterest;
    private Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();
    private String directoryName;

    public String getDirectoryName() {
        return directoryName;
    }


    public String get_Name() {
        return name;
    }

    public void set_Name(String name) {
        this.name = name;
    }

    public double get_incomingInterest() {
        return incomingInterest;
    }

    public void set_incomingInterest(double incomingInterest) {
        if (incomingInterest < 0 || incomingInterest > 1) {
            throw new TransactionAttributeException("Incoming soll sich  zwischen 0 and 1 befinden.");
        } else {
            this.incomingInterest = incomingInterest;
        }
    }


    public double get_outgoingInterest() {
        return outgoingInterest;
    }

    public void set_outgoingInterest(double outgoingInterest) {
        if (outgoingInterest < 0 || outgoingInterest > 1) {
            throw new TransactionAttributeException("Outgoing soll sich  zwischen 0 and 1 befinden.");
        } else {
            this.outgoingInterest = outgoingInterest;
        }
    }

    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest) {
        set_Name(name);
        set_incomingInterest(incomingInterest);
        set_outgoingInterest(outgoingInterest);
    }

    public PrivateBankAlt(PrivateBankAlt X) {
        set_Name(X.get_Name());
        set_outgoingInterest(X.get_outgoingInterest());
        set_incomingInterest(X.get_incomingInterest());
    }

    @Override
    public String toString() {
        return "PrivateBank ->  " + "Name: " + this.get_Name() + ", IncomingInterest: " + this.get_incomingInterest() + ", OutgoingInterest: " + this.get_outgoingInterest() + ", Transactionen: " + accountsToTransactions;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        PrivateBankAlt other = (PrivateBankAlt) obj;

        return this.name.equals(other.name) &&
                this.incomingInterest == other.incomingInterest &&
                this.outgoingInterest == other.outgoingInterest &&
                this.accountsToTransactions.equals(other.accountsToTransactions);
    }

    @Override
    public void createAccount(String account) throws AccountAlreadyExistsException {
        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Konto  schon existiert");
        } else {
            accountsToTransactions.put(account, new ArrayList<>());
        }
    }

    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {

        if (accountsToTransactions.containsKey(account)) {
            throw new AccountAlreadyExistsException("Konto ist schon vorhanden!");
        }

        List<Transaction> neueTransaction = new ArrayList<>();

        //for (Transaction current : transactions)
        for (int i = 0; i < transactions.size(); i++) {
            Transaction current = transactions.get(i);

            if (neueTransaction.contains(current)) {
                throw new TransactionAlreadyExistException("Transaktion ist schon in der Liste ");
            }

            if (current instanceof Payment paymentObject) {

                // i. Kendi Niteliklerini Kontrol Etme (Faiz aralığı)
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
    }

    @Override
    public void addTransaction(String account, Transaction transaction) throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {
        if (!accountsToTransactions.containsKey(account)) {
            throw new AccountDoesNotExistException("Konto ist schon vorhanden!(addTransaction)");
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
        } else if (transaction instanceof Transfer transferRef) {
            if (transferRef.get_Amount() < 0) {
                throw new TransactionAttributeException("Ungiltige Betrag im Transfer!(ADDTRANSACTION)");
            }

        }
        neueaccountT.add(transaction);
    }

    @Override
    public void removeTransaction(String account, Transaction transaction) throws AccountDoesNotExistException, TransactionDoesNotExistException {
        if (!(accountsToTransactions.containsKey(account))) {
            throw new AccountDoesNotExistException("Es gibt keine Konto mit diesem Name->" + account);
        }
        List<Transaction> removeT = accountsToTransactions.get(account);
        if (!(removeT.contains(transaction))) {
            throw new TransactionDoesNotExistException("Die  Transaktion wurde in diesem Konto nicht gefunden!");
        }
        removeT.remove(transaction);

    }

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

    @Override
    public double getAccountBalance(String account) /* throws AccountDoesNotExistException */ {

        // 1. Hesap Varlığı Kontrolü
        if (!accountsToTransactions.containsKey(account)) {
            // Arayüz izin veriyorsa HATA fırlatın. İzin vermiyorsa: return 0.0;
            throw new AccountDoesNotExistException("Konto ist nicht vorhanden!");
        }

        double summe = 0.0;
        List<Transaction> transactionssumme = accountsToTransactions.get(account);


        for (Transaction t : transactionssumme) {
            if (t instanceof Payment) {
                summe += t.calculate();
            } else if (t instanceof Transfer transferRef) {
                if (transferRef.get_sender().equals(account)) {

                    summe -= transferRef.calculate();
                } else {

                    summe += transferRef.calculate();
                }
            }
        }

        return summe;
    }

    @Override

    public  List<Transaction> getTransactions(String account){
        if(!accountsToTransactions.containsKey(account)){return new ArrayList<>();}

        return accountsToTransactions.get(account);

    }
    @Override
    public   List<Transaction> getTransactionsSorted(String account, boolean asc){

        if(!accountsToTransactions.containsKey(account)){return new ArrayList<>();}

        List<Transaction> originalList = accountsToTransactions.get(account);
        List<Transaction> sortedList = new ArrayList<>(originalList);
        sortedList.sort(Comparator.comparing(Transaction::calculate));
        if (!asc) {
            Collections.reverse(sortedList);
        }
        return sortedList;
    }

    @Override

    public  List<Transaction> getTransactionsByType(String account, boolean positive) {
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








