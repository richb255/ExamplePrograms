import java.util.concurrent.*;

public class ServerMonitorThread extends Thread{
	   private CopyOnWriteArrayList<ClientConnection> connections;
	   private boolean bstop = false;
	
	   public ServerMonitorThread(CopyOnWriteArrayList<ClientConnection> conn){
		   connections = conn;
	   }
	   
	   public void run() {
		   int old_size = 0;
		   for(;;){
			   if(bstop){
				   break;
			   }
			      for(int i = 0; i < connections.size(); i++){
				         ClientConnection conn = connections.get(i);
				         if(!conn.isRunning()){
				        	 notifyLeftRoom(conn);
				        	 connections.remove(i);
				        	 System.out.println("One connection removed from list.");
				        	 old_size = connections.size();
				        	 continue;
				         }
				         if(connections.size() > old_size){
				        	 
				         }
				         old_size = connections.size();
				         if(conn.GetThread().inputItemAvailable()){
				        	 ConnectionItem item = conn.GetThread().getNextInputItem();
						      for(int j = 0; j < connections.size(); j++){
						    	     if(i == j){
						    	    	 continue;
						    	     }
							         ClientConnection connec = connections.get(j);
                                     if(item.getDestination() == connec.GetThread().getChatRoom()){
                                    	 connec.GetThread().insertOutputItem(item);
                                     }
						      }
				         }
				      }

			      
		   }
		   
	   }
	   
	   public void StopMonitor(){
	      bstop = true;	   
	   }
	 
       public void notifyLeftRoom(ClientConnection connec){
		      for(int i = 0; i < connections.size(); i++){
			         ClientConnection conn = connections.get(i);
			     if(connec.GetThread().getChatRoom() == conn.GetThread().getChatRoom()){
			    	 conn.GetThread().textOut(connec.GetThread().getUserName() + " has left the room.");
			     }
		      }
       }
       
       public void notifyInRoom(ClientConnection connec){
		      for(int i = 0; i < connections.size(); i++){
			         ClientConnection conn = connections.get(i);
			     if(connec.GetThread().getChatRoom() == conn.GetThread().getChatRoom()){
			    	 conn.GetThread().textOut(connec.GetThread().getUserName());
			     }
		      }
    }
}
