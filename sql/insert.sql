INSERT INTO adminusers (adminname, password) VALUES ('admin','b6f8d434a847fb0f0c1a8d9b936b8ca952e224f205a55f4ba9b2c20f88fdc9e7');

INSERT INTO profiles (name) VALUES ('Profile 1');
INSERT INTO profiles (name) VALUES ('Profile 2');

INSERT INTO shootings (profileID,folderpath) VALUES (1,'/images/shooting1/');
INSERT INTO shootings (profileID,folderpath) VALUES (2,'/images/shooting2/');

INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img1.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img2.jpg',1,'2003-06-25 16:51:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img1.jpg',2,'2003-06-25 16:52:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img2.jpg',2,'2003-06-25 16:53:20');
