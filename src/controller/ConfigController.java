package pilot;

import java.awt.Dimension;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ArrayIndexOutOfBoundsException;
import java.lang.NullPointerException;
import java.lang.NumberFormatException;
import java.util.Properties;

public class ConfigController {

    private Properties props;


    public ConfigController() throws IOException {
        this.props = new Properties();

        FileInputStream configFile = new FileInputStream("pilot.properties");
        props.load(configFile);
    }


    public String getString(String key) {
        String value = this.props.getProperty(key);
        if (value == null) {
            System.err.println("Config value not found for key '" + key + "'");
        }
        return value;
    }


    public int getInt(String key) {
        int value;
        String spec = this.getString(key);
        
        if (spec != null) {
            try {
                value = Integer.parseInt(this.getString(key));
            } catch (NumberFormatException e) {
                System.err.println("Value for key '" + key + "' is not an integer");
                value = Integer.parseInt(null);
            }
        } else {
            value = Integer.parseInt(null);
        }

        return value;
    }


    public Dimension getDimension(String key) {
        Dimension value;
        String spec = this.getString(key);

        if (spec != null) {
            try {
                String[] xy = spec.split("x");
                value = new Dimension(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Dimension value for key '" + key + "' has too few values");
                value = null;

            } catch (NumberFormatException e) {
                System.err.println("Dimension sub-value for key '" + key + "' is not an integer");
                value = null;
            }
        } else {
            value = null;
        }

        return value;
    }
}
