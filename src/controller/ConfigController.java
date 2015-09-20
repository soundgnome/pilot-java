package pilot;

import java.awt.Color;
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


    public Color getColor(String key) {
        Color value = null;
        String spec = this.props.getProperty(key);
        if (spec != null) {
            try {
                String[] rgb = spec.split(",");
                value = new Color(Integer.parseInt(rgb[0]), Integer.parseInt(rgb[1]), Integer.parseInt(rgb[2]));
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Color value for key '" + key + "' has too few values");

            } catch (NumberFormatException e) {
                System.err.println("Color sub-value for key '" + key + "' is not an integer");
            }
        }
        return value;
    }


    public Dimension getDimension(String key) {
        Dimension value = null;
        String spec = this.getString(key);

        if (spec != null) {
            try {
                String[] xy = spec.split("x");
                value = new Dimension(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));

            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println("Dimension value for key '" + key + "' has too few values");

            } catch (NumberFormatException e) {
                System.err.println("Dimension sub-value for key '" + key + "' is not an integer");
            }
        }

        return value;
    }


    public int getInt(String key) {
        int value = Integer.MIN_VALUE;
        String spec = this.getString(key);

        if (spec != null) {
            try {
                value = Integer.parseInt(this.getString(key));
            } catch (NumberFormatException e) {
                System.err.println("Value for key '" + key + "' is not an integer");
            }
        }

        return value;
    }
}
