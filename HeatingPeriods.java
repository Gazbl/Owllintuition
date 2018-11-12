/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;


import java.util.Arrays;


/**
 *
 * @author Gary
 */
public  class HeatingPeriods {
    /**
     * Displays and Edits the Heating Periods
     */
    private int iNoOfPeriods = 0;

    private String [] [] HeatPeriodParam  = new String [100] [4] ;
    private String [] [] HeatDayPeriodParam  = new String [10] [4] ;

    private  static final int NO_OF_PARAMS_PRE_PERIOD = 4;
    
    private  static final int DAY = 0;
    private  static final int STARTTIME = 1;
    private  static final int ENDTIME = 2;
    private  static final int TEMP = 3;
    private  static final int NOOFDAYSINWEEK = 7;
    
    private 	    OwlCommands  Commands;    
    private 	    UDPInterface client;

    /**
     * Holds error messages from the class
     */
    public String sErrorString;
    


    /**
     * Initialises Heating Period object
     * @param UPDKey
     */
    public  HeatingPeriods(String IpAddr,String UPDKey) {
	String sReply;
	try {

	    client = new UDPInterface(IpAddr);
//	    OwlCommands  Commands;
	    Commands = new OwlCommands (IpAddr);    
	    iNoOfPeriods =0;
	    for (int iDay = 0; iDay<NOOFDAYSINWEEK ;iDay++){
		/* Obtain Heating Periods for a day  */
		String Msg = Commands.OwlCommandMsg(2);
		Msg = Msg + String.valueOf(iDay);
		sReply = client.sendMsg((Msg),UPDKey);

		String[] HeatPeriodParams = sReply.split(",");
		// Move reply into appropriate fields
		for (int j =2; j+3<(HeatPeriodParams.length); j=j+NO_OF_PARAMS_PRE_PERIOD,iNoOfPeriods++){
		    HeatPeriodParam[iNoOfPeriods] [0] = String.valueOf(iDay);  //Set day of the  Week as after the 1st instance its always zero
		    if (HeatPeriodParams[j+1].equals("0")  && HeatPeriodParams[j+2].equals("0") && HeatPeriodParams[j+3].equals("0.00")) {
			HeatPeriodParam[iNoOfPeriods] [1] ="EMPTY";
		    } else {			    
			for (int m=1,  n=j+1; m< NO_OF_PARAMS_PRE_PERIOD; m++,n++) { // use n to skip of Day of week param
			    HeatPeriodParam[iNoOfPeriods] [m] = HeatPeriodParams[n];
			    }
		    }
		}
	    }
	    sReply = "";
	} catch (Exception exc) {
		sReply = "HeatingPeriods Exception occured";
		sErrorString = sErrorString+ "HeatingPeriods Error\n";
	}	 

	return ;
    }

    /**
     * Load Heating Periods for a day into the Heating Period Array
     * @param iDay  Day of the week
     * @param UPDKey UPD Key for OWL Device
     * @return True if successful
     */
    public  Boolean HeatingDayPeriods(int iDay, String UPDKey) {
	String sReply;
	try {
  
	    iNoOfPeriods =0;
	    for (int i = 0; i<NOOFDAYSINWEEK ;i++){
		/* Obtain Heating Periods for a day  */
		String Msg = Commands.OwlCommandMsg(2);
		Msg = Msg + String.valueOf(i);
		sReply = client.sendMsg((Msg),UPDKey);

		String[] HeatPeriodParams = sReply.split(","); //split up reply into fields
		// Move reply into appropriate fields
		for (int j =2; j<(HeatPeriodParams.length-1); j=j+NO_OF_PARAMS_PRE_PERIOD,iNoOfPeriods++){
		    HeatPeriodParam[iNoOfPeriods] [0] = String.valueOf(iDay);
		    for (int m=1,  n=j+1; m< NO_OF_PARAMS_PRE_PERIOD; m++,n++) {
			HeatPeriodParam[iNoOfPeriods] [m] = HeatPeriodParams[n];
		    }
		}
	    }
	    sReply = "";
	} catch (Exception exc) {
		sReply = "HeatingDayPeriods Error Occured";
		sErrorString = sErrorString + "HeatingDayPeriods Error\n";
		return false;
	}	 

	return true ;
    }
    
