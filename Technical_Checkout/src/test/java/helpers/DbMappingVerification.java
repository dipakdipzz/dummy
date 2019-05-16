package helpers;

public class DbMappingVerification {
	String expectedQualifier;
	public String verifyDbMappings(String db) {
		switch (db) {
		case "AgencyUserDB2JCC":
			expectedQualifier="ACS";
			break;
		case "ValidateAgentDB2JCC":
			//expectedQualifier="Haplex of the respective lane";
			expectedQualifier="TZP";
			break;
/*		case "AgencyUserDB2JCC":
			expectedQualifier="ACS";
			break;
		case "AgencyUserDB2JCC":
			String expectedQualifier="ACS";
			break;*/
		default:
			
			break;
		}
		return expectedQualifier;
	}

}
