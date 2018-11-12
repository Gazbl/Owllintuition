/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Handles Multicast messages from OWL Device
 * @author Gary
 */
public class MulticastSocketClient extends SwingWorker<Integer, UdpData>{    
    final static String INET_ADDR = "224.192.32.19"; // OWL multicast broadcast address
    final static int PORT = 22600; // OWL multicast broadcast port
    private String sDebug ="Init";
    private int iCountP =0;
    // Text fields on Window UI
    private final JTextArea  HeatingExplantion;
    private final JTextField CurrTempTextField;
    private final JTextField ReqTempTextField;
    private final JTextField HeatTimestampTextField;
    private final JTextField HeatZoneIdTextField;
    private final JTextField HeatLastTextField;
    private final JTextField HeatRssiTextField;
    private final JTextField HeatLqiTextField;
    private final JTextField HeatConfFlagTextField;
    private final JTextField HeatUntilTextField;
    private final JTextField HeatingStateTextField;
    private final JTextField BatteryTextField;   
    
    private final JTextField ElecMacTextField;
    private final JTextField ElecTimestampTextField;
    private final JTextField ElecRssiTextField;
    private final JTextField ElecLqiTextField;
    private final JTextField ElecBattTextField;
    private final JTextField ElecChanTextField;
    private final JTextField ElecCurrTextField;
    private final JTextField ElecDayTextField;    
    private MulticastSocket clientSocket;
    private String ErrorMsg; // Description of any error returned from class

    private final JTextField jTextFieldHeatState;
    /**
     * Initialise Multicast interface
     * @param jTextFieldCurrTemp
     * @param jTextFieldReqTemp
     * @param jTextFieldHeatState
     * @param jTextFieldBatt
     * @throws Exception
     */
    public MulticastSocketClient(   final JTextArea  jTextAreaHeatingExplantion,
				    final JTextField jTextFieldCurrTemp,
				    final JTextField jTextFieldReqTemp,
				    final JTextField jTextFieldHeatState,
				    final JTextField jTextFieldBatt,
				    final JTextField jTextFieldHeatTimestamp,
				    final JTextField jTextFieldHeatZone,
				    final JTextField jTextFieldHeaLast,	    
				    final JTextField jTextFieldHeatRssi,
				    final JTextField jTextFieldHeatLqi,
				    final JTextField jTextFieldHeatConfFlag,
				    final JTextField jTextFieldHeatUntil,				    
				    final JTextField jTextFieldElectMac,
				    final JTextField jTextFieldElectTimestamp,
				    final JTextField jTextFieldElectRssi,
				    final JTextField jTextFieldElectLqi,
				    final JTextField jTextFieldElectBatt,
				    final JTextField jTextFieldElectChan,
				    final JTextField jTextFieldElectCurr,
				    final JTextField jTextFieldElectDay		    
				    ) {
	String text = null;
	this.HeatingExplantion = jTextAreaHeatingExplantion;
	this.CurrTempTextField = jTextFieldCurrTemp;
	this.ReqTempTextField = jTextFieldReqTemp;
	this.HeatUntilTextField = jTextFieldHeatUntil;
	this.HeatZoneIdTextField = jTextFieldHeatZone;
	this.HeatLastTextField = jTextFieldHeaLast;
	this.HeatRssiTextField = jTextFieldHeatRssi;
	this.HeatLqiTextField = jTextFieldHeatLqi;
	this.HeatConfFlagTextField = jTextFieldHeatConfFlag;
	this.HeatingStateTextField = jTextFieldHeatState;
	this.BatteryTextField = jTextFieldBatt;
	this.HeatTimestampTextField = jTextFieldHeatTimestamp;
	this.ElecMacTextField = jTextFieldElectMac;
	this.ElecTimestampTextField =jTextFieldElectTimestamp;
	this.ElecRssiTextField = jTextFieldElectRssi;
	this.ElecLqiTextField = jTextFieldElectLqi;
	this.ElecBattTextField = jTextFieldElectBatt;
	this.ElecChanTextField = jTextFieldElectChan;
	this.ElecCurrTextField = jTextFieldElectCurr;
	this.ElecDayTextField = jTextFieldElectDay;	


	this.jTextFieldHeatState = jTextFieldHeatState;
    }
      
    @Override
    protected Integer doInBackground() throws Exception {
	// Do thread processing
	// Get the address that we are going to connect to.
	try {
	    InetAddress address = InetAddress.getByName(INET_ADDR);      
	    // Create a new Multicast socket (that will allow other sockets/programs
	    // to join it as well.
	    clientSocket = new MulticastSocket(PORT);
	    //Joint the Multicast group.
	    clientSocket.joinGroup(address);
	} catch (Exception ignore) {
	    ErrorMsg = ErrorMsg + "MulticastSocketClient Exception";
        }	

	while (Multicast()) {
	};
        return 1;
    }

