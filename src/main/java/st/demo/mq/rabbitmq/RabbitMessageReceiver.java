package st.demo.mq.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageReceiver {
	
	@RabbitListener(queues = "my-queue")
	public void receiveMessage(String message) {
		System.out.println("Rabbit received: " + message);
	}
	
}
