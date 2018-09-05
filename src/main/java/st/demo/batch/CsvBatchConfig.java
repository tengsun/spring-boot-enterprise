package st.demo.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.validator.Validator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class CsvBatchConfig {

	@Bean
	public ItemReader<Student> reader() throws Exception {
		FlatFileItemReader<Student> reader = new FlatFileItemReader<Student>();
		
		reader.setResource(new ClassPathResource("students.csv"));
		reader.setLineMapper(new DefaultLineMapper<Student>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"name", "age", "sex", "address"});
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Student>() {{
				setTargetType(Student.class);
			}});
		}});
		return reader;
	}
	
	@Bean
	public ItemProcessor<Student, Student> processor() {
		CsvItemProcessor processor = new CsvItemProcessor();
		processor.setValidator(csvBeanValidator());
		return processor;
	}
	
	@Bean
	public ItemWriter<Student> writer(DataSource dataSource) {
		JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
		
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
		String sql = "insert into student " + "(name, age, sex, address) " + 
			"values (:name, :age, :sex, :address)";
		writer.setSql(sql);
		writer.setDataSource(dataSource);
		return writer;
	}
	
	@Bean
	public JobRepository jobRepository(DataSource dataSource, 
			PlatformTransactionManager transactionManager) throws Exception {
		JobRepositoryFactoryBean jobRepositoryFactoryBean = new JobRepositoryFactoryBean();
		jobRepositoryFactoryBean.setDataSource(dataSource);
		jobRepositoryFactoryBean.setTransactionManager(transactionManager);
		jobRepositoryFactoryBean.setDatabaseType("mysql");
		return jobRepositoryFactoryBean.getObject();
	}
	
	@Bean
	public SimpleJobLauncher jobLauncher(DataSource dataSource, 
			PlatformTransactionManager transactionManager) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
		return jobLauncher;
	}
	
	@Bean
	public Job importJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("importJob")
				.incrementer(new RunIdIncrementer())
				.flow(s1)
				.end()
				.listener(csvJobListener())
				.build();
	}
	
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Student> reader, 
			ItemWriter<Student> writer, ItemProcessor<Student, Student> processor) {
		return stepBuilderFactory
				.get("step1")
				.<Student, Student>chunk(65000)
				.reader(reader)
				.processor(processor)
				.writer(writer)
				.build();
	}
	
	@Bean
	public Validator<Student> csvBeanValidator() {
		return new CsvBeanValidator<Student>();
	}
	
	@Bean
	public CsvJobListener csvJobListener() {
		return new CsvJobListener();
	}
	
}
