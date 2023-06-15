// CREATE TABLE filter_cheese (
//   gid INTEGER PRIMARY KEY,
//   cheese_moist_fract TEXT,
//   cheese_blue BOOLEAN,
//   cheese_fat_fract INTEGER,
//   cheese_flavor TEXT
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldBoolean;
import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public class FilterCheese {
  static public final DbFieldInteger gid = new DbFieldInteger(FilterCheeseTab.class, "gid");
  static public final DbFieldText cheese_moist_fract = new DbFieldText(FilterCheeseTab.class, "cheese_moist_fract");
  static public final DbFieldBoolean cheese_blue = new DbFieldBoolean(FilterCheeseTab.class, "cheese_blue");
  static public final DbFieldInteger cheese_fat_fract = new DbFieldInteger(FilterCheeseTab.class, "cheese_fat_fract");
  static public final DbFieldText cheese_flavor = new DbFieldText(FilterCheeseTab.class, "cheese_flavor");
}
