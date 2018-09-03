package edu.harvard.h2ms.service;

import org.springframework.stereotype.Service;
import com.google.common.collect.Lists;
import edu.harvard.h2ms.domain.core.*;
import edu.harvard.h2ms.repository.EventRepository;
import edu.harvard.h2ms.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import edu.harvard.h2ms.repository.UserRepository;
import edu.harvard.h2ms.service.utils.H2msRestUtils;

import org.springframework.transaction.annotation.Transactional;
import static edu.harvard.h2ms.service.utils.H2msRestUtils.calculateAverage;

@Service("userService")
@Repository
@Transactional
public class UserServiceImpl implements UserService { 
  final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
  @Autowired
	private UserRepository userRepository;
	
	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	
	@Override
	public User findUserByResetToken(String resetToken) {
		return userRepository.findByResetToken(resetToken);
	}
	
	@Override
	public void save(User user) {
		userRepository.save(user);
	}
	
	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public Map<String, Double> findComplianceByUserType(Question question, List<Event> events) {
		Map<String, Double> complianceResult = new HashMap<>();
		
		// Fetches all users from H2MS database
		List<User> users = Lists.newArrayList(userRepository.findAll());
		log.info("No. of users found: {}", users.size());
		if(users.isEmpty()) { return complianceResult; }

		// Determines all the distinct types of users
		List<String> distinctUserTypes = users.stream().map(User::getType).collect(Collectors.toList());
		log.info("There are {} distinct user types ", distinctUserTypes.size());
		if(distinctUserTypes.isEmpty()) {
			return complianceResult;
		}

		for (String type : distinctUserTypes) {
			if(type == null)
				continue;

			complianceResult.put(
					type,
					H2msRestUtils.calculateCompliance(question,
					events.stream()
						.filter(event -> event.getSubject().getType().equals(type))
						.collect(Collectors.toSet()))
					);
			
		}
		
		return complianceResult;
	}

}
