package edu.harvard.h2ms.repository;

import edu.harvard.h2ms.domain.core.Question;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuestionRepository extends PagingAndSortingRepository<Question, Long> {
  Question findByQuestion(String name);
}
