package catalog.storage.field;

import java.math.BigDecimal;

public class DbFieldNumeric extends DbField<BigDecimal> {
  final int precision;
  final int scale;

  public DbFieldNumeric(Class<?> table, String name, int precision, int scale) {
    super(BigDecimal.class, table, name);
    this.precision = precision;
    this.scale = scale;
  }
}