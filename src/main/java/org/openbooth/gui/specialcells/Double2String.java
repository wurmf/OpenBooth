package org.openbooth.gui.specialcells;

import javafx.util.StringConverter;


/**
 * <p>{@link StringConverter} implementation for {@link Integer}
 * (and int primitive) values.</p>
 * @since JavaFX 2.1
 */
public class Double2String extends StringConverter<Double> {


    /** {@inheritDoc} */
    @Override
    public Double fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }

        return Double.valueOf(value);
    }

    /** {@inheritDoc} */
    @Override
    public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return (Double.toString(value));
    }
}
