/**
 * 
 */
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import helpers.Browsers;

/**
 * @author E0SG
 *
 */
public class WebsphereLoginPage extends Browsers {
	
	@FindBy(how=How.ID,using="j_username")
	WebElement username;
	@FindBy(how=How.ID,using="j_password")
	WebElement password;
	@FindBy(how=How.CLASS_NAME,using="loginButton")
	WebElement submitbtn;
/*	@FindBy(how=How.ID,using="forceradio")
	WebElement forceLogout;*/
	/*@FindBy(how=How.XPATH,using="//*[@value='OK']")
	WebElement ok;
	*/
	public void validLogin(String userid,String pass) {
		
		try{
			username.sendKeys(userid);
			password.sendKeys(pass);
			submitbtn.click();
			}
		catch (Exception e) {
			System.out.println("login page didn't appear");
			}

		try {
			driver.findElement(By.xpath("//*[@value='OK']")).click();
		    } 
		
		catch (Exception e) {
			System.out.println("ok button is not displayed");
			}
				
		
	}

}
