package resources.cwe89sqlinjection;

import javax.persistence.EntityManager;

public class JPAInjection {

	private EntityManager entityManager;
	
	public void function1() {
		this.entityManager.createQuery("good");
	}
	public void function2(String param) {
		this.entityManager.createQuery("from entity where bad = '" + param + "'");
	}
	public void function3 (String param) {
		this.entityManager.createQuery("from entity where bad = '" + param + "'", String.class);
	}
	public void function4(String param) {
		this.entityManager.createNativeQuery("select * from table where bad='" + param + "'");
	}
	@SuppressWarnings("unqualified-field-access")
	public void function5() {
		entityManager.createQuery("from entity", String.class);
	}
	
	
}
