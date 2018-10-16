package org.openbooth.util;

/**
 * This class is used as a helper class for dealing with paths when using abbreviations like "~"
 */
class PathHelper {

    /**
     * Returns the given path with a starting "~" substituted with the user.home system property
     * @param path the given path
     * @return the expanded path
     */
    static String expandPath(String path){
        if(path.startsWith("~")){
            String homeDirectoryPath = System.getProperty("user.home");
            return homeDirectoryPath + path.substring(1);
        } else {
            return path;
        }
    }
}
