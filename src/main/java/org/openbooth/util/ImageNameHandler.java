package org.openbooth.util;

public class ImageNameHandler {

    private static String shootingName = "NotImplemented";
    private static int currentImageID = 0;

    public static String getNewImageName() {
        return shootingName + "_" + currentImageID;
    }
}
