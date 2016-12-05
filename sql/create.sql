CREATE SEQUENCE profiles_seq;
CREATE SEQUENCE shootings_seq;
CREATE SEQUENCE images_seq;
CREATE SEQUENCE positions_seq;
CREATE SEQUENCE cameras_seq;

CREATE TABLE adminusers (adminname VARCHAR(20) PRIMARY KEY, password BINARY(32) NOT NULL);

CREATE TABLE profiles (profileID BIGINT DEFAULT profiles_seq.nextval PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       logoPath VARCHAR(250) DEFAULT NULL,
                       logoPosition INT DEFAULT 0,
                       watermarkPath VARCHAR(250) DEFAULT NULL,
                       isPrintEnabled BOOLEAN DEFAULT false,
                       isFilerEnabled BOOLEAN DEFAULT false,
                       isGreenscreenEnable BOOLEAN DEFAULT false,
                       isActive BOOLEAN DEFAULT false,
                       isDeleted BOOLEAN DEFAULT false);

CREATE TABLE shootings (shootingID BIGINT DEFAULT shootings_seq.nextval PRIMARY KEY,
                        profileID BIGINT REFERENCES profiles(profileID) ON DELETE SET NULL,
                        folderpath VARCHAR(250) NOT NULL, isActive BOOLEAN DEFAULT true);

CREATE TABLE images (imageID BIGINT DEFAULT images_seq.nextval PRIMARY KEY, imagepath VARCHAR(250) NOT NULL,
                     shootingID BIGINT NOT NULL REFERENCES shootings(shootingID) ON DELETE CASCADE ON UPDATE CASCADE, time TIMESTAMP);

CREATE TABLE positions(positionID BIGINT DEFAULT positions_seq.nextval PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        isDeleted BOOLEAN DEFAULT false);

CREATE TABLE cameras(cameraID BIGINT DEFAULT cameras_seq.nextval PRIMARY KEY,
                     label VARCHAR(50) NOT NULL,
                     modelName VARCHAR(50) NOT NULL,
                     portNumber INT,
                     serialNumber VARCHAR(50) NOT NULL);

CREATE TABLE profile_camera_positions(profileId BIGINT REFERENCES profiles(profileID) ON DELETE CASCADE,
                        cameraId BIGINT REFERENCES cameras(cameraId) ON DELETE CASCADE,
                        positionId BIGINT REFERENCES positions(positionID) ON DELETE CASCADE,
                        isGreenscreenReady BOOLEAN DEFAULT FALSE,
                        PRIMARY KEY (profileId, cameraId,positionId));




