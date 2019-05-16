package pages;

import java.io.File;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import helpers.AppData;
import helpers.Browsers;
import helpers.Screenshots;
import helpers.WordAttachment;

public class ManageModulePage extends Browsers {

	@FindBy(how=How.XPATH,using="//td[@class='collection-table-text']/a")
	List<WebElement> modules;
	
	public void clickOnModules(int row) throws Exception {
		WebServicesPage webservice=PageFactory.initElements(driver, WebServicesPage.class);
		//List<WebElement> modules=driver.findElements(By.xpath("//td[@class='collection-table-text']/a"));
		for(int i=0;i<modules.size();i++) {
			Thread.sleep(2000);
			modules.get(i).click();
			//String module=modules.get(i).getText();
			try {
				Thread.sleep(2000);
				driver.findElement(By.linkText("Web services client bindings")).click();
				try {
					File screenshot=Screenshots.captureScreenshot(AppData.getApplication(row),AppData.getEnvironment(row) , "webservices");
					WordAttachment.attachScreenshotsToWord(AppData.getApplication(row),AppData.getEnvironment(row),screenshot, screenshot.getName());
					Thread.sleep(2000);
					}
				catch (Exception e) {
					System.out.println("screenshot has not been taken for webservices");
									}
				try {
					//System.out.println("before clicking on webserice edit btn");
					webservice.clickOnUrl(row);
					}
				catch (Exception e) {
					System.out.println("unable to click on webserice edit btn");
						}
				}

			catch (Exception e) {
				System.out.println("web service is not available for this module:");
								}
			
			
			finally {
				driver.findElement(By.linkText("Manage Modules")).click();
				//driver.navigate().back();
				//driver.navigate().refresh();
				driver.switchTo().parentFrame();
				driver.switchTo().frame("detail");
				Thread.sleep(2000);
				
			}
			
	
			
		}
	}
}
