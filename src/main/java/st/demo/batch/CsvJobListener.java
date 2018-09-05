package st.demo.batch;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CsvJobListener implements JobExecutionListener {

	long startTime;
	long endTime;

	@Override
	public void beforeJob(JobExecution arg0) {
		startTime = System.currentTimeMillis();
		System.out.println("Job started...");
	}

	@Override
	public void afterJob(JobExecution arg0) {
		endTime = System.currentTimeMillis();
		System.out.println("Job ended...");
		System.out.println("Total running: " + (endTime - startTime) + "ms");
	}

}
