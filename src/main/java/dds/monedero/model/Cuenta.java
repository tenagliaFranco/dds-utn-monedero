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
  private double limiteDiario = 1000;

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void poner(double cuanto) {
    verificarMonto(cuanto);
    verificarMovimientosDiarios();
    agregarMovimiento(new MovimientoDeposito(LocalDate.now(), cuanto));
  }

  public void sacar(double cuanto) {
    verificarMonto(cuanto);
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    verificarLimite(cuanto);
    agregarMovimiento(new MovimientoExtraccion(LocalDate.now(), cuanto));
  }

  public void agregarMovimiento(Movimiento movimiento) {
    calcularSaldo(movimiento);
    movimientos.add(movimiento);
  }

  public void calcularSaldo(Movimiento movimiento) {
    saldo += movimiento.calcularValor();
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return getMovimientos().stream()
        .filter(movimiento -> movimiento.fueExtraido(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum();
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(List<Movimiento> movimientos) {
    this.movimientos = movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

  public void verificarMonto(double monto) {
    if (monto <= 0) {
      throw new MontoNegativoException(monto + ": el monto a ingresar debe ser un valor positivo");
    }
  }
  public void verificarMovimientosDiarios() {
    if (getMovimientos().stream()
        .filter(movimiento -> movimiento.fueDepositado(LocalDate.now()))
        .count() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }
  public void verificarLimite(double monto) {
    double limite = limiteDiario - getMontoExtraidoA(LocalDate.now());
    if (monto > limite) {
      throw new MaximoExtraccionDiarioException(
          "No puede extraer mas de $ " + limiteDiario + " diarios, " + "límite: " + limite);
    }
  }
}
