/**
 * 
 */
package helpers;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import pages.JeeServerPage;
import pages.WebsphereHomepage;

/**
 * @author E0SG
 *
 */
public class Screenshots extends Browsers {
	
	JeeServerPage serverpage=PageFactory.initElements(driver, JeeServerPage.class);
	public static File captureScreenshot(String appName,String Lane,String pageName) throws IOException {
	File screenshotFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
	File screenshotWithName=new File("C:\\automation\\Technical_Checkout\\screenshots\\"+appName+"_"+Lane+"_"+pageName+".png");
	FileUtils.copyFile(screenshotFile,screenshotWithName);
	return screenshotWithName;
	}

}
