/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.text.DateFormatSymbols;
/**
 *
 * @author Gary
 */


/**
 *  Defines the Owl commands available
 */
public class OwlCommands {

    /**
     * UPD Interface
     */
    private  UDPInterface client;
    
    /**
     * Error messages from OWLCommand class
     */
    private String sErrorString;
    
    /**
     * Array of OWL Commands
     * 1 - Command Name 
     * 2 - Command Description
     * 3 - Command Formant
     */
    private final String OwlCommandsArray[][] = 
	{{"VERSION", "Retrieves the version information of the device","VERSION"}, 
	{"DEVICE", "Manages internal device list","GET,DEVICE,1"}, 
	{"DAYHEATINGPERIODS", "Retrieves list of heating periods for a specified day  Get Parameters: GET,DAYHEATINGPERIODS,(day) Day: 0 – 6 = Sunday to Saturday"
	    + "","GET,DAYHEATINGPERIODS,0"},
	{"HEATINGPERIOD", "Add a heating period to the timeclock."
	    + "\nWhen altering the time clock it is recommended that you delete all periods for a day, then upload all of the periods for that day again," 
	    + " in time order,  with the SET,HEATINGPERIOD command. \nThe Network OWL will not order the periods as they are added."
		,"SET,HEATINGPERIOD,(day),(start),(end),(temperature)"},
	{"HEATINGDAY","Deletes heating periods for a specific day.","DEL,HEATINGDAY,(day)"},
	{"UPTIME", "Retrieves the run time of the device in days, hours, minutes and seconds.", "UPTIME"},
	{"CLOCK", "Allows for the retrieval of the network owl’s clock data (in seconds). ", "CLOCK"},
	{"HEATINGDAY","Deletes heating periods for a specific day","DEL,HEATINGDAY,(day)"},
	{"HEATINGPERIOD","Adds a heating period to the timeclock.","SET,HEATINGPERIOD,(day),(start),(end),(temperature)"},
	{"SCAN","Puts the device into wireless scanning mode and starts looking for wireless devices." +  
		" Once 1 device is found, scanning mode is cancelled. Action Parameters (optional):" +
		"SCAN,(device_type) -­‐ Device Type: 119, 160 or 180 to scan for electricity transmitters. " +
		" If left blank, the Network OWL will scan for heating or hot water devices (Room Stat, Tank Sensor, Relay Box, etc)","SCAN,(device_type)" },
	{"DAYHWPERIODS","Retrieves hot water periods for a specified day (0-6)","GET,DAYHWPERIODS,(day)"},
	{"HWDAY","Deletes hot water periods for a specific day.","DEL,HWDAY,(day)"},
	{"HWPERIOD","Add a hot water period to the timeclock."
	    + "\nWhen altering the time clock it is recommended that you delete all periods for a day, then upload all of the periods for that day again," 
	    + " in time order,  with the SET,HWPERIOD command. \nThe Network OWL will not order the periods as they are added.","SET,HWPERIOD,(day),(start),(end),(temperature)"},
	{"MOREHW","Overrides the hot water controls. \n" +
	    "-­‐ ON = overriding hot water. OFF = not overriding.\n"+  
	    "-­‐ Temperature: the  temperature  to be maintained while overriding. "  +
	    "-­‐ The [Device ID] parameter is optional and is the address if the Room Sensor device (ie 2000001\n" +  
	    "– Must be in upper case). If not included, all heating devices on the system will be boosted. ",
	    "MOREHW,(ON/OFF),(temperature),[DeviceID]"},
	{"AWAY","Toggles Away mode & sets times.","SET/GET,AWAY,[start time],[end time]"},
	{"SUMMER","Configures summer start and end dates.OWL \nIntuition will automatically\n" +
//	    "switch your heating to Summer Mode between the First Day of Summer and Last Day of Summer\n" +
//	    "in the boxes above. Whilst in Summer Mode your heating will normally be off (time clock suspended).\n" +
//	    "You can select alternative dates if required - note the dates are inclusive.\n" +
//	    "These same dates will be used every year. Ensure you press Save after changing the dates.\n" +
//	    "TIP: When OWL Intuition is in Summer Mode, if you have a cold day and want the heating to \n" +
//	    "come on then pressing the Comfort button will re-engage the normal time clock heating settings\n" +
	    "for the rest of the day before reverting back to Summer Mode","GET,SUMMER or SET,SUMMER,(start date),(end date)"},
	{"BOOST","Boosts the heating temperature\n The [Device ID] parameter is optional and is the address if the Room  Sensordevice  (ie 2000001 " 
	    + " – Mustbe  in uppercase). If not included,  allheating devices on the  system will be  boosted.};" 
		,"BOOST,(ON/OFF),[Device]"},
	{"STANDBY","Sets Standby mode to on.","STANDBY"},
	{"COMFORT","Sets Comfort mode to on.","COMFORT"},
	    };
	   
