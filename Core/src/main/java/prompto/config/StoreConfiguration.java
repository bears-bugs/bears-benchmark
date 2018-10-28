package prompto.config;

public class StoreConfiguration extends IStoreConfiguration.Inline {

	protected IConfigurationReader reader;

	public StoreConfiguration(IConfigurationReader reader) {
		this.reader = reader;
		this.factory = ()->reader.getString("factory");
		this.host = ()->reader.getString("host");
		this.port = ()->reader.getInteger("port");
		this.dbName = ()->reader.getString("dbName");
		this.user = ()->reader.getString("user");
		this.secretKey = ()->reader.readSecretKeyConfiguration("secretKey");
	}
	
	@Override
	public String toString() {
		return reader.toString(); // TODO call lambas since reader may have been overridden
	}
	
}
