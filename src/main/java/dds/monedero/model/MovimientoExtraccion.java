package dds.monedero.model;

import java.time.LocalDate;

public class MovimientoExtraccion extends Movimiento{
  public MovimientoExtraccion(LocalDate fecha, double monto) {
    super(fecha, monto);
  }
  @Override
  public boolean fueDepositado(LocalDate fecha) {
    return false;
  }

  @Override
  public boolean fueExtraido(LocalDate fecha) {
    return esDeLaFecha(fecha);
  }

  @Override
  public double calcularValor() {
    return -getMonto();
  }
}
