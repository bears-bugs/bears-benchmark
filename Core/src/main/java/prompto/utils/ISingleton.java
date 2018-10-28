package prompto.utils;

public interface ISingleton<T> {
	void set(T instance);
	T get();
}
