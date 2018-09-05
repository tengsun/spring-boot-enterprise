package st.demo.batch;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.springframework.batch.item.validator.ValidationException;
import org.springframework.batch.item.validator.Validator;
import org.springframework.beans.factory.InitializingBean;

public class CsvBeanValidator<T> implements Validator<T>, InitializingBean {

	private javax.validation.Validator validator;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.usingContext().getValidator();
	}

	@Override
	public void validate(T value) throws ValidationException {
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(value);
		if (constraintViolations.size() > 0) {
			StringBuilder message = new StringBuilder();
			for (ConstraintViolation<T> violation : constraintViolations) {
				message.append(violation.getMessage() + "\n");
			}
			throw new ValidationException(message.toString());
		}
	}

}
