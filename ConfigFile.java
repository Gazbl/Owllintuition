/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.io.InputStream;

/**
 * Interfaces to Configuration file. 
 * @author Gary
 */
public class ConfigFile {

    /**
     * IpAddr of OWL First Device
     */
    public String IpAddr1;

    /**
     * OWL key for Heating system
     */
    public String UDPKey1;

    /**
     *
     */
    public String DeviceType1;

    /**
     *
     */
    public String IpAddr2;

    /**
     *
     */
    public String UDPKey2;

    /**
     *
     */
    public String DeviceType2;
    
    /**
     *
     * @throws Exception
     */
    public ConfigFile() throws Exception {

    }
 
    /**
     * Save Configuration values
     * @return 0 = FAILED 1 = OK
     * @throws Exception
     */
    public int SaveConfigFile() throws Exception {
	Properties prop = new Properties();
	OutputStream output = null;

	try {
		output = new FileOutputStream("config.properties");

		// set the properties value
		if (IpAddr1 != null) {
		    prop.setProperty("IpAddr1", IpAddr1);
		}
		
		if (UDPKey1 != null) {
		    prop.setProperty("UDPKey1", UDPKey1);
		}
		
		if (DeviceType1 != null) {
		    prop.setProperty("DeviceType1", DeviceType1);
		}
				
		if (IpAddr2 != null) {
		    prop.setProperty("IpAddr2", IpAddr2);
		}
		
		if (UDPKey2 != null) {
		    prop.setProperty("UDPKey2", UDPKey2);
		}
		
		if (DeviceType2 != null) {
		    prop.setProperty("DeviceType2", DeviceType2);
		}
		
		// save properties to project root folder
		prop.store(output, null);

	} catch (IOException io) {
		io.printStackTrace();
	} finally {
		if (output != null) {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	return 1;
    }

    /**
     * Load configuration file values
     * @return 0  = FAILED  1 = OK
     * @throws Exception
     */
    public int LoadConfigFile() throws Exception {

	Properties prop = new Properties();
	InputStream input = null;

	try {
		input = new FileInputStream("config.properties");
		// load a properties file
		prop.load(input);

		// get the property values and store them
		IpAddr1=(prop.getProperty("IpAddr1"));
		UDPKey1=(prop.getProperty("UDPKey1"));
		DeviceType1=(prop.getProperty("DeviceType1"));
		IpAddr2=(prop.getProperty("IpAddr2"));
		UDPKey2=(prop.getProperty("UDPKey2"));
		DeviceType2=(prop.getProperty("DeviceType2"));
	} catch (IOException ex) {
		return 0;
	} finally {
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				return 0;
			}
		}
	}
	return 1;
    }    

    /**
     * Close configuration file processing
     */
    public void close() {

    } 	
}
