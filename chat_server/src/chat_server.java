/*
 * Author: Richard A. Buttars
 * This file contains the entry point of the server (the main() function).
 * The main intent of this code is to provide a public example of code that
 * I have written. As such this code is available to anyone to read or use
 * but I reserve the right to create derived works from it if I want.
 */


public class chat_server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        System.out.println("chat server ver. 1.00");
        
        Server serv = new Server();
        
        serv.run();
	}

}