    /**
     * Save Period Array to OWL Device
     * @param UPDKey  UPD Key for OWL Device
     * @return True if succesful
     */
    public boolean SavePeriods (String UPDKey){

	String Day = "";
	
	try {
	// Loop though Period Array
	    for (int i =0; i<iNoOfPeriods; i++){			
		if (!Day.equals(HeatPeriodParam[i] [0])){ // New Day of week?
		    Day = HeatPeriodParam[i] [0]; // Store current day		    
		    if (!Commands.OwlDelHeatingDay(Day, UPDKey)) { // Delete all Periods for day
			// Failed to delete periods from day
			 sErrorString = sErrorString + "SavePeriods: Failed  to delete Periods for day (" + String.valueOf(i) + ")\n";
		    }
		}
		
		// add heating period to device
		if (!Commands.OwlHeatingPeriod(HeatPeriodParam[i] [DAY],HeatPeriodParam[i] [STARTTIME],HeatPeriodParam[i] [ENDTIME],
			HeatPeriodParam[i] [TEMP], UPDKey)) {
		    // Error in adding period to device
		    sErrorString = sErrorString +"HeatingPeriods.SavePeriods error adding period\n";
		    return false;
		}
	    }
		
	} catch (Exception exc) {
		sErrorString = "SavePeriods Exception";
		return false;
	}	 	    
	return true;
    }
	
    /**
     * Return the number of heating periods in array
     * @return the number of heating periods in array
     */
    public int GetNoOfPeriods () {
	return iNoOfPeriods;
    }
	    
    /**
     * Set the number of entries  in the heating periods array
     * @param i Number of entires
     */
    public void SetNoOfPeriods (int i) {
	iNoOfPeriods = i;
    }
	
