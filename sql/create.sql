CREATE SEQUENCE profiles_seq CYCLE MAXVALUE 2147483600;
CREATE SEQUENCE shootings_seq CYCLE MAXVALUE 2147483600;
CREATE SEQUENCE images_seq CYCLE MAXVALUE 2147483600;
CREATE SEQUENCE positions_seq CYCLE MAXVALUE 2147483600;
CREATE SEQUENCE cameras_seq CYCLE MAXVALUE 2147483600;
CREATE SEQUENCE logos_seq CYCLE MAXVALUE 2147483600;

CREATE TABLE adminusers (adminname VARCHAR(20) PRIMARY KEY, password BINARY(32) NOT NULL);

CREATE TABLE profiles (profileID BIGINT DEFAULT profiles_seq.nextval PRIMARY KEY,
                       name VARCHAR(50) NOT NULL,
                       isPrintEnabled BOOLEAN DEFAULT false,
                       isFilterEnabled BOOLEAN DEFAULT false,
                       isGreenscreenEnabled BOOLEAN DEFAULT false,
                       isDeleted BOOLEAN DEFAULT false);

CREATE TABLE shootings (shootingID BIGINT DEFAULT shootings_seq.nextval PRIMARY KEY,
                        profileID BIGINT REFERENCES profiles(profileID) ON DELETE SET NULL,
                        folderpath VARCHAR(250) NOT NULL, isActive BOOLEAN DEFAULT true);

CREATE TABLE images (imageID BIGINT DEFAULT images_seq.nextval PRIMARY KEY, imagepath VARCHAR(250) NOT NULL,
                     shootingID BIGINT NOT NULL REFERENCES shootings(shootingID) ON DELETE CASCADE ON UPDATE CASCADE, time TIMESTAMP);

CREATE TABLE positions(positionID BIGINT DEFAULT positions_seq.nextval PRIMARY KEY,
                        name VARCHAR(50) NOT NULL,
                        buttonImagePath VARCHAR(250) NOT NULL,
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

CREATE TABLE logos(logoID BIGINT DEFAULT logos_seq.nextval PRIMARY KEY,
                        path VARCHAR(250) DEFAULT NULL,
                        isDeleted BOOLEAN DEFAULT false);

CREATE TABLE profile_logo_rpositions(profileId BIGINT REFERENCES profiles(profileID) ON DELETE CASCADE,
                        logoId BIGINT REFERENCES logos(logoId) ON DELETE CASCADE,
                        x1 DOUBLE,
                        y1 DOUBLE,
                        x2 DOUBLE,
                        y2 DOUBLE,
                        PRIMARY KEY (profileId, logoId)
                        );