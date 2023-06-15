package catalog.filter_front;

import catalog.storage.field.DbField;

public abstract class FilterFrontCheck<ValT> extends FilterFront<ValT, FilterFrontCheck<ValT>> {
  public Boolean checked;

  public FilterFrontCheck(DbField<ValT> dbField) {
    super(FilterFrontCheck.class, dbField);
  }

  public FilterFrontCheck<ValT> checked(Boolean checked) {
    this.checked = checked;
    return this;
  }
}
