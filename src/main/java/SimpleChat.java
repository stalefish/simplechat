import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleChat extends ReceiverAdapter {
	JChannel channel;
	String user_name=System.getProperty("user.name","n/a");

	private void start() throws Exception {
	    channel = new JChannel().setReceiver(this);
	    channel.connect("ChatCluster");
	    eventLoop();
	    channel.close();
    }

    private void eventLoop() {
	    BufferedReader in =new BufferedReader(new InputStreamReader(System.in));
	    while(true) {
	        try {
	            System.out.print("> ");
	            System.out.flush();
	            String line = in.readLine().toLowerCase();
	            if ( line.startsWith("quit") || line.startsWith("exit") ) {
	                break;
                }
                line = "[" + user_name + "] " + line;
	            Message msg = new Message(null,line);
	            channel.send(msg);
            } catch (Exception ex) {

            }
        }
    }

    public void viewAccepted(View new_view) {
	    System.out.println("** view: " + new_view);
    }

    public void receive(Message message) {
	    System.out.println(message.getSrc() + ": " + message.getObject());
    }

    public static void main(String[] args) throws Exception {
	    new SimpleChat().start();
    }
}