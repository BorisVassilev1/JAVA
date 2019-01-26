package org.boby.socketIOtest3;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Hello world!
 *
 */
public class BasicExample 
{
    public static void main( String[] args ) throws URISyntaxException
    {
        //System.out.println( "Hello World!" );
    	final Socket socket = IO.socket("http://localhost:3000");
    	socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

    	  public void call(Object... args) {
    	    socket.emit("foo", "hi");
    	    System.out.println("connection: " + socket.id());
    	    //socket.disconnect();
    	  }

    	}).on("event", new Emitter.Listener() {
    	  public void call(Object... args) {
    		  System.out.println("recieved a message from the server: ");
    		  for (Object object : args) {
				System.out.println(object);
			}
    	  }
    	  

    	}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

    	  public void call(Object... args) {
    		  System.out.println("disconnected: " + socket.id());
    	  }

    	});
    	socket.connect();
    }
}
