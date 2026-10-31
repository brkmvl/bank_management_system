import bank.IncomingTransfer;
import bank.OutgoingTransfer;
import bank.Payment;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
public class TransferTest {
    private Transfer transfer;
    private OutgoingTransfer out;
    private IncomingTransfer intt;


    @BeforeEach
    public void setUp()  {

        transfer=new Transfer("29.11.2025",200,"Paypal","Burak","Ert");
        out=new OutgoingTransfer("21.05.2024",100,"Amazon","Burak","Jeff");
        intt=new IncomingTransfer("29.11.2025",200,"Paypal","mvl","Burak");
    }

    @Test
    public void Testkonstruktor() {
        assertNotEquals("30.11.2025", transfer.get_Date());
        assertEquals(200, transfer.get_Amount());
        assertEquals("Paypal", transfer.get_Description());
        assertEquals("Burak", transfer.get_sender());
        assertEquals("Ert", transfer.get_recipient());
    }



    @Test
    public void TestKOPYkonstruktor() {
        Transfer kopy=new Transfer(transfer);

        assertEquals(transfer,kopy);
        assertNotSame(transfer, kopy);

        assertEquals(transfer.get_Date(), kopy.get_Date());
        assertEquals(transfer.get_Amount(), kopy.get_Amount());
        assertEquals(transfer.get_Description(), kopy.get_Description());
        assertEquals(transfer.get_sender(), kopy.get_sender());
        assertEquals(transfer.get_recipient(), kopy.get_recipient());
    }

    @ParameterizedTest
    @ValueSource(ints={200,100,300})
    void TestCalculatetransfer(int cal){
        Transfer calc=new Transfer("29.11.2025",cal,"Paypal","Burak","Ert");
        assertEquals(cal,calc.calculate());
    }

    @ParameterizedTest
    @ValueSource(ints={200,100,300})
    void Testincomtransfer(int cal){
        IncomingTransfer incoming=new IncomingTransfer("29.11.2025",cal,"Paypal","Burak","Ert");
        assertEquals(cal,incoming.calculate());
    }

    @ParameterizedTest
    @ValueSource(ints={200,100,300})
    void Testoutgoingtransfer(int cal){
        OutgoingTransfer outgoingTransfer=new OutgoingTransfer("29.11.2025",cal,"Paypal","Burak","Ert");
        assertEquals(-cal,outgoingTransfer.calculate());
    }
    @Test
    void Testequals() {
        assertEquals(transfer,transfer);
        assertNotEquals(transfer,intt);
        Transfer test=new Transfer(transfer);
        assertEquals(transfer,test);
        Transfer test2=new Transfer("10.11.2025",200,"Paypal","Burak","Ert");
        assertNotEquals(transfer,test2);
        IncomingTransfer test3=new IncomingTransfer("10.11.2025",200,"Paypal","Burak","Ert");
        assertNotEquals(test2,test3);

    }

    @Test
    void TesttoString(){
        String test=transfer.toString();
        assertTrue(test.contains("29.11.2025"));
        assertTrue(test.contains("Paypal"));
        assertTrue(test.contains("Burak"));
        assertTrue(test.contains("Ert"));


    }





}
