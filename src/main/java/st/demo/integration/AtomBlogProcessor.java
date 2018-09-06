package st.demo.integration;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndEntry;

@Component
public class AtomBlogProcessor {

	@Value("http://spring.io/blog.atom")
	Resource resource;
	
	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerMetadata poller() {
		return Pollers.fixedRate(500).get();
	}
	
	@Bean
	public FeedEntryMessageSource feedEntryMessageSource() throws IOException {
		FeedEntryMessageSource messageSource = 
			new FeedEntryMessageSource(resource.getURL(), "news");
		return messageSource;
	}
	
	@Bean
	public IntegrationFlow myFlow() throws IOException {
		return IntegrationFlows.from(feedEntryMessageSource())
			.<SyndEntry, String>route(
				payload -> payload.getCategories().get(0).getName(),
				mapping -> mapping.channelMapping("releases", "releasesChannel")
								  .channelMapping("engineering", "engineeringChannel")
								  .channelMapping("news", "newsChannel")
			).get();
	}
	
	@Bean
	public IntegrationFlow releasesFlow() {
		return buildFlow("releases");
	}
	
	@Bean
	public IntegrationFlow engineeringFlow() {
		return buildFlow("engineering");
	}
	
	@Bean
	public IntegrationFlow newsFlow() {
		return buildFlow("news");
	}
	
	private IntegrationFlow buildFlow(String channelName) {
		return IntegrationFlows.from(MessageChannels.queue(channelName + "Channel", 10))
				.<SyndEntry, String>transform(
					payload -> "<<" + payload.getTitle() + ">> " + payload.getLink() + System.getProperty("line.separator"))
				.handle(Files.outboundAdapter(new File("/Users/tengsun/Downloads"))
						.fileExistsMode(FileExistsMode.APPEND)
						.charset("UTF-8")
						.fileNameGenerator(message -> channelName + ".txt")
						.get()
				).get();
	}
	
}
