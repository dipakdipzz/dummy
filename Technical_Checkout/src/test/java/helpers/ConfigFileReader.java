package helpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

public class ConfigFileReader {
	private final String PropertyFilePath="C:\\automation\\Technical_Checkout\\src\\test\\resources\\configuration.properties";
	private Properties properties;
	public ConfigFileReader() throws Exception {
	
		BufferedReader reader=new BufferedReader(new FileReader(PropertyFilePath));
		properties=new Properties();
		properties.load(reader);
	}
	public String getUsername() {
		String username=properties.getProperty("username");
		return username;
	}
	public String getPassword() {
		String password=properties.getProperty("password");
		return password;
	}

}
