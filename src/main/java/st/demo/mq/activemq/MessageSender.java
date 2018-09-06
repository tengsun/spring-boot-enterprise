package st.demo.mq.activemq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender implements CommandLineRunner {
	
	@Autowired
	JmsTemplate jmsTemplate;

	@Override
	public void run(String... args) throws Exception {
		this.jmsTemplate.send("my-dest", new MyMessage());
	}

}
