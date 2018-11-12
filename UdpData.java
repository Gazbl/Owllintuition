/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Takes a OWL heating or Electricity monitor buffer and splits it up into its fields
 * @author Gary
 */
public class UdpData {
    private int MAX_ARRAY_SIZE = 20;
   private String [] [] UdpMsgData = new String [2][MAX_ARRAY_SIZE];
    private  String HeatingFldName [] = {"heating ver",	    //Version of the data packet format. If not  present assume v1 
				"id",		    //The MAC Address of the Network OWL
				"timestamp",
				"zone id",	    //Device ID for the heating device 
				"last",		    //Seconds since the last report was received from this device
				"signal rssi",	    //Receive signal strength (dBm)
				"lqi",		    //Link quality (closer to 0 the better) 
				"battery level",    //Battery voltage reading in millivolts
				"conf flags",	    //Flag to show outstanding configuration to be pushed to the device.
						    //	0 = no outstanding configuration 
						    //	non-­‐zero = outstanding configuration 
				"temperature state",//Heating state for the  device:
						    //	0 = Standby 
						    //	1 = Comfort (Running) 
						    //	4 = Comfort (Up To Temperature) 
						    //	5 = Comfort (Warm Up) 
						    //	6 = Comfort (Cool Down) 
						    //	7 = Standby (Running) – Below the standby temperature
				"flags",	    //Heating system flags – Currently undefined. Temperature until Timestamp of the end of the current
				"until",	    //End of Heating period?
				"zone",		    //Zone id
				"current",	    //Current Room Temperature
				"required"	    //Rerquired Room Temperature
			    };
    private    String ElectFldName [] =	{"electricity id",  //The MAC Address of the Network OWL
				"timestamp",
				"signal rssi",	    //Receive signal strength (dBm)
				"lqi",	    //Link quality (closer to 0 the better)			
				"battery level",    //Percentage of battery remaining
				"chan id",	    //Transmitter channel number
				"curr units",       //Units for the value in the xml tag. wh = Watt Hours, divide by 1000 to get kWh w = watts
				"curr",
				"day units",	    //Units for the value in the xml tag. wh = Watt Hours, divide by 1000 to get kWh w = watts
				"day"
			    };
    private String Type;
    private String HeatVer;
    private String HeatId; 
    private String HeatTimestamp;
    private String HeatZoneId;
    private String HeatLast;
    private String HeatRssi;
    private String HeatLqi;
    private String HeatBattery;
    private String HeatConfFlag;
    private String HeatState;
    private String HeatFlag;
    private String HeatUntil;
    private String HeatZone;
    private String HeatCurr;
    private String HeatReqd;
    private String ElectId;
    private String ElectTimestamp;
    private String ElectRssi;
    private String ElectLqi;
    private String ElectBattery;
    private String ElectChanId;
    private String ElectCurrUnits;
    private String ElectCurr;
    private String ElectDayUnits;
    private String ElectDay;
    private String Msg;

   
    public UdpData ()
     {	 
     }
    
