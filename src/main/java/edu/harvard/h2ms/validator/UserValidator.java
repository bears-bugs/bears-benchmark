package edu.harvard.h2ms.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import edu.harvard.h2ms.domain.core.User;
import edu.harvard.h2ms.service.UserService;

@Component
public class UserValidator implements Validator {
	@Autowired
	private UserService userService;
	
	@Override
	public boolean supports(Class<?> aClass) {
		return User.class.equals(aClass);
	}
	
	@Override
	public void validate(Object o, Errors errors) {
		User user = (User) o;
		
		if (!user.getPasswordConfirm().equals(user.getPassword())){
			errors.rejectValue("passwordConfirm",  "Diff.userform.passwordConfirm");
		}
	}

}
