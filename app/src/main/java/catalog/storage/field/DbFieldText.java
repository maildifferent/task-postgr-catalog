package catalog.storage.field;

public class DbFieldText extends DbField<String> {
  public DbFieldText(Class<?> table, String name) {
    super(String.class, table, name);
  }
}