package edu.harvard.h2ms.domain.core;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * An answer contains the response 
 */
@Entity
public class Answer {

    /* Properties */
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private Question question;
    
    @NotNull
    @Column
    private String answerType;
    
	@Column
    private String value;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "event_id")
	private Event event;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Answer [id=").append(id).append(", question=").append(question).append(", answerType=")
				.append(answerType).append(", value=").append(value).append("]");
		return builder.toString();
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
		this.answerType = question.getAnswerType();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
    public String getAnswerType() {
		return answerType;
	}

	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}
}
