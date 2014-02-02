import java.net.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.*;

public class ClientThread extends Thread{
	private Socket socket = null;
	private PrintWriter out = null;
	private BufferedReader in = null;
	private boolean thRunnning = false;
    private ConcurrentLinkedQueue<ConnectionItem> inList; // "in" means into the server
//    private ConcurrentLinkedQueue<ConnectionItem> outList; // "out" means out of the server
    private ConnectState connectState;
    private String userName = ""; 
    private ChatRooms chatRoom = ChatRooms.GENERAL;
    private Server serv = null;
    private ClientConnection conn = null;
    
    public ClientThread(Socket socket, Server server, ClientConnection connec) {
        super("ClientThread");
        this.socket = socket;
        inList = new ConcurrentLinkedQueue<ConnectionItem>();
//        outList = new ConcurrentLinkedQueue<ConnectionItem>();
        connectState = ConnectState.INITIAL;
        serv = server;
        conn = connec;
    }

    public void run() {
    	thRunnning = true;
    	System.out.println("Running client thread.");
    	try{
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(
                    new InputStreamReader(
                        socket.getInputStream()));
                
            String inputLine;
    		out.println("You are connected to chat server.\r\n");
    		out.write("Login Name?\r\n");
    		out.flush();
    		while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
                
    			if (processLine(inputLine)){
    				out.write("BYE\r\n");
                    break;
    			}
    		}
    	   out.println("Quitting chat server.");
           socket.close();
           
    	}catch(IOException e){
    		
    	}
       System.out.println("Connection dropped.");
       thRunnning = false;
    }
    
    public boolean isRunning()
    {
       return thRunnning;	
    }
    
    // Process the next line received from the client.
    private boolean processLine(String line)
    {
    	if (line.toLowerCase().equals("/quit"))
    		return true;
    	
    	switch(connectState){
    	case INITIAL:
    		
   		if(checkLoginName(line) == false){
   			out.write("Sorry, name taken.\r\n");
    		out.write("Login Name?\r\n");
    		out.flush();
    		}
    		else{
    		   out.write("Welcome " + line + "\r\n");	
    		   out.flush();
               connectState = ConnectState.CONNECTED;
               userName = line;
    		}
  			
    		break;
    		
    	case CONNECTED:
        	if(line.toLowerCase().equals("/rooms")){
        	   	out.write("Active rooms are:\r\n");
        	   	out.write("* chat" + "\r\n");
        	   	out.write("* hottub" + "\r\n");
        	   	out.write("end of list.\r\n");
        	   	out.flush();
        	} 
            if(line.length() < 6){
            	break;
            }
    		if(line.substring(0, 5).equals("/join")){
 			   out.write("entering room: " + line.substring(6) + "\r\n");
 			   out.flush();
    		   if(line.substring(6).toLowerCase().equals("chat")){
    			   chatRoom = ChatRooms.CHAT;
    			   connectState = ConnectState.INROOM;
    	           serv.informInRoom(conn);    	  
    		   }
    		   else if(line.substring(6).toLowerCase().equals("hottub")){
    			   chatRoom = ChatRooms.HOTTUB;
    			   connectState = ConnectState.INROOM;
    	           serv.informInRoom(conn); 
    		   } 
    		   else{
    			   out.write("Invalid room name!\r\n");
    			   out.flush();
    		   }
    			   
    		}        	
    		break;
    		
    	case INROOM:
    		if(line.toLowerCase().equals("/leave")){
    			//out.write(userName + " has left " + "\r\n");
    			//out.flush();
    			connectState = ConnectState.CONNECTED;
    			serv.informLeftRoom(conn);
    			chatRoom = ChatRooms.GENERAL;
    			break;
    		}else{
            ConnectionItem item = new ConnectionItem();               		
            item.setLine(line);
            item.setDestination(chatRoom);
            item.setUserName(userName);
            inList.add(item);
    		}	
    		break;
    	}
    	
    	return false;
    }
    
    public ConnectionItem getNextInputItem()
    {
      return inList.remove();	
    }
    
    public boolean inputItemAvailable()
    {
       return !inList.isEmpty();	
    }
    
    public void insertOutputItem(ConnectionItem item)
    {
		out.write(item.getUserName() + ": " + item.getLine() + "\r\n");
		out.flush();
    }
    
    private boolean checkLoginName(String name)
    {
    	return serv.checkUserName(name);
    }
    
    public String getUserName()
    {
       return userName;   	
    }
    
    public ChatRooms getChatRoom()
    {
       return chatRoom;  	
    }
    
    public void textOut(String str){
    	out.write(str + "\r\n");
    	out.flush();
    }
}
