INSERT INTO adminusers VALUES ('admin', 'password');

INSERT INTO profiles (name) VALUES ('Profile 1');
INSERT INTO profiles (name) VALUES ('Profile 2');

INSERT INTO shootings (profileID,folderpath) VALUES (1,'/home/dummy/shooting1/');
INSERT INTO shootings (profileID,folderpath) VALUES (2,'/home/dummy/shooting2/');

INSERT INTO images VALUES ('/home/dummy/shooting1/img.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images VALUES ('/home/dummy/shooting1/img.jpg',2,'2003-06-25 16:50:20');
INSERT INTO images VALUES ('/home/dummy/shooting1/img.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images VALUES ('/home/dummy/shooting1/img.jpg',2,'2003-06-25 16:50:20');
