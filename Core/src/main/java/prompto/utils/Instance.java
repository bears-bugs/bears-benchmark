package prompto.utils;

public class Instance<T> {
	
	T instance;
	
	public T get() {
		return instance;
	}
	
	public void set(T instance) {
		this.instance = instance;
	}
}
