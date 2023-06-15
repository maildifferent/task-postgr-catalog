// CREATE TABLE image (
//   gid INTEGER PRIMARY KEY,
//   image_src TEXT
// );

package catalog.storage.tables;

import catalog.storage.field.DbFieldInteger;
import catalog.storage.field.DbFieldText;

public class Image {
  static public final DbFieldInteger gid = new DbFieldInteger(ImageTab.class, "gid");
  static public final DbFieldText image_src = new DbFieldText(ImageTab.class, "image_src");
}
