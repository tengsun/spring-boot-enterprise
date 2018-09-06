package st.demo.mq.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.core.MessageCreator;

public class MyMessage implements MessageCreator {

	@Override
	public Message createMessage(Session session) throws JMSException {
		return session.createTextMessage("This is a test message");
	}

}
