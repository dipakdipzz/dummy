/**
 * 
 */
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import helpers.Browsers;
import helpers.Screenshots;

/**
 * @author E0SG
 *
 */
public class JeeServerPage extends Browsers {

@FindBy(how=How.ID,using="f_host")
WebElement hostname;
@FindBy(how=How.PARTIAL_LINK_TEXT,using="console")
WebElement console;
@FindBy(how=How.PARTIAL_LINK_TEXT,using="bldtoolGUI")
WebElement propexec;
@FindBy(how=How.XPATH,using="//*[@type='radio' and @value='buildVar']")
WebElement buildInformation;


public void enterServerName(String vm) {
	try
	{
	hostname.click();
	hostname.clear();
	hostname.sendKeys(vm);
	}
	catch(Exception e) {
		System.out.println("end of data");
	}
}

/*public void buildInfo() {
	buildInformation.click();
	s.captureScreenshot(appName, pageName);
}*/
public void clickOnConsole() {
	/*Actions action=new Actions(driver);
	action.doubleClick(console).build().perform();*/
	try {
	console.click();
	for(String window:driver.getWindowHandles()) {
		driver.switchTo().window(window);
												}
	}
	catch (Exception e) {
		System.out.println("directly routed to login page");
			}

	try {
	console.click();
	for(String window:driver.getWindowHandles()) {
		driver.switchTo().window(window);
												}
	}
	catch(Exception e) {
		System.out.println("websphere home page launched on 1st click");
	}

	
}

public void clickOnPropex() {
	propexec.click();
	propexec.click();
	for(String window:driver.getWindowHandles()) {
		driver.switchTo().window(window);
		
		
	}
}

}
