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

	public class RFISubmission extends CBPLP_Common{
		
		String testCaseName="RFISubmission";
		Xls_Reader xls;
		
		
		@BeforeMethod
		public void befortest()
		{
			startReport("RFI Submission");
		}
		
		@Test(dataProvider="getData")
		public void RFIconfirmation(Hashtable<String,String> data){
			Initialize(testCaseName,xls);
			String Browsertype = DataUtil.BrowserType("TestCases", testCaseName, xls);
			String SubEnv = DataUtil.SubEnvironment("TestCases", testCaseName, xls);
			String Env = DataUtil.Environment("TestCases", testCaseName, xls);
			String CompanyName = data.get("CompanyName");
			
			System.out.println(Browsertype + "-"+SubEnv +"-"+Env);
			openBrowser(Browsertype);
			CATNAV_OpenPLPURL(CompanyName,Env,SubEnv);
			clickonobject("RFIBtn_xpath", " RFI Button ",true);
			setvaluetoobject("RFIName_xpath",data.get("RFIName"),"RFI Name Edit Box ",true);
			setvaluetoobject("RFIEmail_xpath",data.get("Email"),"Email Address Edit Box",true);
			clickonobject("RFiSendBtton_xpath", "Submit Button",true);
			delay(1);
			verifyText("RFiConfirmation_xpath","RFI_Confirmation","RFI confirmation message",true);
		}
		
		@AfterMethod
		public void quit(){
			stopReport();
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
	

