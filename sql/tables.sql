-- INSERT INTO amo_lead 
--   (id, name, price, responsible_user_id, status_id, pipeline_id, created_at)
--   VALUES
--   (239529, 'Сделка 1', 0, 9219490, 54958050, 6428286, 1675937365),
--   (239530, 'Сделка 2', 1000, 9219490, 54958050, 6428286, 1675937365);

CREATE TABLE resource (
  -- https://wiki.postgresql.org/wiki/Don%27t_Do_This#Don.27t_use_serial
  -- id SERIAL PRIMARY KEY,
  gid INTEGER PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
  resource_name TEXT,
  resource_category TEXT
);

CREATE TABLE image (
  gid INTEGER PRIMARY KEY,
  image_src TEXT
);

CREATE TABLE loctext ( -- Local text.
  gid INTEGER,
  loctext_lang TEXT,
  loctext TEXT,
  PRIMARY KEY (gid, loctext_lang)
);

CREATE TABLE price (
  gid INTEGER,
  price_datefr DATE,
  price_dateto DATE,
  price INTEGER,
  price_cur TEXT,
  price_qty INTEGER,
  price_uon TEXT,
  PRIMARY KEY (gid, price_datefr)
);

CREATE TABLE measurelot (
  gid INTEGER PRIMARY KEY,
  measurelot_weight_mainuon TEXT,
  measurelot_nweight_appr BOOLEAN,
  measurelot_nweight_qty INTEGER,
  measurelot_nweight_uon TEXT,
  measurelot_weight_appr BOOLEAN,
  measurelot_weight_qty INTEGER,
  measurelot_weight_uon TEXT
);

CREATE TABLE filter_cheese (
  gid INTEGER PRIMARY KEY,
  cheese_moist_fract TEXT,
  cheese_blue BOOLEAN,
  cheese_fat_fract INTEGER,
  cheese_flavor TEXT
);

CREATE TABLE filter_single_str (
  gid INTEGER PRIMARY KEY,
  single_class_str TEXT
);
