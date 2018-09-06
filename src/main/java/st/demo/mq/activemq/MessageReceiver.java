package st.demo.mq.activemq;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageReceiver {

	@JmsListener(destination = "my-dest")
	public void receiveMessage(String message) {
		System.out.println("Message received: " + message);
	}
	
}
