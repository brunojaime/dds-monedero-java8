package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() { //Está bueno que el constructor de menos parámetros llame al de mas parámetros pasando sus parámetros iniciales
    this(0);
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }


  //Duplicated Code -> poner y Extraer ambos validan que el valor no sea un monton negativo, y e instancian un nuevo movimiento (new Movimiento)
  public void Depositar(double cuanto) {
    validarMontoNegativo(cuanto);
    // Long Method. getMovimientos().stream.filter.count()
    if (getMovimientos().stream().filter(movimiento -> movimiento.isDeposito()).count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    new Deposito(LocalDate.now(), cuanto).agregateA(this);
  }


  public void Extraer(double cuanto) {
    validarMontoNegativo(cuanto);
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede Extraer mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    //Veo un acoplamiento raro. Cuenta instancia un nuevo movimiento, que llama a agregateA, y agregateA vuelve a llamar la cuenta con agregarMovimiento
    new Extraccion(LocalDate.now(), cuanto).agregateA(this);
  }

  private  void validarMontoNegativo(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void agregarMovimiento(Movimiento movimiento) {
    movimientos.add(movimiento);
  }

  // Long method -> getMovimientos().stream().filter().map().sum()
  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> !movimiento.isDeposito() && movimiento.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}
