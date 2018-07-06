package org.openbooth.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

/*
* PropertiesPersister
*
* What does do for you?
* It persists variables into and loads them from file config.properties
* Since properties are generally strings it also provides you a method to load variable and convert to integer in one step
*
* How to use it?
* Simply import the class and get the singleInstance of it:
* PropertiesPersister propertiesPersister = PropertiesPersister.getInstance();
* and use its set and get methods
*
* below see one example usage
* */
public class PropertiesPersister {

    private static PropertiesPersister singleInstance;
    private FileWriter writer;
    private FileReader reader;
    private File configFile;
    private Properties props;


    private PropertiesPersister(){
        configFile = new File("config.properties");
        props = new Properties();
    }

    /*
        create or get only existing instance
    */
    public static PropertiesPersister getInstance(){
        if(singleInstance == null){
            singleInstance = new PropertiesPersister();
        }
        return singleInstance;
    }


    /*
        set and persist a string 'value' with name 'key'
        @param key: name of property as string
        @param value: value of property as a string
        @throws IOException e
    */
    public void setProperty(String key, String value) {
        props.setProperty(key,value);
        try {
            writer = new FileWriter(configFile);
            props.store(writer,"");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        set and persist a integer value with name 'key'
        @param key: name of property as string
        @param value: value of property as an integer
        @throws IOException e
    */
    public void setProperty(String key, int value) {
        props.setProperty(key, Integer.toString(value));
        try {
            writer = new FileWriter(configFile);
            props.store(writer,"");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    set and persist a integer value with name 'key'
    @param key: name of property as string
    @param value: value of property as a double
    @throws IOException e
*/
    public void setProperty(String key, double value) {
        props.setProperty(key, Double.toString(value));
        try {
            writer = new FileWriter(configFile);
            props.store(writer,"");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        get persisted a string value with name 'key' and leave it unconverted
    */
    public String getPropertyString(String key) {
        try {
            reader = new FileReader(configFile);
            props.load(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props.getProperty(key);
    }

    /*
        get persisted a string value with name 'key' and convert it to integer
    */
    public int getPropertyInteger(String key) {
        return Integer.parseInt(this.getPropertyString(key));
    }

    /*
        get persisted a string value with name 'key' and convert it to double
    */
    public double getPropertyDouble(String key) {
        return Double.parseDouble(this.getPropertyString(key));
    }

    /*
        get currents propertyies enlisted by key and value
    */
    public void list() {
        props.list(System.out);

    }

    /*
    examples code how to set a string, integer and double value and get them back in the desired type:

    PropertiesPersister propertiesPersister = PropertiesPersister.getInstance();
    propertiesPersister.setProperty("example_string","openbooth" );
    propertiesPersister.setProperty("example_integer_number",1000 );
    propertiesPersister.setProperty("example_double_number",3.141 );
    String host = propertiesPersister.getPropertyString("example_string");
    int number_integer = propertiesPersister.getPropertyInteger("example_integer_number");
    double number_double = propertiesPersister.getPropertyDouble("example_double_number");
    propertiesPersister.list();
    System.out.println(number_integer*number_double);
    */

}
