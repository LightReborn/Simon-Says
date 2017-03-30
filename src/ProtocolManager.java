import java.net.*;

public class ProtocolManager {
    public static final int buffersize = 13;
  	public static final int lostframes = 5;
  	public static final int timeout = 10000;
    private final static int frame_arrival = 0;
    private final static int chksum_err = 1;
    private final static int timeout = 2;

  //from DataGram
    private int remotePort;
    private String remoteAddress;
    private DatagramSocket ds = null;
    private boolean arrived = false;
    private String info;
    private byte buffer[]=new byte[this.buffersize+4];   // Allow for 1 digit Ack and Seq

    public int seq=0;
    public int ack=0;

    private boolean timer = false;
    private int lostframes = 0;
    private int event = chksum_err;

//------------------------------------------------------------------------------------------------------
    ProtocolManager (String remoteAddress, int remotePort, int localPort){
        //constructor
        try { ds = new DatagramSocket(localPort);}
        catch (Exception e) {System.out.println("Exception " + e + " port " + localPort );}
        this.remoteAddress = remoteAddress;
        this.remotePort = remotePort;
    }//end constructor----------------------------------------------------------------------------------

//--------------------------------------------------------------------------------------------------------------------
//=============================================Private  Functions=====================================================
//--------------------------------------------------------------------------------------------------------------------

    private void start_receive() throws Exception{
        ds.setSoTimeout(this.timeout);
        this.info = "";
        DatagramPacket p = new DatagramPacket(buffer, buffer.length);
        ds.receive(p);
        this.info = new String(p.getData(), 0, 0, p.getLength());
    }//end start_receive

    private int wait_for_event(){
        int timelimit = 0;
        event = this.chksum_err;
        try { start_receive(); }
        catch(Exception e) { return this.timeout; }

        if(this.lostframes > 0 &&  ++lostframes % this.lostframes == 0) return this.timeout;
        event = this.frame_arrival;
        return this.frame_arrival;
    }// end wait_for_event

//--------------------------------------------------------------------------------------------------------------------
//==============================================Public Functions======================================================
//--------------------------------------------------------------------------------------------------------------------

    public void send(String message){//finished
      String s = "" + seq + " " + ack + " " + message; //prep the message for transmission
      byte buffer[]=new byte[s.length()]; //convert to byte
      s.getBytes(0, s.length(), buffer, 0);

      try { ds.send(new DatagramPacket(buffer, s.length(), InetAddress.getByName(remoteAddress), remotePort)); } //attempt to send
      catch (Exception e) { System.out.println("Exception " + e); }; //failed to send.
    } //end send

    public String receive(){//todo
      //Simply the function users will call. Handles event waiting and all.
      int newestEvent = wait_for_event(); //get our packet

      return this.info;
    }// end receive

    public void changeRemoteAddress(String address){//finished
        this.remoteAddress = address;
    } //end changeremoteAddress

    public void changeRemotePort(int port){//finished
      this.remotePort = port;
    } //end changeRemotePort

    public void changeLocalPort(int port){//finished
      try { ds = new DatagramSocket(port);}
      catch (Exception e) {System.out.println("Exception " + e + " port " + port );}
    }
}
