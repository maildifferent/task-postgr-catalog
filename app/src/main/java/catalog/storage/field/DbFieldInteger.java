package catalog.storage.field;

public class DbFieldInteger extends DbField<Integer> {
  public DbFieldInteger(Class<?> table, String name) {
    super(Integer.class, table, name);
  }
}