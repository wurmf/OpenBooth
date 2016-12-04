create sequence profiles_seq;
create sequence shootings_seq;
create sequence images_seq;


CREATE TABLE adminusers (adminname VARCHAR(20) PRIMARY KEY, password BINARY(32) NOT NULL);

CREATE TABLE profiles (profileID bigint default profiles_seq.nextval PRIMARY KEY,
                      name VARCHAR(50) NOT NULL, isActive BOOLEAN DEFAULT false);

CREATE TABLE shootings (shootingID bigint default shootings_seq.nextval PRIMARY KEY,
                      profileID INT REFERENCES profiles(profileID) ON DELETE SET NULL,
		      folderpath VARCHAR(250) NOT NULL, isActive BOOLEAN DEFAULT true);

CREATE TABLE images (imageID bigint default images_seq.nextval PRIMARY KEY, imagepath VARCHAR(250) NOT NULL,
			shootingID INT NOT NULL REFERENCES shootings(shootingID) ON DELETE CASCADE ON UPDATE CASCADE, time TIMESTAMP);
