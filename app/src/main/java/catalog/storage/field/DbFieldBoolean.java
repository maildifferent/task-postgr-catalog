package catalog.storage.field;

public class DbFieldBoolean extends DbField<Boolean> {
  public DbFieldBoolean(Class<?> table, String name) {
    super(Boolean.class, table, name);
  }
}
