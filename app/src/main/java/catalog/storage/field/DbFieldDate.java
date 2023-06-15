package catalog.storage.field;

import java.time.LocalDate;

public class DbFieldDate extends DbField<LocalDate> {
  public DbFieldDate(Class<?> table, String name) {
    super(LocalDate.class, table, name);
  }
}
