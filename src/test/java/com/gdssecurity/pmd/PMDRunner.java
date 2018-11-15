package com.gdssecurity.pmd;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.cli.PMDCommandLineInterface;
import net.sourceforge.pmd.cli.PMDParameters;

public final class PMDRunner {

	public static final String RULESET_DEFAULT = "rulesets/GDS/SecureCoding.xml";
	public static final String RULESET_SQL_INJECTION = "rulesets/GDS/CWE/cwe-0089-sql-injection.xml";
	public static final String RULESET_XSS = "rulesets/GDS/CWE/cwe-0079-cross-site-scripting.xml";
	public static final String RULESET_ACCESS="rulesets/GDS/CWE/cwe-0285-improper-authorization.xml";
	public static final String RULEST_PATH_TRAVERSAL = "rulesets/GDS/CWE/cwe-0022-path-traversal.xml";
	public static final String RULESET_WEAK_CRYPTO = "rulesets/GDS/CWE/cwe-0327-weak-cryptographic-algorithms.xml";
	public static final String RULESET_UNVALIDATED_REDIRECTS = "rulesets/GDS/CWE/cwe-0601-open-redirect.xml";
	public static final String RULESET_HTTP_RESPONSE_SPLITTING = "rulesets/GDS/CWE/cwe-0113-http-response-splitting.xml";

	private PMDRunner() {
		throw new AssertionError("No instances allowed");
	}

	public static int run(String directory, String ruleset) throws Exception {
		int violations = run(new String[] { "-d", directory, "-R", ruleset, "-f", "text", "-language", "java" , "-no-cache"});
		return violations;

	}

	public static int run(String directory) throws Exception {
		return run(directory, RULESET_DEFAULT);
	}

	public static int run(String[] args) throws Exception {
		final PMDParameters params = PMDCommandLineInterface.extractParameters(new PMDParameters(), args, "pmd");
		final PMDConfiguration configuration = params.toConfiguration();

		try {
			int violations = PMD.doPMD(configuration);
			return violations;
		} catch (Exception e) {
			throw e;
		}
	}
}
