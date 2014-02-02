
public class ConnectionItem {
    private ChatRooms destination = ChatRooms.GENERAL;
    private String line;
    private String userName;
    
    public void setDestination(ChatRooms chNew){
    	destination = chNew;
    }
    
    public void setLine(String strNew){
    	line = strNew;
    }
    
    public ChatRooms getDestination(){
       return destination;	
    }
    
    public String getLine(){
       return line;	
    }
    
    public String getUserName(){
    	return userName;
    }
    
    public void setUserName(String strNew){
    	userName = strNew;
    }
}
