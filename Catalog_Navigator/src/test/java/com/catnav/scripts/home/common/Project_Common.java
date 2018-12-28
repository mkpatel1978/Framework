package com.catnav.scripts.home.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import com.catnav.scripts.home.util.DataUtil;
import com.catnav.scripts.home.util.ExtentManager;
import com.catnav.scripts.home.util.Xls_Reader;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class Project_Common {
	public WebDriver driver;
	
	public SoftAssert softAssert ;
	public Properties prop;
	public Properties envProp;
	public ExtentReports rep = ExtentManager.getInstance();
	public ExtentTest logreport;
	boolean gridRun=false;
	
	public void init(){
		//init the prop file
		if(prop==null){
			prop=new Properties();
			//envProp=new Properties();
			try {
				FileInputStream fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
				prop.load(fs);
				//String env=prop.getProperty("env");
				//fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//"+env+".properties");
				//fs = new FileInputStream(System.getProperty("user.dir")+"//src//test//resources//projectconfig.properties");
				//envProp.load(fs);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void openBrowser(String bType){
		logreport.log(LogStatus.INFO, "Opening browser "+bType);
		if(!gridRun){
			if(bType.equals("Mozilla")){
				System.setProperty("webdriver.gecko.driver",System.getProperty("user.dir")+prop.getProperty("geckodriver_exe"));
				driver=new FirefoxDriver();
			}
			else if(bType.equals("Chrome")){
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+prop.getProperty("chromedriver_exe"));
				driver=new ChromeDriver();
			}
			else if (bType.equals("IE")){
				System.setProperty("webdriver.chrome.driver",System.getProperty("user.dir")+prop.getProperty("iedriver_exe"));
				driver= new InternetExplorerDriver();
			}
		}else{// grid run
			
			DesiredCapabilities cap=null;
			if(bType.equals("Mozilla")){
				cap = DesiredCapabilities.firefox();
				cap.setBrowserName("firefox");
				cap.setJavascriptEnabled(true);
				cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
				
			}else if(bType.equals("Chrome")){
				 cap = DesiredCapabilities.chrome();
				 cap.setBrowserName("chrome");
				 cap.setPlatform(org.openqa.selenium.Platform.WINDOWS);
			}
			
			try {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		logreport.log(LogStatus.INFO, "Browser opened successfully "+ bType);

		
	}
	
	public void Initialize(String TestCaseName,Xls_Reader xls)
	{
		if (DataUtil.isRunnable("TestCases",TestCaseName, xls)==false){
			logreport.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
	
	}

	public void clickonobject(String ObjectIdentifier,String ObjectDescription,boolean AddAsTestResult){
		if (AddAsTestResult=true) logreport.log(LogStatus.INFO, " Clicking on "+ObjectDescription);
		gettheobject(ObjectIdentifier).click();
		if (AddAsTestResult=true) logreport.log(LogStatus.PASS, ObjectDescription+" Clicked successfully ");

	}
	
	public void setvaluetoobject(String ObjectIdentifier,String ValuetoSet,String ObjectDescription,boolean AddAsTestResult){
		if (AddAsTestResult=true)logreport.log(LogStatus.INFO, "set value "+ValuetoSet+" in "+ObjectDescription);
		gettheobject(ObjectIdentifier).sendKeys(ValuetoSet);
		if (AddAsTestResult=true)logreport.log(LogStatus.PASS, "Value "+ ValuetoSet+" is set to "+ObjectDescription);

	}
	
	
	// finding element and returning it
	public WebElement gettheobject(String ObjectIdentifier){
		WebElement e=null;
		try{
		if(ObjectIdentifier.contains("_id"))
			e = driver.findElement(By.id(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.contains("_name"))
			e = driver.findElement(By.name(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.contains("_xpath"))
			e = driver.findElement(By.xpath(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.contains("_linkText"))
			e= driver.findElement(By.linkText(prop.getProperty(ObjectIdentifier)));
		else if (ObjectIdentifier.contains("_classname"))
			e= driver.findElement(By.className(prop.getProperty(ObjectIdentifier)));	
		else{
			reportFailure("Locator not correct - " + ObjectIdentifier);
			Assert.fail("Locator not correct - " + ObjectIdentifier);
		}
		
		}catch(Exception ex){
			// fail the test and report the error
			reportFailure(ex.getMessage());
			ex.printStackTrace();
			Assert.fail("Failed the test - "+ex.getMessage());
		}
		return e;
	}
	/***********************Validations***************************/
	public boolean verifyTitle(){
		return false;		
	}
	
	public boolean checkobjectexist(String ObjectIdentifier,String ObjectDescription,boolean AddAsTestResult){
		List<WebElement> elementList=null;
		if(ObjectIdentifier.endsWith("_id"))
			elementList = driver.findElements(By.id(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.endsWith("_name"))
			elementList = driver.findElements(By.name(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.endsWith("_xpath"))
			elementList = driver.findElements(By.xpath(prop.getProperty(ObjectIdentifier)));
		else if(ObjectIdentifier.contains("_linkText"))
			elementList= driver.findElements(By.linkText(prop.getProperty(ObjectIdentifier)));
		else if (ObjectIdentifier.contains("_classname"))
			elementList= driver.findElements(By.className(prop.getProperty(ObjectIdentifier)));	
		else{
			reportFailure("Locator not correct - " + ObjectIdentifier);
			Assert.fail("Locator not correct - " + ObjectIdentifier);
		}
		
		if(elementList.size()==0){
			if (AddAsTestResult=true) reportPass(ObjectDescription+"is exist in the page");
			return false;
		}
		else{
			if (AddAsTestResult=true) reportFail(ObjectDescription+"is not exist in the page");
			return true;
			}
	}
	
	public boolean verifyText(String ObjectIdentifier,String expectedTextKey,String ObjectDescription,boolean AddAsTestResult){
		String actualText=gettheobject(ObjectIdentifier).getText().trim();
		String expectedText=prop.getProperty(expectedTextKey).trim();
		if(actualText.equals(expectedText)){
			if (AddAsTestResult=true) reportPass(ObjectDescription+" is same");
			return true;
		}
		else {
			if (AddAsTestResult=true) reportFail(ObjectDescription+" is not same");
			return false;
			}
	}
	
	public void clickAndWait(String locator_clicked,String locator_pres){
		logreport.log(LogStatus.INFO, "Clicking and waiting on - "+locator_clicked);
		int count=5;
		for(int i=0;i<count;i++){
			gettheobject(locator_clicked).click();
			delay(2);
			if(checkobjectexist(locator_pres,"",false))
				break;
		}
	}
	
	
	/*****************************Reporting********************************/
	
	public void reportPass(String msg){
		softAssert= new SoftAssert();
		softAssert.assertTrue(true);
		logreport.log(LogStatus.PASS, msg);
	}
	
	public void reportFailure(String msg){
		logreport.log(LogStatus.FAIL, msg);
		takeScreenShot();
		Assert.fail(msg);
	}
	
	public void reportFail(String msg)
	{
		softAssert= new SoftAssert();
		softAssert.assertTrue(false);
		logreport.log(LogStatus.FAIL,msg);
		takeScreenShot();
	}
	
	public void takeScreenShot(){
		// fileName of the screenshot
		Date d=new Date();
		String screenshotFile=d.toString().replace(":", "_").replace(" ", "_")+".png";
		// store screenshot in that file
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		//put screenshot file in reports
		logreport.log(LogStatus.INFO,"Screenshot-> "+ logreport.addScreenCapture(System.getProperty("user.dir")+"//screenshots//"+screenshotFile));
		
	}
	
	public void acceptAlert(){
		WebDriverWait wait = new WebDriverWait(driver,5);
		wait.until(ExpectedConditions.alertIsPresent());
		logreport.log(LogStatus.INFO,"Accepting alert");
		driver.switchTo().alert().accept();
		driver.switchTo().defaultContent();
	}
	
	public void waitForPageToLoad() {
		delay(1);
		JavascriptExecutor js=(JavascriptExecutor)driver;
		String state = (String)js.executeScript("return document.readyState");
		
		while(!state.equals("complete")){
			delay(2);
			state = (String)js.executeScript("return document.readyState");
		}
	}
	
	public void delay(int timeToWaitInSec){
		try {
			Thread.sleep(timeToWaitInSec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getText(String locatorKey){
		logreport.log(LogStatus.INFO, "Getting text from "+locatorKey);
		return gettheobject(locatorKey).getText();

	}
	public void startReport(String ReportName)
	{
		logreport = rep.startTest(ReportName);
	}
	
	public void stopReport()
	{
		if(rep!=null){
			rep.endTest(logreport);
			rep.flush();
		}
	}
	
	public void quitDriver()
	{

		if(driver!=null)
			driver.quit();
	}
	
	public void quitAssertion()
	{
		try{
			softAssert.assertAll();
		}catch(Error e){
			logreport.log(LogStatus.FAIL, e.getMessage());
		}
	}
}
	/************************App functions*****************************/
	