package edu.harvard.h2ms.domain.core;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An Event is what observer or sensor records about observee's actions.
 */
@Entity
@Table(name = "EVENT")
public class Event {
	/* Properties */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private Long id;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column
	private Date timestamp;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id")
	private User subject;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "event_template_id")
	private EventTemplate eventTemplate;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "observer_id")
	private User observer;

	// TODO: Place holder until Location model is ready + updated according to data
	// model
	@NotNull
	@Column
	private String location;

	@Valid
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "event")
	private Set<Answer> answers = new HashSet<>();

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		answers.forEach((a) -> {
			a.setEvent(this);
		});

		this.answers = answers;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public User getSubject() {
		return subject;
	}

	public void setSubject(User subject) {
		this.subject = subject;
	}

	public User getObserver() {
		return observer;
	}

	public void setObserver(User observer) {
		this.observer = observer;
	}

	public EventTemplate getEventTemplate() {
		return eventTemplate;
	}

	public void setEventTemplate(EventTemplate eventTemplate) {
		this.eventTemplate = eventTemplate;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Returns the answer for a specific question in an Event.
	 * 
	 * @param question
	 * @return answer to that question, or null if one is not present
	 */
	public Answer getAnswer(Question question) {
		return this.getAnswers().stream().filter(q -> q.getQuestion().equals(question)).findFirst().orElse(null);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Event [id=").append(id).append(", timestamp=").append(timestamp).append(", subject=")
				.append(subject).append(", eventTemplate=").append(eventTemplate).append(", observer=").append(observer)
				.append(", location=").append(location).append(", answers=").append(answers).append("]");
		return builder.toString();
	}
}
