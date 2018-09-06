package st.demo.mq.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageSender implements CommandLineRunner {
	
	@Autowired
	RabbitTemplate rabbitTemplate;
	
	@Bean
	public Queue myQueue() {
		return new Queue("my-queue");
	}

	@Override
	public void run(String... args) throws Exception {
		this.rabbitTemplate.convertAndSend("my-queue", "A message to test RabbitMQ!");
	}

}
