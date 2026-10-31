 package bank;

import bank.exceptions.*;

import java.io.IOException;
import java.util.List;

/**
 * Interface for a generic bank. Provides multiple methods to handle the interaction between
 * accounts and transactions. Es beschreibt, wie eine Bank funktionieren soll – aber implementiert nichts.
 */
public interface Bank {

    /**
     * Erstellt ein neues Konto mit dem angegebenen Namen.
     * Wenn ein Konto mit diesem Namen bereits existiert, muss eine
     * AccountAlreadyExistsException geworfen werden.
     *
     * Adds an account to the bank.
     *
     * @param account the account to be added
     * @throws AccountAlreadyExistsException if the account already exists
     */
    void createAccount(String account) throws AccountAlreadyExistsException, IOException;

    /**
     * Adds an account (with specified transactions) to the bank.
     * Important: duplicate transactions must not be added to the account!
     *
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException    if the account already exists 	•	Konto existiert schon
     * @throws TransactionAlreadyExistException if the transaction already exists 	•	Eine Transaktion kommt doppelt vor
     * @throws TransactionAttributeException    if the validation check for certain attributes fail  	•	Eine Transaktion ist fehlerhaft (z. B. negativer Betrag)
     */
    void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException;

    /**
     * Adds a transaction to an already existing account.
     *
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if the transaction already exists  •		•	Transaktion existiert bereits
     * @throws AccountDoesNotExistException     if the specified account does not exist    	•	Konto existiert nicht
     * @throws TransactionAttributeException    if the validation check for certain attributes fail  	•	Attribute sind ungültig
     */
    void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException;

    /**
     * Removes a transaction from an account. If the transaction does not exist, an exception is
     * thrown.
     *
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException     if the specified account does not exist  •	Konto existiert nicht
     * @throws TransactionDoesNotExistException if the transaction cannot be found•	Transaction  nicht gefunden
     */
    void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException;

    /**
     * Checks whether the specified transaction for a given account exists.
     *
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     */
    boolean containsTransaction(String account, Transaction transaction);

    /**
     * Calculates and returns the current account balance.
     *
     * @param account the selected account
     * @return the current account balance
     */
    double getAccountBalance(String account);

    /**
     * Returns a list of transactions for an account.
     *
     * @param account the selected account
     * @return the list of all transactions for the specified account
     */
    List<Transaction> getTransactions(String account);

    /**
     * Returns a sorted list (-> calculated amounts) of transactions for a specific account. Sorts the list either in ascending or descending order
     * (or empty).
     *
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return the sorted list of all transactions for the specified account
     */
    List<Transaction> getTransactionsSorted(String account, boolean asc);

    /**
     * Returns a list of either positive or negative transactions (-> calculated amounts).
     *
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed
     * @return the list of all transactions by type
     */
    List<Transaction> getTransactionsByType(String account, boolean positive);

    void deleteAccount(String account) throws AccountDoesNotExistException, IOException;
    List<String> getAllAccounts();




}
