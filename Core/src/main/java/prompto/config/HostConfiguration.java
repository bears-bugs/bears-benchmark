package prompto.config;

public class HostConfiguration implements IHostConfiguration {

	protected IConfigurationReader reader;

	public HostConfiguration(IConfigurationReader reader) {
		this.reader = reader;
	}
	
	@Override
	public String toString() {
		return reader.toString();
	}

	@Override
	public String getHost() {
		return reader.getString("host");
	}
	
	@Override
	public Integer getPort() {
		return reader.getInteger("port");
	}
	
	
}
