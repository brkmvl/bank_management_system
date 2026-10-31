import bank.Payment;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;


public class PaymentTest {
    private Payment payment;
    private Payment payment2;
    @BeforeEach
    void setup(){
        payment=new Payment("29.11.2025",100,"Miete");
        payment.set_outgoingInterest(0.2);
        payment.set_incomingInterest(0.1);
        payment2=new Payment("29.12.2026",200,"Paypal");
        payment2.set_outgoingInterest(0.4);
        payment2.set_incomingInterest(0.5);

    }
    @Test
    void testKonstruktor(){
        Payment payment3=new Payment("31.11.2025",100,"Miete",0.1,0.8);
assertEquals(0.8,payment3.get_outgoingInterest());
assertEquals(0.1,payment3.get_incomingInterest());
assertEquals("31.11.2025",payment3.get_Date());
assertEquals("Miete",payment3.get_Description());
assertEquals(100,payment3.get_Amount());
    }
    @Test
    void TestKopykonstruktor() {
       Payment B= new Payment(payment);
       assertEquals(payment,B);
       assertNotSame(payment,B);
       assertEquals(payment.get_outgoingInterest(),B.get_outgoingInterest());
       assertEquals(payment.get_incomingInterest(),B.get_incomingInterest());
       assertEquals(payment.get_Amount(),B.get_Amount());
       assertEquals(payment.get_Description(),B.get_Description());
       assertEquals(payment.get_Date(),B.get_Date());
    }

    @Test
    void TestCalculate(){
      payment.set_Amount(100);
      double wert=100-100* payment.get_incomingInterest();
      assertEquals(wert,payment.calculate());

    }

    @Test
    public void testCalculateWithdrawal() {
        payment.set_Amount(-1000.0);
        assertEquals(-1200.0, payment.calculate(), "Auszahlung berechnung falsch");
    }
    @Test
    public void TesttoString(){
        String res=payment.toString();
     assertTrue(res.contains("29.11.2025"));
        assertTrue(res.contains("90"));
        assertTrue(res.contains("Miete"));
        assertTrue(res.contains("incomingInterest=0.1"));
        assertTrue(res.contains("outgoingInterest=0.2"));


    }

    @Test
    public void Testequals(){
  assertEquals(payment,payment);
  Payment burak=new Payment(payment);
  assertEquals(payment,burak);
  Payment neus=new Payment("29.11.2025",100,"Miete",0.1,0.2);
  assertEquals(payment,neus);
        Payment different = new Payment("01.01.2020", 500, "XYZ", 0.1, 0.2);
        assertNotEquals(payment, different);
 assertNotNull(payment);


    }





    @Test
    void testInvalidInterest() {

        assertThrows(TransactionAttributeException.class, () -> {
            payment.set_incomingInterest(3.5);
        });

        assertThrows(TransactionAttributeException.class, () -> {
            payment.set_outgoingInterest(-0.15);
        });
    }







}