    public Boolean ParseUDPMsg (String UdpMsg) {
	if (UdpMsg.substring(1,6).equals("elect")) { 
	    
	    if (false == FindTaggedField(UdpMsg,ElectFldName,UdpMsgData)) { // Find and Store Elect values 
		return false;
	    } else {
		Type = "elect";
		ElectId = sDataFldValue(UdpMsgData,"electricity id");
		ElectTimestamp = sDataFldValue(UdpMsgData,"timestamp");
		ElectRssi = sDataFldValue(UdpMsgData,"signal rssi");
		ElectLqi = sDataFldValue(UdpMsgData,"lqi");
		ElectBattery = sDataFldValue(UdpMsgData,"battery level");
		ElectChanId = sDataFldValue(UdpMsgData,"chan id");
		ElectCurrUnits = sDataFldValue(UdpMsgData,"curr units");
		ElectCurr = sDataFldValue(UdpMsgData,"curr");
		ElectDayUnits = sDataFldValue(UdpMsgData,"day units");
		ElectDay = sDataFldValue(UdpMsgData,"day");
		
	    }
	} else if (UdpMsg.substring(1,5).equals("heat")) {
	     
	    if (false ==  FindTaggedField(UdpMsg,HeatingFldName, UdpMsgData)) { // Find and Store heating values
		return false;
	    } else {
		Type = "heat";
		HeatVer = sDataFldValue(UdpMsgData,"heating ver");
		HeatId = sDataFldValue(UdpMsgData,"id");
		HeatTimestamp = sDataFldValue(UdpMsgData,"timestamp");
		HeatZoneId = sDataFldValue(UdpMsgData,"zone id");
		HeatLast = sDataFldValue(UdpMsgData,"last");
		HeatRssi= sDataFldValue(UdpMsgData,"signal rssi");
		HeatLqi = sDataFldValue(UdpMsgData,"lqi");
		HeatBattery = sDataFldValue(UdpMsgData,"battery level");
		HeatConfFlag = sDataFldValue(UdpMsgData,"conf flags");
		HeatState = sDataFldValue(UdpMsgData,"temperature state");
		HeatFlag = sDataFldValue(UdpMsgData,"flags");
		HeatUntil = sDataFldValue(UdpMsgData,"until");
		HeatZone = sDataFldValue(UdpMsgData,"zone");
		HeatCurr = sDataFldValue(UdpMsgData,"current");
		HeatReqd = sDataFldValue(UdpMsgData,"required");		
	    }
	    
	} else {
	    return false;
	}
	return true;
    }
    
