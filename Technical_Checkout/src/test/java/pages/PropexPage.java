package pages;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.Select;

import helpers.AppData;
import helpers.Browsers;
import helpers.DbMappingVerification;
import helpers.Screenshots;
import helpers.WordAttachment;

public class PropexPage extends Browsers {
	
	@FindBy(how=How.NAME,using="f_view")
	WebElement viewbtn;
	DbMappingVerification verify=new DbMappingVerification();
	String dbName;
	String j2cName;
	public void dbMappings(int row) throws Exception {
		
		
		/*List<WebElement> list=driver.findElements(By.tagName("option"));
		for(int i=0;i<list.size();i++) {
			
		}*/
			
		String mainwindow=driver.getWindowHandle();
		WebElement multiselect=driver.findElement(By.name("f_active_props"));
		Select db=new Select(multiselect);
		List<WebElement> options=db.getOptions();
		System.out.println("total mappings count :"+options.size());
		//for-each loop or Advanced for loop
		
	  for(int i=0;i<options.size();i++){
		db.deselectAll();
		String propertyName=options.get(i).getText();
		System.out.println(propertyName);
		
		/*Find out dbName*/
		
		if(propertyName.startsWith("sfDB2")) {
			dbName=propertyName.substring(propertyName.lastIndexOf("_")+1, propertyName.length());
			System.out.println(dbName);
		}
		/*Find out J2C Name*/
		
		if(propertyName.startsWith("sfJ2C")) {
			j2cName=propertyName.substring(propertyName.lastIndexOf("_")+1, propertyName.length());
			System.out.println(j2cName);
		}
		
		
	//The DB's are under a multi select table
		
		db.selectByIndex(i);
		viewbtn.click();
		Thread.sleep(2000);
		for(String window:driver.getWindowHandles()) {
			driver.switchTo().window(window);
			
		}
		driver.manage().window().maximize();
		
		/*FInd the actual qualifier mapping and verifying it*/
		String TextFile=driver.findElement(By.name("file")).getText();
		String db2="${qualifier}=";
		if(TextFile.contains(db2)) {
			int startingIndex=TextFile.indexOf(db2)+13;
			String qualifierValue=TextFile.substring(startingIndex, startingIndex+3);
			System.out.println(qualifierValue);
			if(qualifierValue.equalsIgnoreCase(verify.verifyDbMappings(dbName))){
				System.out.println("correct mapping");
			}
			else {
				System.out.println("wrongly mapped : "+dbName);
			}
		}
		File screenshot=Screenshots.captureScreenshot(AppData.getApplication(row),AppData.getEnvironment(row),propertyName );
		WordAttachment.attachScreenshotsToWord(AppData.getApplication(row),AppData.getEnvironment(row),screenshot, screenshot.getName());
		Thread.sleep(2000);
		driver.close();		
		Thread.sleep(1000);
		driver.switchTo().window(mainwindow);
		Thread.sleep(1000);
		
		}
	  driver.close();
	  for(String window:driver.getWindowHandles()) {
			driver.switchTo().window(window);
	  }
	}	
}
