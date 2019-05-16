/**
 * 
 */
package testCases;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import helpers.AppData;
import helpers.Browsers;
import helpers.ConfigFileReader;
import helpers.WordAttachment;
import pages.JeeServerPage;
import pages.ManageModulePage;
import pages.PropexPage;
import pages.WebServicesPage;
import pages.WebsphereHomepage;
import pages.WebsphereLoginPage;

/**
 * @author E0SG
 *
 */
public class Checkout extends Browsers {

	public static void main(String[] args) throws Exception {
		Browsers.launchBrowser("chrome");
		/*Browsers.launchBrowser("headlessbrowser");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);*/
		
		
		/*Initializing variables, Pages & Classes*/
		int i=1;
		AppData data=new AppData("Sheet1");
		ConfigFileReader reader=new ConfigFileReader();
		String username=reader.getUsername();
		String password=reader.getPassword();
		JeeServerPage jee=PageFactory.initElements(driver, JeeServerPage.class);
		WebsphereLoginPage login=PageFactory.initElements(driver, WebsphereLoginPage.class);
		WebsphereHomepage home=PageFactory.initElements(driver, WebsphereHomepage.class);
		PropexPage propex=PageFactory.initElements(driver, PropexPage.class);
		ManageModulePage module=PageFactory.initElements(driver, ManageModulePage.class);
		System.out.println("data count :"+data.numberOfData());
		
		
		/*Calling Respective Page Methods*/
		while(i<=data.numberOfData()) {
			String vmName=data.getVM(i);
			jee.enterServerName(vmName);
			Thread.sleep(2000);
			jee.clickOnPropex();
			Thread.sleep(3000);
			propex.dbMappings(i);
			Thread.sleep(2000);
			jee.clickOnConsole();
			//System.out.println("clicked on console");
			login.validLogin(username, password);
			Thread.sleep(3000);
			
			home.navigateToApplication();
			home.roleMappings(i);
			Thread.sleep(2000);
			home.navigateToModulePage();
			module.clickOnModules(i);
			driver.close();
			driver.switchTo().window(ParentWindow);
			i++;
		}

	}

}
