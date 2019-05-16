import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import helpers.Screenshots;

public class TakeScreenshotsTrial {
 
 public static void main(String[] args) throws Exception{
	 HtmlUnitDriver unitDriver = new HtmlUnitDriver();
	 
	 unitDriver.get("https://google.com");
	 unitDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	 Screenshots.captureScreenshot("a", "b", "google");
	 System.out.println("Title of the page is -> " + unitDriver.getTitle());

	 WebElement searchBox = unitDriver.findElement(By.name("q"));
	 searchBox.sendKeys("Selenium");
	 WebElement button = unitDriver.findElement(By.name("btnG"));
	 button.click();
	 System.out.println("Title of the page is -> " + unitDriver.getTitle());
 }
}