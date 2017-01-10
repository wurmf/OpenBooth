ALTER SEQUENCE profiles_seq RESTART WITH 1;
ALTER SEQUENCE shootings_seq RESTART WITH 1;
ALTER SEQUENCE images_seq RESTART WITH 1;
ALTER SEQUENCE positions_seq RESTART WITH 1;
ALTER SEQUENCE cameras_seq RESTART WITH 1;
ALTER SEQUENCE logos_seq RESTART WITH 1;
ALTER SEQUENCE profile_logo_relativeRectangles_seq RESTART WITH 1;
ALTER SEQUENCE profile_camera_positions_seq RESTART WITH 1;

DELETE FROM profile_camera_positions;
DELETE FROM profile_logo_relativeRectangles;
DELETE FROM positions;
DELETE FROM logos;
DELETE FROM cameras;
DELETE FROM images;
DELETE FROM shootings;
DELETE FROM profiles;
DELETE FROM adminusers;