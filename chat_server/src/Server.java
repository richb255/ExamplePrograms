import java.net.*;
//import java.util.*;
import java.util.concurrent.*;
import java.io.*;


public class Server {
	private CopyOnWriteArrayList<ClientConnection> connections = new CopyOnWriteArrayList<ClientConnection>();
	private ServerMonitorThread servMonitor; 
	
	public Server(){
		servMonitor = new ServerMonitorThread(connections);
	}
	
	public void run()
	{
		servMonitor.start();
		try(ServerSocket serverSocket = new ServerSocket(9399);)
          {
			
		  for(;;)
		   {
		   try{
			   Socket clientSocket = serverSocket.accept();
			   ClientConnection connection = new ClientConnection(clientSocket, this);
			   Thread.sleep(50);
			   if(connection.isRunning())
			   {
				   System.out.println("Client thread is running now.");
				   connections.addIfAbsent(connection);
			   }
			   
		   }catch(IOException | InterruptedException e){
			   
		   }

			  
		   }
          }catch(IOException e){
        	  
        	  System.exit(-1);
          }
		
		servMonitor.StopMonitor();
	}
	
	public boolean checkUserName(String name)
	{
		for(int i = 0; i < connections.size(); i++){
			
	       if(connections.get(i).GetThread().getUserName().toLowerCase().equals(name)){
	    	   return false;
	       }
		}
		return true;
	}
	
	public void informLeftRoom(ClientConnection conn){
		servMonitor.notifyLeftRoom(conn);
	}
	
	public void informInRoom(ClientConnection conn){
		servMonitor.notifyInRoom(conn);
	}
}
