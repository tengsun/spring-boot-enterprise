package st.demo.batch;

import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.batch.item.validator.ValidationException;

public class CsvItemProcessor extends ValidatingItemProcessor<Student> {

	@Override
	public Student process(Student item) throws ValidationException {
		if (item.getSex().equals("Male")) {
			item.setSex("M");
		} else {
			item.setSex("F");
		}
		return item;
	}
}