    /**
     *
     * @param chunks
     */
    @Override
    protected void process(final List<UdpData> chunks) {    

	// Updates the messages text area
	//HeatingExplantion.setText(sDebug +" " + String.valueOf(iCountP++));
	for (final UdpData DataFlds  : chunks) {
	    if (DataFlds.GetType() == "elect") {
		ElecMacTextField.setText(DataFlds.GetElectId());
		ElecTimestampTextField.setText(DataFlds.GetElectTimestamp()); 
		ElecRssiTextField.setText(DataFlds.GetElectRssi());
		ElecLqiTextField.setText(DataFlds.GetElectLqi());
		ElecBattTextField.setText(DataFlds.GetElectBattery());
		ElecChanTextField.setText(DataFlds.GetElectChanId());
		ElecCurrTextField.setText(DataFlds.GetElectCurr()+DataFlds.GetElectCurrUnits());
		ElecDayTextField.setText(DataFlds.GetElectDay()+DataFlds.GetElectDayUnits());	
		HeatingExplantion.setText(DataFlds.GetMsg());
	    } else if (DataFlds.GetType() == "heat") {
		HeatTimestampTextField.setText(DataFlds.GetHeatTimestamp());
		CurrTempTextField.setText(DataFlds.GetHeatCurr());
		ReqTempTextField.setText(DataFlds.GetHeatReqd());
		HeatingStateTextField.setText(DataFlds.GetHeatState()); 
		BatteryTextField.setText(DataFlds.GetHeatBattery()+"mV");
		HeatConfFlagTextField.setText(DataFlds.GetHeatConfFlag());
		HeatZoneIdTextField.setText(DataFlds.GetHeatZoneId());
		HeatLastTextField.setText(DataFlds.GetHeatLast());
		HeatRssiTextField.setText(DataFlds.GetHeatRssi());
		HeatLqiTextField.setText(DataFlds.GetHeatLqi());
		HeatUntilTextField.setText(DataFlds.GetHeatUntil());		
		HeatingExplantion.setText(DataFlds.GetMsg());
	    } 
	}
    }    
    
    @Override
    protected void done() {
        String text = null;
	ErrorMsg = ErrorMsg + "MulticastSocketClient done";
        try {
            Integer i = get();
        } catch (Exception ignore) {

            text = "done unavailable";
        }
    }
    

    /**
     * Obtains Mulitcast message from OWL deive <br>
     * Heating id <tr> The MAC Address of the Network OWL <br>
     * Heating ver Version of the data packet format. If not present assume v1 <br>
     * Zone id Device ID for the heating device <br>
     * Zone last  Seconds since the last report was received from this device <br>
     * Signal rssi Receive signal strength  (dBm)<br>
     * Signal lqi Link quality (closer to 0 the better) <br>
     * Battery level Battery voltage reading  in millivolts <br>
     * Conf flags Flag to  show outstanding configurationto be pushed to the  device.<br>
     *	0  =no outstanding configuration <br>
     * non-­‐zero =  outstanding configuration <br>
     * Temperature state Heating state for the device:  <br>
     *	0 = Standby  <br>
     *	1= Comfort (Running)  <br>
     *	4 =  Comfort (UpTo Temperature)  <br>
     *	5 = Comfort  (WarmUp)  <br>
     *	6= Comfort  (Cool Down) <br>
     *	7 = Standby  (Running) –Below  the standby temperature  <br>
     * Temperature flags Heating system flags  –Currently undefined.  <br>
     * Temperature until Timestamp  of the end of the current comfort/standby period  <br>
     * Temperature Zone Current active heating zone <br>
     *  <br>
     * Example Resp "<heating ver='2' id='23452345234'>
     * <timestamp>1517140677</timestamp><zones><zone id='20002BA' last='1'>
     * <signal rssi='-66' lqi='51'/><battery level='2770'/><conf flags='0'/>
     * <temperature state='4' flags='1' until='1517164200' zone='63'>
     * <current>19.62</current><required>19.00</required></temperature></zone></zones></heating>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             "
     * @return Mutlicast Message 
     * @throws Exception
     */
    public  boolean Multicast( )  throws Exception { 
	boolean Reply;
        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.

        byte[] buf = new byte[4096];
        DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);

//	clientSocket.setSoTimeout(5);
        clientSocket.receive(msgPacket);
	sDebug = "Multicast 3";
        //String msg = new String(buf, 0, buf.length); // Convert msg buffer to string
	String msg = new String ( msgPacket.getData());
	UdpData Updflds = new UdpData();
	Updflds.ParseUDPMsg(msg);
	publish(Updflds);

	sDebug = "Multicast 8"+" Port=" + msgPacket.getPort()+ " Lth=" + msgPacket.getLength() +"\n Buffer="+  new String( msgPacket.getData()) ;
	return true;
    }
}
