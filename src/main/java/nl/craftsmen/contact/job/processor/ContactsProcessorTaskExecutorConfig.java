package nl.craftsmen.contact.job.processor;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Beware for deadlocks: https://www.springcloud.io/post/2022-04/spring-threadpool/#gsc.tab=0
 */
@Configuration
@AllArgsConstructor
public class ContactsProcessorTaskExecutorConfig {

	private final int asyncThreadCorePoolSize;
	private final int asyncThreadMaxPoolSize;
	private final int asyncThreadQueueCapacity;

	@Bean
	public TaskExecutor getTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(asyncThreadCorePoolSize);
		executor.setMaxPoolSize(asyncThreadMaxPoolSize);
		executor.setQueueCapacity(asyncThreadQueueCapacity);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setThreadNamePrefix("MultiThreadedContactProcessorTaskExecutor-");
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.initialize();
		return executor;
	}
}