    /**
     *
     * @return 
     */
    public String GetType ()
    {
	return Type;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatVer ()
    {
	return HeatVer;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatId ()
    {
	return HeatId;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatTimestamp ()
    {
	long unix_seconds;
	//Unix seconds
	unix_seconds =  Integer.parseInt(HeatTimestamp);
	//convert seconds to milliseconds
	Date date = new Date(unix_seconds*1000L); 
	// format of the date
	SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	return jdf.format(date);	
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatZoneId ()
    {
	return HeatZoneId;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatLast ()
    {
	return HeatLast + "secs";
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatRssi ()
    {
	return HeatRssi +"dBm";
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatLqi ()
    {
	return HeatLqi;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatBattery ()
    {
	return HeatBattery;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatConfFlag ()
    {
	return HeatConfFlag;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatState ()
    {
	switch (HeatState) { //Heating state for the  device:
	case "0":    //	0 = Standby 
	    return("Standby"); 
	case "1":  //	1 = Comfort (Running) 			
	    return("Comfort (Running)"); 
	case "4": //	4 = Comfort (Up To Temperature)
	    return("Comfort (Up To Temperature)");	
	case "5":  //	5 = Comfort (Warm Up)   
	    return("Comfort (Warm Up)");	
	case "6": //	6 = Comfort (Cool Down) 
	    return("Comfort (Cool Down)");	  
	case "7": //	7 = Standby (Running) – Below the standby temperature	
	    return("Standby");  
	default:
	    return ("Unknown State");
        }	
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatFlag ()
    {
	return HeatFlag;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatUntil ()
    {
	long unix_seconds;	
	//Unix seconds
	unix_seconds =  Integer.parseInt(HeatUntil);
	//convert seconds to milliseconds
	Date date = new Date(unix_seconds*1000L); 
	// format of the date
	SimpleDateFormat jdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");	
	return jdf2.format(date);
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatZone ()
    {
	return HeatZone;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatCurr ()
    {
	return HeatCurr;
    }
    
    /**
     *
     * @return 
     */
    public String GetHeatReqd ()
    {
	return HeatReqd;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectId ()
    {
	return ElectId;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectTimestamp ()
    {
	long unix_seconds;
	//Unix seconds
	unix_seconds =  Integer.parseInt(ElectTimestamp);
	//convert seconds to milliseconds
	Date date = new Date(unix_seconds*1000L); 
	// format of the date
	SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
	return jdf.format(date);
    }
    
    /**
     *
     * @return 
     */
    public String GetElectRssi ()
    {
	return ElectRssi + "dBm";
    }
    
    /**
     *
     * @return 
     */
    public String GetElectLqi ()
    {
	return ElectLqi;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectBattery ()
    {
	return ElectBattery;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectChanId ()
    {
	return ElectChanId;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectCurrUnits ()
    {
	return ElectCurrUnits;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectCurr ()
    {
	return ElectCurr;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectDayUnits ()
    {
	return ElectDayUnits;
    }
    
    /**
     *
     * @return 
     */
    public String GetElectDay ()
    {	
	try {
	    float d = Float.parseFloat(ElectDay);
	    d = d/1000;
	    return String.valueOf(d) + "k";	
	} catch (NumberFormatException e) {
            return "Unavailable";
        }

    }
    
    /**
     *
     * @return 
     */
    public String GetMsg ()
    {
	return Msg;
    }
private boolean FindTaggedField (String sBuffer,String FieldNames [],  String FieldValues [] []  ) {
    String FieldTag;
    String [] ElectFieldsArray;
    ElectFieldsArray = new String[100];
    String Fld;  
    String s;
    //<electricity id='44371900090A'><timestamp>1519044462</timestamp><signal rssi='-58' lqi='15'/><battery level='100%'/><chan id='0'><curr units='w'>966.00</curr>
    //<day units='wh'>12772.80</day></chan><chan id='1'><curr units='w'>0.00</curr><day units='wh'>0.00</day></chan>
    //<chan id='2'><curr units='w'>0.00</curr><day units='wh'>0.00</day></chan></electricity>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

    
    try {
	//toXmlString (sBuffer);
	// the pattern we want to search for
	FieldTag="[^(<'>/)](.*?)['<>=]"; // Regex patteren to isloate fields and values
	Pattern p = Pattern.compile(FieldTag);  
	
	//Process Electricty Data Packet
	int i =0;
	for (String FldName : FieldNames) { // Does Field match the name of a required field
	    // if we find a match, get the group 
	    Matcher m = p.matcher(sBuffer);
	    int lth;
	    while(m.find()){
		lth=m.group().length();  // Length of field found
		if (m.group().substring(0,1).equals(" ")) {
		    Fld = m.group().substring(1, lth-1); // Store field minus ;rsfmomh and trailing delimiter
		} else {
		    Fld = m.group().substring(0, lth-1); // Store field minus trailing delimiter
		}	    

	        if (Fld.equals(FldName)) {
		    //for (String ExistFldName : ElectData[0]) {  // Check if field alread found
		    //    if (!Fld.equals(ExistFldName)){			    
		    FieldValues[0][i] = Fld;
		    m.find();
		    lth=m.group().length();  // Length of Value field found
		    FieldValues[1][i] = m.group().substring(0, lth-1);
		    // handle subsequesnt field e.g. <curr units='w'>1449.00</curr>
		    m.find();		    
		    lth=m.group().length();  // Length of Value field found
		    s =  m.group().substring(0, lth-1);
		    if (s.charAt(0) <= '9' && s.charAt(0) >= '0') {
			i++;
			FieldValues[1][i] = m.group().substring(0, lth-1);
			m.find();		    
			lth=m.group().length();  // Length of Value field found
			FieldValues[0][i] = m.group().substring(0, lth-1);
		    }
		    i++;
		    break;
		}
	    }
	}       
    } catch (Exception e) {
 	Msg = Msg + this.getClass().getName() + ".FindHeatingTaggedField:" + e.getMessage();
	return false;
    }	    
    return true;
}                                                                                                                              

    
    /**
     * Find the corresponding value for a data Field in Heating or Elect array
     *@return String Field Value 
     */
    String sDataFldValue (String [][] DataFld, String sFieldName) {
	String s;
	for (int i=0; i<MAX_ARRAY_SIZE; i++) { // Does Field match the name of a required field
	    if (DataFld[0][i].equals(sFieldName)) {
		return DataFld[1][i];
	    }	
	}
	return "UNKOWN";
    }
}