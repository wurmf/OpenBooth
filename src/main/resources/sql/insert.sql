INSERT INTO profiles (profileID, name, isFilterEnabled,isGreenScreenEnabled) VALUES (1,'Profile 1',true,true);
INSERT INTO profiles (profileID, name) VALUES (2,'Profile 2');
INSERT INTO profiles (profileID, name) VALUES (3,'Profile 3');
INSERT INTO profiles (profileID, name) VALUES (4,'Profile 4');

INSERT INTO shootings (profileID,folderpath) VALUES (1,'/images/shooting1/');
INSERT INTO shootings (profileID,folderpath, isActive) VALUES (2,'/images/shooting2/',FALSE);

INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img1.jpg',1,'2003-06-25 16:50:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting1/img2.jpg',1,'2003-06-25 16:51:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img1.jpg',2,'2003-06-25 16:52:20');
INSERT INTO images(imagepath,shootingid,time) VALUES ('/images/shooting2/img2.jpg',2,'2003-06-25 16:53:20');

INSERT INTO positions(name,buttonImagePath,isDeleted) VALUES ('DB-Test-Position-1','/images/position/camerafront.png',FALSE);
INSERT INTO positions(name,buttonImagePath,isDeleted) VALUES ('DB-Test-Position-3','/images/position/cameraside.png',FALSE);
INSERT INTO positions(name,buttonImagePath,isDeleted) VALUES ('DB-Test-Position-2','/images/position/cameratop.png',FALSE);

INSERT INTO logos(label, path) VALUES ('Logo 1', '/images/logos/logo1.jpg');
INSERT INTO logos(label, path) VALUES ('Logo 2', '/images/logos/logo2.jpg');

INSERT INTO cameras(label,modelName,portNumber,serialNumber) VALUES ('DB-Test-Kamera-1','THIS-IS-NO-MODEL',5,'abcdef');
INSERT INTO cameras(label,modelName,portNumber,serialNumber) VALUES ('DB-Test-Kamera-2','THIS-IS-NO-MODEL-EITHER',1,'ghijklm');

INSERT INTO profile_camera_positions VALUES (1,1,1,1,true);
INSERT INTO profile_camera_positions VALUES (2,1,2,2,false);
INSERT INTO profile_camera_positions VALUES (3,2,2,1,false);
INSERT INTO profile_camera_positions VALUES (4,2,1,2,false);

INSERT INTO profile_logo_relativeRectangles VALUES (1, 1, 1, 85.0, 95.0, 80.0, 90.0);
INSERT INTO profile_logo_relativeRectangles VALUES (2, 1, 2,  5.0, 10.0, 15.0, 20.0);
INSERT INTO profile_logo_relativeRectangles VALUES (3, 2, 1, 75.0, 95.0, 80.0, 90.0);
INSERT INTO profile_logo_relativeRectangles VALUES (4, 2, 2, 15.0, 20.0, 25.0, 30.0);

INSERT INTO backgroundcategories VALUES (1, 'Ostern', false);
INSERT INTO backgroundcategories VALUES (2, 'Weihnachten', false);
INSERT INTO backgroundcategories VALUES (3, 'Geburtstage', false);
INSERT INTO backgroundcategories VALUES (4, 'Hochzeiten', false);

INSERT INTO backgrounds(backgroundID,name,path,backgroundcategoryID) VALUES (1, 'Grüne Wiese','/bsp/folder/p1.jpg',1);
INSERT INTO backgrounds(backgroundID,name,path,backgroundcategoryID) VALUES (2, 'Bretter','/bsp/folder/p1.jpg',1);
INSERT INTO backgrounds(backgroundID,name,path,backgroundcategoryID) VALUES (3, 'Zimmer','/bsp/folder/p1.jpg',1);

INSERT INTO profile_backgroundcategories VALUES (1,1);

ALTER SEQUENCE IF EXISTS profiles_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS shootings_seq RESTART WITH 3;
ALTER SEQUENCE IF EXISTS images_seq RESTART WITH 5;
ALTER SEQUENCE IF EXISTS positions_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS cameras_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS logos_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS profile_logo_relativeRectangles_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS profile_camera_positions_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS backgroundcategories_seq RESTART WITH 100;
ALTER SEQUENCE IF EXISTS backgrounds_seq RESTART WITH 100;