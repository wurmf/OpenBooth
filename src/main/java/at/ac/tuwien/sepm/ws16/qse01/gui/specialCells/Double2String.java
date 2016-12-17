package at.ac.tuwien.sepm.ws16.qse01.gui.specialCells;

import javafx.util.StringConverter;


/**
 * <p>{@link StringConverter} implementation for {@link Integer}
 * (and int primitive) values.</p>
 * @since JavaFX 2.1
 */
public class Double2String extends StringConverter<Double> {
    private String title;
    public Double2String(String title){
        this.title = title;
    }
    /** {@inheritDoc} */
    @Override public Double fromString(String value) {
        // If the specified value is null or zero-length, return null
        if (value == null) {
            return null;
        }

        value = value.trim();

        if (value.length() < 1) {
            return null;
        }
        try{
            return Double.valueOf(value);
        }catch (Exception e){

        }
        return null;
    }

    /** {@inheritDoc} */
    @Override public String toString(Double value) {
        // If the specified value is null, return a zero-length String
        if (value == null) {
            return "";
        }

        return (Double.toString(((Double)value).doubleValue()));
    }
}
