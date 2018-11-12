/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.owl;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
/**
 * Provides Interface to  UDP Comms
 * @author Gary
 */
public class UDPInterface {
    private DatagramSocket socket;
    private InetAddress address;
 
    private byte[] Buf;
    private byte [] BufLth; 
 
    /**
     * Initialises UDP Interface
     * @throws Exception
     */
    public UDPInterface(String IpAddr) throws Exception {
        socket = new DatagramSocket();
        address = InetAddress.getByName(IpAddr);
    }
 
    /**
     * Send message via UDP attaching the OWL's UDP key to the output
     * @param msg Output message
     * @param sUDPKey Of OWL Device
     * @return Response from OWL Device
     * @throws Exception
     */
    public String sendMsg(String msg, String sUDPKey) throws Exception {
	String sSendMsg;
	sSendMsg = msg + ","+ sUDPKey;
        Buf = sSendMsg.getBytes();

        DatagramPacket packet;
	packet = new DatagramPacket(Buf,Buf.length, address, 5100);
        socket.send(packet);
	
	byte[] receiveData = new byte[1024];	
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	socket.setSoTimeout(10000);   // set the receive timeout in millisecounds.
        socket.receive(receivePacket);
        String received = new String( receivePacket.getData());

        return received;
    }
 
    /**
     *
     */
    public void close() {
        socket.close();
    }    
}
