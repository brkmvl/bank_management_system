import bank.*;
import bank.exceptions.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrivateBankTest {
    private PrivateBank bank;
    private String testDir = "test_privatebank";

    private Payment payment;
    private IncomingTransfer incomingTransfer;
    private OutgoingTransfer outgoingTransfer;

    @BeforeEach
    void setUp() throws IOException {
        bank = new PrivateBank("JUnit Bank", 0.1, 0.1, testDir);

        payment = new Payment("10.01.2024", 1000, "Einzahlung", 0.1, 0.1);
        incomingTransfer = new IncomingTransfer("11.01.2024", 500, "Hilfe", "Oma", "Burak");
        outgoingTransfer = new OutgoingTransfer("12.01.2024", 200, "Rechnung", "Burak", "Strom");
    }

    @AfterEach
    void tearDown() {
        File file = new File(testDir);
        if (file.exists()) {
            for (File f : file.listFiles()) {
                f.delete();
            }
        }
    }


    @Test
    void TestKonstrukt() throws IOException {

        assertEquals("JUnit Bank", bank.get_Name());
        assertEquals(0.1, bank.get_incomingInterest());
        assertEquals(0.1, bank.get_outgoingInterest());
        assertTrue(new File(testDir).exists());
        assertEquals(testDir, bank.getDirectoryName());
         //File dir = new File(testDir);
         //assertTrue(dir.exists());
    }

    @Test
    void TestKopyKonstruktion() throws IOException {
        PrivateBank privateBank = new PrivateBank(bank);
        assertEquals("JUnit Bank", privateBank.get_Name());
        assertEquals(0.1, privateBank.get_incomingInterest());
        assertEquals(0.1, privateBank.get_outgoingInterest());
        assertTrue(new File(privateBank.getDirectoryName()).exists());
        assertEquals(testDir, privateBank.getDirectoryName());
    }



    @Test
    void testSetIncomingInterest_Valid() {
        Payment p = new Payment("10.01.2024", 100, "OK", 0.1, 0.1);
        assertDoesNotThrow(() -> p.set_incomingInterest(0.5));
        assertEquals(0.5, p.get_incomingInterest());
    }

    @Test
    void testSetIncomingInterest_Invalid() {
        Payment p = new Payment("10.01.2024", 100, "OK", 0.1, 0.1);

        assertThrows(TransactionAttributeException.class, () -> p.set_incomingInterest(-0.1));
        assertThrows(TransactionAttributeException.class, () -> p.set_incomingInterest(1.2));
    }

    @Test
    void testSetOutgoingInterest_Invalid() {
        Payment p = new Payment("10.01.2024", 100, "OK", 0.1, 0.1);

        assertThrows(TransactionAttributeException.class, () -> p.set_outgoingInterest(-0.5));
        assertThrows(TransactionAttributeException.class, () -> p.set_outgoingInterest(2.0));
    }

    @Test
    void testCreateAccount() throws AccountAlreadyExistsException, IOException {
        bank.createAccount("BurakMvl");

        assertTrue(bank.getTransactions("BurakMvl").isEmpty());

        File file = new File(testDir + "/Konto BurakMvl.json");
        assertTrue(file.exists());
    }

    @Test
    void testCreateAccountDuplicate() throws Exception {
        bank.createAccount("Burak");
        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank.createAccount("Burak");
        });
    }

    @Test
    void testCreateAccountIOException() {
        assertThrows(IOException.class, () -> {
            PrivateBank badBank = new PrivateBank("Numan", 0.1, 0.1, "/root/protected_dir");
            badBank.createAccount("Burak");
        });
    }


    @Test
    void testCreateAccountWithListSuccess() throws Exception {
        List<Transaction> list = new ArrayList<>();
        list.add(payment);
        list.add(incomingTransfer);

        bank.createAccount("TestAcc", list);

        assertEquals(2, bank.getTransactions("TestAcc").size());

        File file = new File(testDir + "/Konto TestAcc.json");
        assertTrue(file.exists());
    }


    @Test
    void testCreateAccountList_DuplicateAccount() throws Exception {
        List<Transaction> list = new ArrayList<>();
        list.add(payment);

        bank.createAccount("Adam", list);

        assertThrows(AccountAlreadyExistsException.class, () -> {
            bank.createAccount("Adam", list);
        });
    }

    @Test
    void testCreateAccountList_DuplicateTransaction() {
        List<Transaction> list = new ArrayList<>();
        list.add(payment);
        list.add(payment); // aynı objeyi iki kere ekliyoruz

        assertThrows(TransactionAlreadyExistException.class, () -> {
            bank.createAccount("Acc1", list);
        });
    }

    @Test
    void testCreateAccountList_ProblembeimPayment() {
        Payment bad = new Payment("01.01.2024", 200, "Fehler", 1.5, 0.2);
        List<Transaction> list = new ArrayList<>();
        list.add(bad);

        assertThrows(TransactionAttributeException.class, () -> {
            bank.createAccount("FehlerAcc", list);
        });
    }

    @Test
    void testCreateAccountList_ProblembeimTransfer() {
        Transfer bad = new Transfer("01.01.2024", -50, "Fehler", "A", "B");
        List<Transaction> list = new ArrayList<>();
        list.add(bad);

        assertThrows(TransactionAttributeException.class, () -> {
            bank.createAccount("FehlerAcc2", list);
        });
    }


    @Test
    void testAddTransactionSuccess() throws Exception {
        bank.createAccount("BurakMvl");
        assertDoesNotThrow(() -> bank.addTransaction("BurakMvl", payment));

        assertThrows(TransactionAlreadyExistException.class, () -> {
            bank.addTransaction("BurakMvl", payment);
        });
    }

    @Test
    void testAddTransaction_notexist() throws Exception {
        bank.createAccount("BurakMvl");
        assertThrows(AccountDoesNotExistException.class, () -> {
            bank.addTransaction("emir", payment);
        });

    }

    @Test
    void testAddTransaction_InvalidPaymentInterest() throws Exception {
        bank.createAccount("Burak");

        Payment bad = new Payment("01.01.2024", 200, "Fehler", 2.0, 0.2);

        assertThrows(TransactionAttributeException.class, () -> {
            bank.addTransaction("Burak", bad);
        });
    }


    @Test
    void testRemoveTransaction_AccountDoesNotExist() {
        assertThrows(AccountDoesNotExistException.class, () -> {
            bank.removeTransaction("NichtDa", payment);
        });
    }

    @Test
    void testRemoveTransaction_TransactionDoesNotExist() throws Exception {
        bank.createAccount("Burak");

        /*assertThrows(TransactionDoesNotExistException.class, () -> {
            bank.removeTransaction("Burak", payment);
        });*/


        assertThrows(TransactionDoesNotExistException.class, () -> {
            bank.removeTransaction("Burak", payment);
        });





    }

    @Test
    void testRemoveTransaction_FileUpdated() throws Exception {
        bank.createAccount("Burak");
        bank.addTransaction("Burak", payment);
        bank.removeTransaction("Burak", payment);
        File file = new File(testDir + "/Konto Burak.json");
        assertTrue(file.exists());
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Transactiondeserial())
                .create();
        Transaction[] arr = gson.fromJson(new FileReader(file), Transaction[].class);
        assertEquals(0, arr.length);
    }

    @Test
    void testContainsTransaction() throws Exception {
        bank.createAccount("Burak");
        // Henüz eklenmedi → false
        assertFalse(bank.containsTransaction("Burak", payment));
        bank.addTransaction("Burak", payment);
        assertTrue(bank.containsTransaction("Burak", payment));
        assertFalse(bank.containsTransaction("YokHesap", payment));
    }

    @Test
    void testGetAccountBalance() throws Exception {
        assertEquals(0, bank.getAccountBalance("Burak"));//haben wir kein Konto
        bank.createAccount("Burak");
        // Örnek bir ödeme ekle
        bank.addTransaction("Burak", payment); // 1000 - %10 = 900
        double expected = payment.calculate();
        assertEquals(expected, bank.getAccountBalance("Burak"));
    }

    @Test
    void testGetTransaction() throws IOException {
        assertTrue(bank.getTransactions("Numan").isEmpty());
        bank.createAccount("Brk");
        assertTrue(bank.getTransactions("Brk").isEmpty());
        bank.addTransaction("Brk", payment);
        assertEquals(1, bank.getTransactions("Brk").size());
        assertTrue(bank.getTransactions("Brk").contains(payment));

    }


    @Test
    void testGetTransactionsSorted() throws Exception {
        bank.createAccount("Burak");

        Payment p1 = new Payment("01.01.2024", 100, "A", 0.1, 0.1);  // calculate = 90
        Payment p2 = new Payment("02.01.2024", 200, "B", 0.1, 0.1);  // calculate = 180
        OutgoingTransfer p3 = new OutgoingTransfer("03.01.2024", 50, "C", "X", "Y"); // calculate = -50
        bank.addTransaction("Burak", p1);
        bank.addTransaction("Burak", p2);
        bank.addTransaction("Burak", p3);

        List<Transaction> asc = bank.getTransactionsSorted("Burak", true);
        assertEquals(-50, asc.get(0).calculate());
        assertEquals(90, asc.get(1).calculate());
        assertEquals(180, asc.get(2).calculate());


        List<Transaction> desc = bank.getTransactionsSorted("Burak", false);
        assertEquals(180, desc.get(0).calculate());
        assertEquals(90, desc.get(1).calculate());
        assertEquals(-50, desc.get(2).calculate());
    }

    @Test
    void testGetTransactionsByType() throws Exception {
        bank.createAccount("Burak");
        Payment p1 = new Payment("01.01.2024", 100, "A", 0.1, 0.1);   // pozitif: 90
        OutgoingTransfer p2 = new OutgoingTransfer("02.01.2024", 50, "B", "X", "Y"); // negatif: -50
        bank.addTransaction("Burak", p1);
        bank.addTransaction("Burak", p2);
        //fuer positive
        List<Transaction> positives = bank.getTransactionsByType("Burak", true);
        assertEquals(1, positives.size());
        assertEquals(p1, positives.get(0));
        // fuer negative
        List<Transaction> negatives = bank.getTransactionsByType("Burak", false);
        assertEquals(1, negatives.size());
        assertEquals(p2, negatives.get(0));

        // irgendein konto
        assertTrue(bank.getTransactionsByType("X", true).isEmpty());
    }


    @Test
    void testWriteAccount() throws Exception {
        bank.createAccount("Burak");
        bank.addTransaction("Burak", payment); // Payment zaten setup()’ta hazır

        File file = new File(testDir + "/Konto Burak.json");
        assertTrue(file.exists());

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Transactiondeserial())
                .create();

        Transaction[] arr = gson.fromJson(new FileReader(file), Transaction[].class);

        assertEquals(1, arr.length);
        assertEquals(payment, arr[0]);
    }

    @Test
    void testReadAccounts() throws Exception {

        File dir = new File(testDir);
        if (!dir.exists()) dir.mkdir();

        String fileName = testDir + "/Konto Burak.json";

        Transaction[] arr = {
                new Payment("01.01.2023", 100, "Gehalt", 0.1, 0.1),
                new IncomingTransfer("02.01.2023", 200, "TF", "Ali", "Burak"),
                new OutgoingTransfer("03.01.2023", 50, "Miete", "Burak", "Vermieter")
        };
        // Gson serialize (custom serializer KULLANILMALI)
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Transaction.class, new Transactiondeserial())
                .setPrettyPrinting()
                .create();

        FileWriter writer = new FileWriter(fileName);
        gson.toJson(arr, writer);
        writer.close();

        PrivateBank bank2 = new PrivateBank("JUnit Bank", 0.1, 0.1, testDir);


        assertTrue(bank2.getTransactions("Burak") != null);
        assertEquals(3, bank2.getTransactions("Burak").size());


        assertTrue(bank2.getTransactions("Burak").get(0) instanceof Payment);
        assertTrue(bank2.getTransactions("Burak").get(1) instanceof IncomingTransfer);
        assertTrue(bank2.getTransactions("Burak").get(2) instanceof OutgoingTransfer);


        assertEquals("Gehalt", bank2.getTransactions("Burak").get(0).get_Description());
        assertEquals(200, bank2.getTransactions("Burak").get(1).get_Amount());
        assertEquals(50, bank2.getTransactions("Burak").get(2).get_Amount());
    }

    @Test
    void testEquals() throws Exception {

        PrivateBank b1 = new PrivateBank("BankA", 0.1, 0.2, "dirA");
        PrivateBank b2 = new PrivateBank("BankA", 0.1, 0.2, "dirA");
        assertEquals(b1, b1);
        assertEquals(b1, b2);
        assertNotEquals(b1, bank);
        ;
    }

    @Test
    void testToString() throws Exception {
        PrivateBank bank = new PrivateBank("TestBank", 0.1, 0.2, testDir);
        String text = bank.toString();
        assertTrue(text.contains("TestBank"));
        assertTrue(text.contains("IncomingInterest: 0.1"));
        assertTrue(text.contains("OutgoingInterest: 0.2"));
        assertTrue(text.contains("Transactionen"));
    }


}
