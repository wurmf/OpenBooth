package org.openbooth.config.keys;

public enum StringKey {

    IMG_PREFIX ("image_prefix"),
    IMAGE_FOLDER ("image_storage_path");

    public final String key;

    StringKey(String key){
        this.key = key;
    }

}
