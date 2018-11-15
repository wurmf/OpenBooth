package org.openbooth.config.keys;

public enum IntegerKey {

    MAX_PREVIEW_REFRESH("max_previews_per_second", 0, "Having 0 or less previews per second is not valid, as there will be no live preview."),
    NUM_BURST_SHOTS("burst_number_of_shots", 1, "1 or less shots in burst mode does not make sense."),
    SHOW_SHOT_TIME("show_shot_time", 0, "It does not make sense to show the shot less than 0 milliseconds."),
    SHOT_COUNTDOWN("counter_per_shot", 0, "It does not make sense to count down from zero or less.");

    public final String key;
    public final int infimum;
    public final String validationErrorMessage;

    IntegerKey(String key, int infimum, String validationErrorMessage) {
        this.key = key;
        this.infimum = infimum;
        this.validationErrorMessage = validationErrorMessage + " Please change the value of '" + key + "' in the config file";
    }

}