    /**
     * Get the name of the day of the week.
     * @param iPeriod Number of the day of the week
     * @return the name of the day.
     */
    public String GetDay (int iPeriod) {
	String WeekDay[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	String Day;
	
	int i = Integer.parseInt(HeatPeriodParam[iPeriod][DAY]);
	if (i< NOOFDAYSINWEEK) {
	    Day= WeekDay[i];    
	} else {
	    Day = "GetPeriod:Unknown";
	}
	return Day;
    }

    /**
     *  Validate the Day, Start time, End Time and Temp of a  period
     * @param iPeriod Index to entry in the Heating Param Table
     * @param sDay   Name of the Day of the Week
     * @param sStart  Start Time
     * @param sEnd End Time	
     * @param sTemp Temperture required
     * @return  True if succesful
     */
    public boolean StorePeriodInArray (int iPeriod, String sDay, String sStart, String sEnd, String sTemp) {
	int iStartTime;
	int iEndTime;
	int iDayOfWeek; // No of the day of the week
	if ((iDayOfWeek= iValidateDay(sDay)) == -1) return false;
	if (!ValidateStart(sStart)) return false;
	if (!ValidateEnd(sEnd)) return false;
	if (!ValidateTemp(sTemp)) return false;
	iStartTime =ConvertTimeToSecs(sStart);
	iEndTime =ConvertTimeToSecs(sEnd);
	if (iStartTime >= iEndTime) {
	    sErrorString = sErrorString + "ValidatePeriod: ERROR Start time (" + sStart + ") <= End Time (" + sEnd + ")\n";
	    return false;
	}
	// update period into period array   
	HeatPeriodParam[iPeriod][DAY] = String.valueOf(iDayOfWeek);	
	HeatPeriodParam[iPeriod][STARTTIME] = String.valueOf(iStartTime);
	HeatPeriodParam[iPeriod][ENDTIME] = String.valueOf(iEndTime);
	HeatPeriodParam[iPeriod][TEMP] = sTemp;
	return true;
    }
    
    /**
     * Convert Name of the day  of the week in to the Number of the day
     * @param sDay Name of day of the week
     * @return The Number of the Day of the week or -1 if not found
     */
    public int iValidateDay( String sDay)  {
	String WeekDay[] = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
	
	for (int i = 0; i < NOOFDAYSINWEEK; i++) {
	    if (sDay.equals(WeekDay[i])) return i;
	}
	sErrorString = sErrorString + "FAILED - SetDay:Day of week not found\n";
	return -1;
    }
        
    /**
     *  Convert the Start Time for a Period into secs
     * @param sStartTime  Time in format HH:SS
     * @return  True if successful
     */
    public boolean ValidateStart (String sStartTime) {
	int iStart = ConvertTimeToSecs(sStartTime);
	if (iStart < 0) {
	    sErrorString = sErrorString + "\nSetStart: Error in ConvertTimeToSecs=" + String.valueOf(iStart) + "\n";
	    return false;
	}			
	return true;
    }
    
    /**
     * Convert the End Time for a Period into secs
     * @param sEndTime Time in format HH:SS
     * @return True if successful
     */
    public boolean ValidateEnd (String sEndTime) {
	int iEnd = ConvertTimeToSecs(sEndTime);
	if (iEnd < 0) {
	    sErrorString = sErrorString + "\nSetEnd: Error in ConvertTimeToSecs=" + String.valueOf(iEnd)+ "\n";
	    return false;
	}	
		
	return true;
    }
    
    /**
     * Validate the temperture for a Period
     * @param sTemp
     * @return True if successful
     */
    public boolean ValidateTemp (String sTemp) {
	double dTemp;
	// Validate the temperture
	try {
	    dTemp = Double.parseDouble(sTemp);
	} catch (NumberFormatException e) { 
	    sErrorString = sErrorString +"\n setTemp: sTemp(" + sTemp + ") not numeric\n";
	    return false;  // Not numeric	
	}
	if (dTemp <0 || dTemp >50) {
	    sErrorString = sErrorString +"\n setTemp: sTemp(" + sTemp + ") not in range 0-50\n";
	    return false;  // Not numeric		    
	}	
	return true;
    }
         
    /**
     * Get the Start Time for a Period
     * @param iPeriod Heating Param Array
     * @return True is successful
     */
    public String GetStart (int iPeriod) {
	String TimeHHMMSS;
	if (!(HeatPeriodParam[iPeriod][STARTTIME].equals ("EMPTY"))) {
	    int iTimeInSecs = Integer.valueOf(HeatPeriodParam[iPeriod][STARTTIME]) ;
	    TimeHHMMSS = ConvertSecsToTime(iTimeInSecs);
	} else {
	    TimeHHMMSS = "EMPTY";
	}
	return (TimeHHMMSS);
    }
        
    /**
     * Get the End Time for a Period
     * @param iPeriod Index to Heating Param Array
     * @return True is successful
     */
    public String GetEnd(int iPeriod) {
	String TimeHHMMSS = ConvertSecsToTime(Integer.valueOf(HeatPeriodParam[iPeriod][ENDTIME]));
	return (TimeHHMMSS);
    }

    /**
     * Get the Temperature for a Period from the healing 
     * @param iPeriod Heating Param Array
     * @return String Temp required for heating period
     */
    public String GetTemp (int iPeriod) {
	String Temp = HeatPeriodParam[iPeriod][TEMP];
	return (Temp);
    }
    

/**
 * Convert the time in secs into string format HH:SS
 * @param longVal Time in secs
 * @return String Time in HH:SS
 */
    private String ConvertSecsToTime(int longVal)  {
	//Calculate HH 
	int hours = (int) longVal / 3600;
	int remainder = (int) longVal - hours * 3600;
	
	// Calculte MM
	int mins = remainder / 60;
	remainder = remainder - mins * 60;
	int secs = remainder;

	// Add leading zero to HH and MM
	String h = String.valueOf(hours);
	String hh = ("00" + h).substring(h.length());
	String m = String.valueOf(mins);
	String mm = ("00" + m).substring(m.length());
	
	String sReply = hh + ":" + mm; // Format Reply

	return sReply ;
    }    

    /**
     * Convert a string time format HH:SS into int Seconds
     * @param sTimeString Time in HH:SS
     * @return Time in seconds
     */
    private int ConvertTimeToSecs(String sTimeString)  {
	int iSecs;
	int iHH;
	int iMM;
	String[] sTime = sTimeString.split(":"); //split up reply into fields
	String sHH =sTime[0];
	String sMM =sTime[1];
	
	// Check the time is valid
	try {
	    iHH = Integer.parseInt(sHH);
	    iMM = Integer.parseInt(sMM);
	} catch (NumberFormatException e) { 
	    sErrorString = sErrorString +" ConvertTimeToSecs: sHH(" + sHH + ")/sMM("+sMM + ") not numeric\n";
	    return -1;  // No
	    
	}
	
	if (iHH <0 || iHH > 23) { // is Hours in valid range
	    sErrorString = sErrorString +" ConvertTimeToSecs: iHH (" + sHH + ") not 0-23\n";
	}
	if (iMM <0 || iMM > 59) { // Is Mins in valid range?
	    sErrorString = sErrorString +" ConvertTimeToSecs: iMM (" + sMM + ") not 0-60\n";
	}
	
	// Convert time into seconds
	iSecs = (iHH * 3600) + (iMM * 60);
	
	return iSecs;
    }
        
    /**
     * Clear the Error Message text
     */
    public void ClearErrorMsg () {
    /**
     * Save Period
     */
	sErrorString ="";
    }        
    
    /**
     * Return the Error message text
     * @return
     */
    public String ErrorMsg () {
	/**
	 * Return error description
	 */
	return (sErrorString);
    }
	    

    /**
     *  Close Function
     */
    public void close() {
        
    }  

}
