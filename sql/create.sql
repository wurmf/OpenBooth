CREATE TABLE adminusers (adminname VARCHAR(20) PRIMARY KEY, password VARCHAR(50) NOT NULL);

CREATE TABLE profiles (profileID INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(50) NOT NULL);

CREATE TABLE shootings (shootingID INT AUTO_INCREMENT PRIMARY KEY,
                      profileID INT REFERENCES profiles(profileID) ON DELETE SET NULL, 
		      folderpath VARCHAR(250) NOT NULL, isActive BOOLEAN DEFAULT true);

CREATE TABLE images (imageID INT AUTO_INCREMENT PRIMARY KEY, imagepath VARCHAR(250) NOT NULL,
			shootingID INT NOT NULL REFERENCES shootings(shootingID) ON DELETE CASCADE ON UPDATE CASCADE, time TIMESTAMP);
