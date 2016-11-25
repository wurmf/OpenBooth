INSERT INTO adminusers VALUES ('admin', 'password');

INSERT INTO profiles (name) VALUES ('Profile 1');
INSERT INTO profiles (name) VALUES ('Profile 2');

INSERT INTO shootings (profileID,folderpath) VALUES (1,'/home/dummy/shooting1/');
INSERT INTO shootings (profileID,folderpath) VALUES (2,'/home/dummy/shooting2/');

INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img1.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img2.jpg',1,'2003-06-25 16:51:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img1.jpg',2,'2003-06-25 16:52:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img2.jpg',2,'2003-06-25 16:53:20');
