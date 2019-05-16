package helpers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import com.gargoylesoftware.htmlunit.BrowserVersion;

public class Browsers {
	
	public static WebDriver driver;
	public static String ParentWindow;
	
	public static void launchBrowser(String browsername) {
		if(browsername.equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver", "C:\\automation\\Technical_Checkout\\src\\test\\resources\\chromedriver.exe");
			 ChromeOptions options = new ChromeOptions();
		     options.setExperimentalOption("useAutomationExtension", false);
		     driver = new ChromeDriver(options);
		     driver.get("https://sfjtech.opr.statefarm.org/cgi-bin/JEEutils");
		     ParentWindow=driver.getWindowHandle();
		}
		if(browsername.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", "C:\\automation\\Technical_Checkout\\src\\test\\resources\\IEDriverServer.exe");
			driver=new InternetExplorerDriver();
			driver.get("https://sfjtech.opr.statefarm.org/cgi-bin/JEEutils");
			}
		if(browsername.equalsIgnoreCase("headlessbrowser")) {
			//System.setProperty("webdriver.ie.driver", "C:\\automation\\Technical_Checkout\\src\\test\\resources\\IEDriverServer.exe");
			//driver=new HtmlUnitDriver(BrowserVersion.FIREFOX_60, true);
			driver=new PhantomJSDriver ();
			driver.get("https://sfjtech.opr.statefarm.org/cgi-bin/JEEutils");
			}
	}

}
