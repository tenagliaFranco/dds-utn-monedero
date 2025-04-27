package dds.monedero.model;

import java.time.LocalDate;

public class MovimientoDeposito extends Movimiento{

  public MovimientoDeposito(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  @Override
  public boolean fueDepositado(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }

  @Override
  public boolean fueExtraido(LocalDate fecha) {
    return false;
  }

  @Override
  public double calcularValor() {
    return getMonto();
  }
}
