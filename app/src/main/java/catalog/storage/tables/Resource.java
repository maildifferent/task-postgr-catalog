// CREATE TABLE resource (
//   -- https://wiki.postgresql.org/wiki/Don%27t_Do_This#Don.27t_use_serial
//   -- id SERIAL PRIMARY KEY,
//   gid INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
//   resource_name TEXT,
//   resource_category TEXT
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public abstract class Resource {
  static public final DbFieldInteger gid = new DbFieldInteger(ResourceTab.class, "gid");
  static public final DbFieldText resource_name = new DbFieldText(ResourceTab.class, "resource_name");
  static public final DbFieldText resource_category = new DbFieldText(ResourceTab.class, "resource_category");
}