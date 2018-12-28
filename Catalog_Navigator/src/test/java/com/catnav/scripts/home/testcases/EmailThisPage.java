package com.catnav.scripts.home.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.catnav.scripts.home.common.Project_Common;
import com.catnav.scripts.home.common.CBPLP_Common;
import com.catnav.scripts.home.util.DataUtil;
import com.catnav.scripts.home.util.Xls_Reader;
import com.relevantcodes.extentreports.LogStatus;

public class EmailThisPage extends CBPLP_Common {

	String testCaseName="EmailThisPage";
	Xls_Reader xls;
	
	
	@BeforeMethod
	public void befortest()
	{
		startReport("Email This Page");
	}
	
	@Test(dataProvider="getData")
	public void fnEmailThisPage(Hashtable<String,String> data){
		Initialize(testCaseName,xls);
		
		String Browsertype = DataUtil.BrowserType("TestCases", testCaseName, xls);
		String SubEnv = DataUtil.SubEnvironment("TestCases", testCaseName, xls);
		String Env = DataUtil.Environment("TestCases", testCaseName, xls);
		String CompanyName = data.get("CompanyName");
		System.out.println(Browsertype + "-"+SubEnv +"-"+Env);
		openBrowser(Browsertype);
		//System.out.println(Browsertype + "-"+SubEnv +"-"+Env);
		CATNAV_OpenPLPURL(CompanyName,Env,SubEnv);
		navigateBreadcrumb(data.get("Breadcrumb"));
		clickonobject("EmailthisPage_linkText", "Email This Page",true);
		setvaluetoobject("EmailRecipient_xpath",data.get("RecipientEmail"),"Recipient Email",true);
		setvaluetoobject("EmailYourMail_xpath",data.get("YourEmail"),"Your Mail",true);
		clickonobject("EmailSendCopy_xpath", "Send copy to self",false);
		setvaluetoobject("EmailComments_xpath","Order Submission","Comments",true);
		clickonobject("EmailSendMail_xpath", "Send Mail Button",true);
		delay(1);
		verifyText("EmailConfirmation_xpath","Email_Confirmation","Email this Page confirmation message",true);
	}
	

	@AfterMethod
	public void quit(){
		stopReport();
		System.gc();
		//quitAssertion();
		quitDriver();
	}
	
	@DataProvider(parallel=true)
	//@DataProvider
	public Object[][] getData(){		
		init();	
		xls = new Xls_Reader(System.getProperty("user.dir")+prop.getProperty("DataTablePath"));
		return DataUtil.getTestData(xls,"Data",testCaseName);
	}

}