    /**
     *
     */
    public OwlCommands(String IpAddr) {
	try{ 
	    client = new UDPInterface(IpAddr);
	} catch (Exception exc) {
	    sErrorString = "UDPInterface Exception returned to OwlCommands()";
	}
    }
    
    /**
     * Returns the number of Commands available in Command Array
     * @return No of Commands available
     */
    public int OwlNoOfCommands() {
	return OwlCommandsArray.length;
    }
 
    /**
     * Returns the description of the command indicated
     * @param i Index to Command array
     * @return Description of Command
     */
    public String OwlCommandsDesc(int i) {	
	String sDesc;
	if (i < OwlCommandsArray.length) {
	    sDesc = OwlCommandsArray[i] [1];
	} else {
	    sDesc = "";
	    sErrorString = "OwlCommandsDesc: Invalid Index " + String.valueOf(i) 
		    +"< OwlCommandsArray.length(" + String.valueOf(OwlCommandsArray.length)+")";
	}
	return sDesc;    
    }
    
    /**
     * Current Error message
     * @return Error message
     */
    public String OwlErrorMsg () {
	return sErrorString;
    }
        
    /**
     * Returns a Command from the array of commands for the index given
     * If Index invalid returns "FAILED
     * @param i Index of command in array
     * @return Command Name
     */
    public String OwlCommandList(int i) {
/**
"
 */	
	String item;
	if (i < OwlCommandsArray.length) {
	    item = OwlCommandsArray[i] [0];
	} else {
	    item = "FAILED";
	}
	return item;
	
    }

    /**
     * Command Descriptionm
     * @param  i Index of command in array
     * @return Command Description
     */
    public String OwlCommandMsg(int i)  {
	String CommandMsg;
	CommandMsg =OwlCommandsArray[i] [2];
        return CommandMsg;
    }
 
