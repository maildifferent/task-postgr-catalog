// CREATE TABLE filter_single_str (
//   gid INTEGER PRIMARY KEY,
//   single_class_str TEXT
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public class FilterSingleStr {
  static public final DbFieldInteger gid = new DbFieldInteger(FilterSingleStrTab.class, "gid");
  static public final DbFieldText single_class_str = new DbFieldText(FilterSingleStrTab.class, "single_class_str");
}