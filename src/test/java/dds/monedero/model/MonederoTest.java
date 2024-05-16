package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonederoTest {
  private Cuenta cuenta;

  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void HacerDepositoCambiarSaldoYRegistraElMovimiento() {
    cuenta.Depositar(1500);
    Assertions.assertTrue(cuenta.getSaldo()==1500);
    Assertions.assertEquals(1,cuenta.getMovimientos().size());
  }
  @Test
  void Sacar(){
    cuenta.setSaldo(1500);
    cuenta.Extraer(900);

    Assertions.assertTrue(cuenta.getSaldo()==600);
  }

  @Test
  void PonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.Depositar(-1500));
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.Extraer(-500));
  }


  @Test
  void TresDepositos() {
    cuenta.Depositar(1500);
    cuenta.Depositar(456);
    cuenta.Depositar(1900);
    Assertions.assertTrue(cuenta.getSaldo()==(1500+456+1900));
  }

  @Test
  void MasDeTresDepositos() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.Depositar(1500);
          cuenta.Depositar(456);
          cuenta.Depositar(1900);
          cuenta.Depositar(245);
    });
  }

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90);
          cuenta.Extraer(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000);
      cuenta.Extraer(1001);
    });
  }
@Test
  void agregarDeposito(){
    cuenta.agregarMovimiento(new Deposito( LocalDate.now(),600));
    Assertions.assertTrue(cuenta.getMovimientos().size()==1);
}

}