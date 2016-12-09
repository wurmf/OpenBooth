INSERT INTO adminusers (adminname, password) VALUES ('admin','b6f8d434a847fb0f0c1a8d9b936b8ca952e224f205a55f4ba9b2c20f88fdc9e7');

INSERT INTO profiles (name) VALUES ('Profile 1');
INSERT INTO profiles (name) VALUES ('Profile 2');

INSERT INTO shootings (profileID,folderpath) VALUES (1,'/images/shooting1/');
INSERT INTO shootings (profileID,folderpath, isActive) VALUES (2,'/images/shooting2/',FALSE);

INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img1.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img2.jpg',1,'2003-06-25 16:51:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img1.jpg',2,'2003-06-25 16:52:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img2.jpg',2,'2003-06-25 16:53:20');

INSERT INTO positions(positionID,name,buttonImagePath,isDeleted) VALUES (1,'DB-Test-Position-1','/images/position/pos1.jpg',FALSE);
INSERT INTO positions(positionID,name,buttonImagePath,isDeleted) VALUES (2,'DB-Test-Position-2','/images/position/pos2.jpg',FALSE);

INSERT INTO logos(logoID,path) VALUES (1,'/images/logos/logo1.jpg');
INSERT INTO logos(LogoID,path) VALUES (2,'/images/logos/logo2.jpg');
INSERT INTO logos(LogoID,path) VALUES (3,'/images/logos/watermark1.jpg');

INSERT INTO cameras(cameraID,label,modelName,portNumber,serialNumber) VALUES (1,'DB-Test-Kamera-1','THIS-IS-NO-MODEL',5,'abcdef');
INSERT INTO cameras(cameraID,label,modelName,portNumber,serialNumber) VALUES (2,'DB-Test-Kamera-2','THIS-IS-NO-MODEL-EITHER',1,'ghijklm');

INSERT INTO profile_camera_positions VALUES (1,1,1,false);
INSERT INTO profile_camera_positions VALUES (1,2,2,false);

INSERT INTO profile_logo_rpositions VALUES (1,1,85.0,95.0,80.0,90.0);
INSERT INTO profile_logo_rpositions VALUES (1,2, 5.0,10.0,15.0,20.0);
INSERT INTO profile_logo_rpositions VALUES (1,3, -1.0,-1.0,-1.0,-1.0);