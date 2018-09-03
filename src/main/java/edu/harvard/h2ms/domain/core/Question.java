package edu.harvard.h2ms.domain.core;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Question
 */
@Entity
public class Question {

	/* Properties */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	@NotNull
	@Column
    private Integer priority;

    @ElementCollection
    private List<String> options;

    @NotNull
    @Column
    private String question;

    @NotNull
	@Column
    private Boolean required;

    @NotNull
    @Column
    private String answerType;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_template_id")
	private EventTemplate eventTemplate;
	
	public Question() {
        super();
    }
	
	public Question(String question, String answerType, List<String> options, Boolean required, Integer priority, EventTemplate eventTemplate) {
		super();
		this.question = question;
		this.answerType = answerType;
		this.options = options;
		this.required = required;
		this.priority = priority;
		this.eventTemplate = eventTemplate;
	}

	public String getQuestion() {
		return question;
	}


	public void setQuestion(String question) {
		this.question = question;
	}

	public EventTemplate getEventTemplate() {
		return eventTemplate;
	}

	public void setEventTemplate(EventTemplate eventTemplate) {
		this.eventTemplate = eventTemplate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Question [id=").append(id).append(", priority=").append(priority).append(", options=")
				.append(options).append(", question=").append(question).append(", required=").append(required)
				.append(", answerType=").append(answerType).append("]");
		return builder.toString();
	}
}
