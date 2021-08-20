package kulon.publicapps.environmenttest.components;

public class Constants {
	public static final String ELASTIC_INDEX_TYPE = "_doc";
	
	public static final String CUSTOM_HEADER_PREFIX = "kulon";
	// transient headers are not persisted to log
	public static final String TRANSIENT_HEADER_PREFIX = "_kulon";
	public static final String TEST_REPORT = TRANSIENT_HEADER_PREFIX + "TestReport";

	public static final String NOT_TESTED = "Not Tested";
	
	private Constants() {
	}
}
