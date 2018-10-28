package prompto.store;

public interface IStoredIterable extends Iterable<IStored> {
	
	long count();
	long totalCount();
	
}
