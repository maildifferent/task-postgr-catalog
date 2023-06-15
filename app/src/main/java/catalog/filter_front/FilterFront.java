package catalog.filter_front;

import catalog.storage.field.DbField;
import catalog.tree.Tree;

public abstract class FilterFront<ValT, SpecificT extends FilterFront<ValT, SpecificT>>
    extends Tree<FilterFront<?, ?>, SpecificT> {
  public final Class<ValT> typeClass;
  public final DbField<ValT> dbField;
  public String id; // Method id() should be used to set the id property.

  public <S extends SpecificT> FilterFront(Class<S> specificClass, DbField<ValT> dbField) {
    super(FilterFront.class, specificClass);
    this.dbField = dbField;
    this.typeClass = dbField.typeClass;
  }

  @Override
  protected void setId(String id) {
    this.id = id;
  }

  @Override
  protected String getId() {
    return this.id;
  }
}
