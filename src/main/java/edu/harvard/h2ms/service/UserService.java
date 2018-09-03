package edu.harvard.h2ms.service;

import edu.harvard.h2ms.domain.core.Event;
import edu.harvard.h2ms.domain.core.Question;
import edu.harvard.h2ms.domain.core.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService {
	
	public User findUserByEmail(String email);
	public User findUserByResetToken(String resetToken);
	public void save(User user);
	
	/**
	 * Prepares a key-value mapping of average compliance grouped by employee
	 * type.  The average compliance is how many times ansewr value is true, divided
	 * by total number of answers.
	 * 
	 * @param question
	 * @param events
	 * @return
	 */
	@Transactional(readOnly=true)
	public Map<String, Double> findComplianceByUserType(Question question, List<Event> events);
}
