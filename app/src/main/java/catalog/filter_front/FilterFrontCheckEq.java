package catalog.filter_front;

import catalog.storage.field.DbField;

public class FilterFrontCheckEq<ValT> extends FilterFrontCheck<ValT> {
  public final ValT optionValue;

  public FilterFrontCheckEq(DbField<ValT> dbField, ValT optionValue) {
    super(dbField);
    this.optionValue = optionValue;
  }
}
