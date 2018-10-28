package prompto.intrinsic;

import java.security.InvalidParameterException;

public class PromptoVersion implements Comparable<PromptoVersion> {
	
	public static final PromptoVersion LATEST = parse("-1.-1.-1");

	public static PromptoVersion parse(String version) {
		String[] parts = version.split("\\.");
		if(parts.length<3)
			throw new InvalidParameterException("Version must be like 1.2.3!");
		PromptoVersion v = new PromptoVersion();
		v.major = Integer.parseInt(parts[0]);
		v.minor = Integer.parseInt(parts[1]);
		v.fix = Integer.parseInt(parts[2]);
		return v;
	}

	public static PromptoVersion parse(int version) {
		PromptoVersion v = new PromptoVersion();
		v.major = version >> 24 & 0x000000FF;
		v.minor = version >> 16 & 0x000000FF;
		v.fix = version & 0x0000FFFF;
		return v;
	}
	
	int major;
	int minor;
	int fix;
	
	@Override
	public String toString() {
		return "" + major + '.' + minor + '.' + fix;
	}

	public int asInt() {
		return major << 24 & 0xFF000000
			| minor << 16 & 0x00FF0000
			| fix & 0x0000FFFF;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PromptoVersion)
			return this.asInt() == ((PromptoVersion)obj).asInt();
		else
			return false;
	}

	@Override
	public int compareTo(PromptoVersion o) {
		return Integer.compare(this.asInt(), o.asInt());
	}
}
