package com.catnav.scripts.home.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import com.relevantcodes.extentreports.LogStatus;

public class CBPLP_Common extends Project_Common{
	
	//OpenURL open url
		public void CATNAV_OpenPLPURL(String CompanyName,String Environment,String SubEnvironment){
			String AppURL ;
			if (Environment.compareToIgnoreCase("SITE")==0)
			{
				if (SubEnvironment.toUpperCase().equals("STAGE"))
					AppURL = "http://" + CompanyName + ".stage.thomasnet-navigator.com/" ;
				else
					AppURL = "http://" + CompanyName + ".thomasnet-navigator.com/" ;
			}
			else{
				//AppURL="www.yahoo.com";
				AppURL = "http://" + CompanyName + ".cn-" + Environment.toLowerCase() + "-" + SubEnvironment.toLowerCase() +".catnav.us/" ;
				}
			System.out.println(AppURL);
			driver.get(AppURL);
		}
		
		//Nevigate to a particular breadcrumb 
		public void navigateBreadcrumb(String BreadCrumb)
		{
			try{
			String[] arrBreadcrumb = BreadCrumb.split(">");
			for (String s: arrBreadcrumb) { 
				s = s.trim();
		        driver.findElement(By.linkText(s)).click();
		    }
			}catch(Exception ex)
			{
				reportFailure(ex.getMessage());
				ex.printStackTrace();
				Assert.fail("Failed the test - "+ex.getMessage());
			}
		}
		
	
}


