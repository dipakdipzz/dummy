package pages;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import helpers.AppData;
import helpers.Browsers;
import helpers.Screenshots;
import helpers.WordAttachment;

public class WebServicesPage extends Browsers {
	@FindBy(how=How.XPATH,using="//td[@class='collection-table-text']")
	List<WebElement> webservice_ejb;
	@FindBy(how=How.PARTIAL_LINK_TEXT,using="Edit")
	List<WebElement> edit_btn;

	
	/*@FindBy(how=How.NAME,using="overriddenEndpoints")
	WebElement urlValue;*/
	
	public void clickOnUrl(int row) throws Exception {
		int i;
		int j=1;
		String webservice = null;
		String ejbValue = null;
		boolean flag = false;
		Actions action=new Actions(driver);
		
		for(i=0;i<webservice_ejb.size();i++) 
		{
			
			try {
				driver.findElement(By.xpath("//*[@class='column-head-name']/following::*[text()='EJB ']")).isDisplayed();
				flag=true;
			} 
			catch (Exception e) {
				flag = false;
				
			}
			
			//System.out.println("flag is :"+flag);
			//System.out.println("webservice is :"+webservice_ejb.get(i).getText());
			if(flag)
			{
				if(i%5==0) {
					webservice=webservice_ejb.get(i).getText();
					System.out.println("webservice value is :"+webservice);
							}
				if(i%5==1) {
					ejbValue=webservice_ejb.get(i).getText();
					System.out.println("ejbValue is :"+ejbValue);
							}

				if(i%5==4) {
					edit_btn.get(j).click();
					System.out.println("clicked on locator");
					WebElement url=driver.findElement(By.xpath("(//*[@disabled=\"disabled\"])[2]"));
					try {
					action.moveToElement(url).doubleClick().build().perform();
					for(int k=0;k<10;k++) {
					action.sendKeys(Keys.ARROW_RIGHT).build().perform();
											}
						}
					catch(Exception e) {
						System.out.println("not clicked");
					}
					Thread.sleep(2000);
					//action.
					String urlValue=driver.findElement(By.xpath("(//*[@disabled=\"disabled\"])[2]")).getAttribute("value");
					System.out.println(urlValue);
					j=j+2;
				
					/*Thread.sleep(3000);
					WebElement urlValue=driver.findElement(By.name("overriddenEndpoints"));
					urlValue.click();
					System.out.println("clicked on urlvalue");*/
					
					try {
		
						WebElement cancel_btn=driver.findElement(By.name("cancel"));
						File screenshot=Screenshots.captureScreenshot(AppData.getApplication(row),AppData.getEnvironment(row),webservice+"_"+ejbValue);
						WordAttachment.attachScreenshotsToWord(AppData.getApplication(row),AppData.getEnvironment(row),screenshot, urlValue);
						//Screenshots.captureScreenshot("", "",urlValue);
						cancel_btn.click();
			
						}
					catch (Exception e) {
						System.out.println("port info");
										}
			//driver.navigate().back();
			driver.switchTo().parentFrame();
			driver.switchTo().frame("detail");
			Thread.sleep(2000);
				}
			}
				else {
					//Thread.sleep(5000);
					System.out.println("ejb is not available for this application");
					if(i%4==0) {
						webservice=webservice_ejb.get(i).getText();
						System.out.println("webservice is :"+webservice);
						ejbValue="na";
						System.out.println("ejbValue is :"+ejbValue);
								}

					if(i%4==3) {
						//System.out.println("Value of j: "+j);
						//System.out.println("locator value : "+Url.get(j).getText());
						edit_btn.get(j).click();
						System.out.println("clicked on locator");
						WebElement url=driver.findElement(By.xpath("(//*[@disabled=\"disabled\"])[2]"));
						try {
						action.moveToElement(url).doubleClick().build().perform();
						for(int k=0;k<10;k++) {
							action.sendKeys(Keys.ARROW_RIGHT).build().perform();
													}
						}
						catch(Exception e) {
							System.out.println("not clicked");
						}
					
					/*	Point point = url.getLocation();
						int xcord = point.getX();
						int ycord = point.getY();
						System.out.println("x value :"+xcord);
						System.out.println("y value :"+ycord);
						action.doubleClick(url);*/
						Thread.sleep(2000);
						//action.
						String urlValue=driver.findElement(By.xpath("(//*[@disabled=\"disabled\"])[2]")).getAttribute("value");
						System.out.println(urlValue);
						j=j+2;
						//driver.manage().window().maximize();
		/*				Thread.sleep(3000);
						WebElement urlValue=driver.findElement(By.name("overriddenEndpoints"));
						urlValue.click();
						System.out.println("clicked on urlvalue");
						int index=webservice.indexOf("}");*/
					
						//String urlValue=driver.findElement(By.xpath("(//td[@class='collection-table-text'])[3]")).getAttribute("value");
						//Thread.sleep(4000);		
						//driver.findElement(By.name("cancel")).click();
						try {
			
							WebElement cancel_btn=driver.findElement(By.name("cancel"));
							//System.out.println("WebService URl Value :"+urlValue);
							//driver.manage().window().fullscreen();
							//Thread.sleep(2000);
							File screenshot=Screenshots.captureScreenshot(AppData.getApplication(row),AppData.getEnvironment(row),webservice+"_"+ejbValue);
							WordAttachment.attachScreenshotsToWord(AppData.getApplication(row),AppData.getEnvironment(row),screenshot, urlValue);
							cancel_btn.click();
				
							//Thread.sleep(3000);
							}
						catch (Exception e) {
							System.out.println("port info");
											}
				//driver.navigate().back();
				driver.switchTo().parentFrame();
				driver.switchTo().frame("detail");
				Thread.sleep(2000);
					}
					}
			
			}
			
	}

}
