package catalog.storage.field;

import java.lang.reflect.Field;
import java.util.Set;

import catalog.filter_main.FilterField;

// Class is made abstract because children classes should be used. Children classes
// might have special properties. E.g. FieldNumeric has precision and scale.
// Fields should be created only in "tables" folder and then used in other parts of
// the program.
public abstract class DbField<OperandT> {
  public final Class<OperandT> typeClass;
  public final Class<?> table;
  public final String name;

  public DbField(Class<OperandT> typeClass, Class<?> table, String name) {
    this.typeClass = typeClass;
    this.table = table;
    this.name = name;
  }

  public String getTableName()
      throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
    Field tableNameField = this.table.getDeclaredField("name");
    String tableName = (String) tableNameField.get(null);
    return tableName;
  }

  // Comparison methods (eq, ne, in, nin, gt, gte, lt, lte).
  public FilterField<OperandT> eq(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.eq(val);
    return filterField;
  }

  public FilterField<OperandT> ne(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.ne(val);
    return filterField;
  }

  public FilterField<OperandT> in(Set<OperandT> val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.in(val);
    return filterField;
  }

  public FilterField<OperandT> nin(Set<OperandT> val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.nin(val);
    return filterField;
  }

  public FilterField<OperandT> gt(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.gt(val);
    return filterField;
  }

  public FilterField<OperandT> gte(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.gte(val);
    return filterField;
  }

  public FilterField<OperandT> lt(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.lt(val);
    return filterField;
  }

  public FilterField<OperandT> lte(OperandT val) {
    var filterField = new FilterField<OperandT>(this);
    filterField.lte(val);
    return filterField;
  }
}