    /**
     * Obtains the Version of the OWL
     * @param UPDKey UPD Key of OWL device
     * @return Version of OWL Device
     */
    public String OwlVersion (String UPDKey) {
	String sReply;
	try {
	    sReply = client.sendMsg("VERSION",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "UDPInterface Exception returned to OwlVersion()";
	    sReply = "FAILED";
	}    
	return sReply;
    }

    /**
     * Obtains the Up Time of the OWL Device
     * @param UPDKey UPD Key of OWL device
     * @return Up Time of the OWL Device
     */
    public String OwlUptime (String UPDKey) {
	String sReply;
	try {
	    sReply = client.sendMsg("UPTIME",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "UDPInterface Exception returned to OwlUptime()";
	    sReply = "FAILED";
	}    
	return sReply;	
    }
    
    /**
     * Obtains the Clock of the OWL
     * @param UPDKey UPD Key of OWL device
     * @return Both Clock values as a string
     */
    public String OwlClock (String UPDKey) {
	String sReply;
	try {
	    sReply = client.sendMsg("CLOCK",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "UDPInterface Exception returned to OwlClock()";
	    sReply = "FAILED";
	}    
	return sReply;	
    }
    
    /**
     * Delete  heating periods for Day from device
     * @param sDay Number of Day of the week to be deleted (0=Sunday)
     * @param UPDKey UPD Key of OWL device
     * @return
     */
    public boolean OwlDelHeatingDay (String sDay, String UPDKey) {
	String sReply = "";
	try {
	    sReply = client.sendMsg("DEL,HEATINGDAY," + sDay ,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlHeatingDay()" + sReply;
	    return false;
	}    	
	return true;
    }
    
    /**
     * Adds a heating period to device
     * @param sDay  Number of Day of the week to be added (0=Sunday)
     * @param sStart Start Time
     * @param sEnd   End Time
     * @param sTemp Temperature required
     * @param UPDKey for the OWL device
     * @return True if sucessful
     */
    public boolean OwlHeatingPeriod (String sDay, String sStart, String sEnd, String sTemp, String UPDKey){
	String sReply = "";
	try {
	    sReply = client.sendMsg("SET,HEATINGPERIOD," + sDay + "," +sStart + "," + sEnd+ "," + sTemp,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlHeatingPeriod()" + sReply;
	    return false;
	}    
	return true;		
    }
	 
    /**
     * Load Holiday dates from the Owl Device
     * Holiday Start: Unix timestamp for Start of the  holiday period (from 1/1/1970 00:00:00)
     * @param UPDKey for the OWL device
     * @return  Start Date of Holiday
     */
    public Date OwlLoadHolidayStart (String UPDKey) {
	String sRespFields [];
	Date sReply;
	sReply = new Date ();
	String sResp ="";
	sReply.setTime(0);
	try {
	    sResp = client.sendMsg("GET,HOLIDAY",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayDates()" + sResp;
	    return sReply;
	}   	
	
	// Convert date
	sRespFields = sResp.split(",");
	long i = Integer.valueOf(sRespFields[2]);
	sReply.setTime(i *1000); // Java expects date in milliseconds
	return sReply;
    }
    
    /**
     * Load Holiday End date from the Owl Device
     * Holiday End: Unix timestamp for End of the  holiday period (from 1/1/1970 00:00:00)
     * @param UPDKey for the OWL device
     * @return End date of Holiday
     */
    public Date OwlLoadHolidayEnd (String UPDKey) {
	String sRespFields [];
	Date sReply;
	sReply = new Date ();
	String sResp ="";
	sReply.setTime(0);
	try {
	    sResp = client.sendMsg("GET,HOLIDAY",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayDates()" + sResp;
	    return sReply;
	}   	
	
	// Convert date
	sRespFields = sResp.split(",");  // Split up response into fields
	int iEnddateLth = iAlphaNumericLth (sRespFields[3]); // Find length of End date
	if (iEnddateLth <=0) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayEnd() Invalid" + sResp;
	    return sReply;	    
	}
	String s = new String (); // Define new string for end date
	s =  sRespFields[3].substring(0,iEnddateLth);	// Move End date only into string 
	long i = Integer.valueOf(s); // Convert to s
	sReply.setTime(i *1000); // Java expects date in milliseconds
	
	return sReply;
    }
    
    /**
     * Save Holiday Dates
     * @param Start Start date of holiday
     * @param End End date of Holiday
     * @param UPDKey for the OWL device
     * @return True if Successful
     */
    public Boolean OwlSaveHolidayDates (Date Start, Date End, String UPDKey) {
    long  iStart = Start.getTime() / 1000;
    long iEnd = End.getTime() / 1000;
    if (iStart<0) iStart = 0;
    if (iEnd< 0) iEnd = 0;
    String sResp ="";
    String sMsg;
	try {
	    sMsg = ("SET,HOLIDAY," + String.valueOf(iStart) + "," + String.valueOf(iEnd));
	    sResp = client.sendMsg(sMsg,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in OwlSaveHolidayDates.UDPInterface()" + sResp;
	    return false;
	}   	
	String sReplyOK = sResp.substring(0, 2);
	if (!sReplyOK.equals("OK")){
	    sErrorString = "Error in OwlSaveHolidayDates.UDPInterface() Msg =" + sMsg + "Response=" + sResp;
	    return false;	    
	}
    return true;
}
    	 
    /**
     * Load Summer Start date from the Owl Device
     * Device gives response "OK,SUMMER,(start date),(end date)"
     * Start date: the day of the year to start summer mode. (1-­‐365)
     * End   date: the day  of the year to end summer  mode. (1-­‐365)
     * @param UPDKey for the OWL device
     * @return OwlHeatingTimes The day/month of the start and end of summer
     */
    public OwlHeatingTimes OwlLoadSummerDates (String UPDKey) {
	
//	Calendar calendarDate;
	// Initialse Calender date to 1st day of year
	OwlHeatingTimes  Response = new OwlHeatingTimes();
	Response.iSummerStartDay=0;
	Calendar calendarStarDate = new GregorianCalendar(2013,1,1,13,24,56);
	Calendar calendarEndDate = new GregorianCalendar(2013,1,1,13,24,56);



	Date sReply;
	sReply = new Date ();
	String sResp ="";
	sReply.setTime(0);
	try {
	    sResp = client.sendMsg("GET,SUMMER",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayDates()" + sResp;
	    return Response;
	}   	
	
	// Convert dates into Calender Object
	String sOWLRespFlds[] = sResp.split(",");
	int iStartDayofYear = Integer.valueOf(sOWLRespFlds[2]);

	int j = iAlphaNumericLth (sOWLRespFlds[3]); // Find length of End date
	if (j <=0) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayEnd() Invalid" + sResp;
	    return Response;	    
	}
	String s = new String ();	
	s =  sOWLRespFlds[3].substring(0,j);

	int iEndDayofYear = Integer.valueOf(s);	
	
	calendarStarDate.add(Calendar.DAY_OF_YEAR, iStartDayofYear);
	calendarEndDate.add(Calendar.DAY_OF_YEAR, iEndDayofYear);	

	Response.iSummerStartDay = calendarStarDate.get(Calendar.DAY_OF_MONTH);
	Response.iSummerStartMth  = calendarStarDate.get(Calendar.MONTH);
	Response.iSummerEndDay = calendarEndDate.get(Calendar.DAY_OF_MONTH);
	Response.iSummerEndMth = calendarEndDate.get(Calendar.MONTH);
	return Response;
    }

    /**
     * Device gives response "OK,SUMMER,(start date),(end date)"
     * Start date: the day of the year to start summer mode. (1-­‐365)
     * End   date: the day  of the year to end summer  mode. (1-­‐365)      *
     * @param OwlHeatingTimes Start and End day/month of summer
     * @param UPDKey for the OWL device
     * @return True if saved,
     */
    public Boolean OwlSaveSummerDates (OwlHeatingTimes SummerDates, String UPDKey) {
    
    // Convert Dates to Day of the Year
    Calendar calendar = Calendar.getInstance();

    calendar.set(1970, SummerDates.iSummerStartMth-1, SummerDates.iSummerStartDay);
    int StartDayofYear = calendar.get(Calendar.DAY_OF_YEAR);

    calendar.set(1970, SummerDates.iSummerEndMth-1, SummerDates.iSummerEndDay);
    int EndDayofYear = calendar.get(Calendar.DAY_OF_YEAR);
    
    if (StartDayofYear > EndDayofYear) {
	    sErrorString = "Start of Summer is after End of Summer" ;
	    return false;	
    }
    
    String sResp ="";
    String sMsg ="";
	try {
	    sMsg = "SET,SUMMER," + String.valueOf(StartDayofYear) + "," + String.valueOf(EndDayofYear) ;
	    sResp = client.sendMsg(sMsg ,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  OwlSaveSummerDates.UDPInterface() " + "Msg=" + sMsg + " Response =" + sResp;
	    return false;
	}       
    return true;
}

    /**
     * Set Device to Standby
     * @param UPDKey for the OWL device
     * @return True if Successful
     */
    public Boolean OwlStandby (String UPDKey)    {
    String sResp ="";
	try {
	    sResp = client.sendMsg("STANDBY",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlStandby() " + sResp;
	    return false;
	}   	   
    return true;
}

    /**
     * Set Device to Comfort
     * @param UPDKey for the OWL device
     * @return True if Successful
     */
    public Boolean OwlComfort (String UPDKey)    {
    String sResp ="";
	try {
	    sResp = client.sendMsg("COMFORT",UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlComfort() " + sResp;
	    return false;
	}   	
    return true;
}

    /**
     * Set Device to Boost
     * @param sSwitch
     * @param UPDKey for the OWL device
     * @return True if Successful
     */
    public Boolean OwlBoost (String sSwitch, String UPDKey)    {
    String sResp ="";
	try {
	    sResp = client.sendMsg("BOOST," +sSwitch ,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlBoost() " + sResp;
	    return false;
	}   	
    return true;
}
    /**
     * Gat Away Times from Device
     * @param UPDKey for the OWL device
     * @return OwlHeatingTimes Away from HH MM to HH MM  If failed then AwayTimes.iAwayFromHH = -1 
     */
    public OwlHeatingTimes GetOwlAway ( String UPDKey)    {
	String sResp ="";

        OwlHeatingTimes AwayTimes; 
        AwayTimes = new OwlHeatingTimes();	
	// Get Away time from  OWL Device
	try {
	     sResp = client.sendMsg("GET,AWAY" ,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.GetOwlAway() " + sResp;
	    AwayTimes.iAwayFromHH = -1;
	    return AwayTimes;
	} 

	// Format away times response
	String sOWLRespFlds[] = sResp.split(",");
	int iAwayStartInSecs = Integer.valueOf(sOWLRespFlds[2]); // Start Time in secs

	int j = iAlphaNumericLth (sOWLRespFlds[3]); // Find length of End Time
	if (j <=0) {
	    sErrorString = "Error in  UDPInterface.OwlLoadHolidayEnd() Invalid" + sResp;
	    AwayTimes.iAwayFromHH = -1;
	    return AwayTimes;	    
	}
	String s = new String ();	
	s =  sOWLRespFlds[3].substring(0,j);
	int iAwayEndInSecs = Integer.valueOf(s);	// Away End time in secs
	

	AwayTimes.iAwayFromHH = iAwayStartInSecs/3600;
	AwayTimes.iAwayFromMM = (iAwayStartInSecs - (AwayTimes.iAwayFromHH * 3600))/60;
	AwayTimes.iAwayToHH = iAwayEndInSecs/3600;
	AwayTimes.iAwayToMM = (iAwayEndInSecs - (AwayTimes.iAwayToHH * 3600))/60;	
    return AwayTimes;
}

    /**
     * Set Device to Away
     * @param UPDKey for the OWL device
     * @return True if Successful
     */
    public Boolean OwlAway ( String UPDKey)    {
    String sResp ="";
	try {
	    sResp = client.sendMsg("SET,AWAY" ,UPDKey);
	} catch (Exception exc) {
	    sErrorString = "Error in  UDPInterface.OwlAway() " + sResp;
	    return false;
	}   	
    return true;
}

    /**
     * Convert Number of Month into Month Name
     * @param month Number of Month 1=January
     * @return Month Name
     */
    public String getMonth(int month) {	
	String s;
	s= DateFormatSymbols.getInstance().getMonths()[month-1];
    return new DateFormatSymbols().getMonths()[month-1];
    }
  
    /**
     * Find the length of non null characters in a buffer
     * @param s Buffer
     * @return Length of non null character string.
     */
    private int iAlphaNumericLth(String s) {
	for (int i = s.length() - 1; i >= 0; i--) {
	    char c = s.charAt(i);
	    if ( Character.isDefined(c) && (c != 00)) //Character.isLetter(c) || Character.isDigit(c) 
		return i+1;
	}
	return -1; // no alphanumeric character at all
    }  

    /**
     *
     */
    public void close() {

    }        
}
