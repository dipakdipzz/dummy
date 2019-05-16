/**
 * 
 */
package pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import helpers.AppData;
import helpers.Browsers;
import helpers.Screenshots;
import helpers.WordAttachment;

/**
 * @author E0SG
 *
 */
public class WebsphereHomepage extends Browsers {
	String appName;
	@FindBy(how=How.XPATH,using="//*[text()='Applications']")
	WebElement applications;
	@FindBy(how=How.XPATH,using="(//*[text()='Application Types'])[1]")
	WebElement applicationType;
	@FindBy(how=How.XPATH,using="//*[text()='WebSphere enterprise applications']")
	WebElement webSphereEnterpriseApllication;
	@FindBy(how=How.XPATH,using="//*[text()='You can monitor the following resources:']/following::a[1]")
	WebElement applicationName;
	@FindBy(how=How.LINK_TEXT,using="Security role to user/group mapping")
	WebElement roles;
	@FindBy(how=How.PARTIAL_LINK_TEXT,using="Manage Modules")
	WebElement modules;
	
	public void navigateToApplication() throws InterruptedException {
		driver.switchTo().frame("navigation");
		driver.manage().window().maximize();
		
		if(webSphereEnterpriseApllication.isDisplayed()) 
		{
			webSphereEnterpriseApllication.click();
		}
		else if(applicationType.isDisplayed()) 
		{
		applicationType.click();
		webSphereEnterpriseApllication.click();
		}
		else
		{
			applications.click();
			applicationType.click();
			webSphereEnterpriseApllication.click();
		}
		
		Thread.sleep(2000);
		driver.switchTo().parentFrame();
		driver.switchTo().frame("detail");
		applicationName.click();
		
	}
	public void roleMappings(int row) throws Exception   {
		roles.click();
		File screenshot=Screenshots.captureScreenshot(AppData.getApplication(row),AppData.getEnvironment(row),"roleToUser");
		WordAttachment.attachScreenshotsToWord(AppData.getApplication(row),AppData.getEnvironment(row),screenshot, screenshot.getName());
		driver.navigate().back();
	}
	public void navigateToModulePage() {
		driver.switchTo().parentFrame();
		driver.switchTo().frame("detail");
		modules.click();
	}
	

}
