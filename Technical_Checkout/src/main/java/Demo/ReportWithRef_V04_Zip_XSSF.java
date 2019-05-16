package Demo;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.lingala.zip4j.core.ZipFile;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.commons.io.FileUtils;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTAutoFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilter;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTFilterColumn;


public class ReportWithRef_V04_Zip_XSSF {

	static OutputStream os;
	static XSSFWorkbook excel = new XSSFWorkbook();
	static String rpmMappingLocation = "";
	static String rpmRefMappingLocation = "";
	static String rpmPropertiesLocation = "";
	static String rpmRefPropertiesLocation = "";
	static String OutputfileName = "C:\\DEV\\Validations\\RPMVerify _";

	static String QDEnv = "";
	static String QDEnvRef = "";

	static String DBSubsystem = "";
	static String DBSubsystemRef = "";
	static String DBSubsystemWest = "";
	static String DBSubsystemWestRef = "";
	static String ECSQualifier = "";
	static String ECSQualifierRef = "";
	static String ECSQualifierRU99 = "";
	static String ECSQualifierRU99Ref = "";
	static String ECSQualifierRU99West = "";
	static String ECSQualifierRU99WestRef = "";
	static String RU99QualifierCorp = "";
	static String RU99QualifierCorpRef = "";

	static String IMSHaplex = "";
	static String IMSCorp = "";
	static String IMSWest = "";
	static String IMSHaplexRef = "";
	static String IMSCorpRef = "";
	static String IMSWestRef = "";

	static String CPEnv = "";
	static String CPEnvRef = "";
	static String CPVIP= "";
	static String CPVIPRef = "";
	
	static String ECSProd = "";
	static String ECSQualifierEastRef = "";
	static String DBSubsystemEastRef = "";
	static String IMSEastRef = "";
	static String ECSSubSystemCorp1Ref = "";
	static String ECSSubSystemCorp2Ref = "";

	public static Map<String, String> runAsRoleMappingsRef = new HashMap<String, String>();

	public static void main(String[] args) throws Exception {
		boolean noVerifyRPM = false;
		fileDelete();
		getECSEnv();
		File fileRPM = null;
		File folder = new File("C:\\DEV\\Validations\\RPMZip");
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			if (file.isFile()) {
				fileRPM = file;
				System.out.println(file.getName());
			}
		}
		File fileRPMRef = null;
		File folderRef = new File("C:\\DEV\\Validations\\RPMZipRef");
		File[] listOfFilesRef = folderRef.listFiles();
		// System.out.println(listOfFilesRef.length);

		if (listOfFilesRef.length < 1) {
			System.out.println("No Verify file\nCreating RPMReport file");
			noVerifyRPM = true;

		} else {

			for (File file : listOfFilesRef) {
				if (file.isFile()) {
					fileRPMRef = file;
					System.out.println(file.getName());
				} else if (file.isFile()) {
					System.out.println("No verify File");
				}
			}
		}

		ZipFile zipFile = new ZipFile(fileRPM);
		String destPath = "C:\\DEV\\Validations\\rpm";
		zipFile.extractAll(destPath);
		File rpmFile = new File("C:\\DEV\\Validations\\rpm\\tmp");
		retrieveFolderNames(rpmFile, 1);
		System.out.println(rpmPropertiesLocation + "\n" + rpmMappingLocation);

		if (!noVerifyRPM) {
			ZipFile zipFileRef = new ZipFile(fileRPMRef);

			String destPathRef = "C:\\DEV\\Validations\\rpmRef";
			zipFileRef.extractAll(destPathRef);

			rpmFile = new File("C:\\DEV\\Validations\\rpmRef\\tmp");
			retrieveFolderNames(rpmFile, 2);

			System.out.println(rpmRefPropertiesLocation + "\n"
					+ rpmRefMappingLocation);

			retriveValues();
			getRoles();
			
			excel.write(os);
			os.close();
		} else {
			ReportwithoutRPM report = new ReportwithoutRPM();
			report.reportRPM(rpmMappingLocation, rpmPropertiesLocation,
					OutputfileName);
		}
		
	}

	static void retrieveFolderNames(File rpmFile, int i) {

		File[] listOfDir = rpmFile.listFiles();
		String fileName = "C:\\DEV\\Validations\\rpm\\tmp";
		String fileNameRef = "C:\\DEV\\Validations\\rpmRef\\tmp";
		for (File file : listOfDir) {
			if (file.isDirectory()) {
				fileName = fileName + "\\" + file.getName();
				fileNameRef = fileNameRef + "\\" + file.getName();
			}
		}
		if (i == 1) {
			rpmFile = new File(fileName);
		} else
			rpmFile = new File(fileNameRef);

		listOfDir = rpmFile.listFiles();
		for (File file : listOfDir) {
			if (file.isDirectory()) {
				fileName = fileName + "\\" + file.getName();
				fileNameRef = fileNameRef + "\\" + file.getName();
				OutputfileName = OutputfileName + file.getName() + "_";
			}
		}
		if (i == 1) {
			rpmPropertiesLocation = fileName + "\\installation\\properties";
			fileName = fileName + "\\resourcePropertyFiles\\app";
			rpmFile = new File(fileName);
		} else {
			rpmRefPropertiesLocation = fileNameRef
					+ "\\installation\\properties";
			fileNameRef = fileNameRef + "\\resourcePropertyFiles\\app";
			rpmFile = new File(fileNameRef);
		}
		listOfDir = rpmFile.listFiles();
		for (File file : listOfDir) {
			if (file.isDirectory()) {
				fileName = fileName + "\\" + file.getName();
				fileNameRef = fileNameRef + "\\" + file.getName();
			}
		}
		if (i == 1) {
			rpmMappingLocation = fileName;
		} else
			rpmRefMappingLocation = fileNameRef;
	}

	static void fileDelete() throws IOException {
		File index = new File("C:\\DEV\\Validations\\rpm\\tmp");
		if (index.exists()) {
			FileUtils.forceDelete(index);
		}

		index = new File("C:\\DEV\\Validations\\rpmRef\\tmp");
		if (index.exists()) {
			FileUtils.forceDelete(index);
		}

		index = new File("C:\\DEV\\Validations\\rpmRef");
		if (!index.exists()) {
			index.mkdir();
		}

		index = new File("C:\\DEV\\Validations\\rpm");
		if (!index.exists()) {
			index.mkdir();
		}

	}

	static void getRoles() throws Exception {
		File file = new File(rpmPropertiesLocation);
		String[] files = file.list();
		Map<String, Map<String, String>> roleToUsers = new HashMap<String, Map<String, String>>();
		Map<String, String> runAsRoleMappings = new HashMap<String, String>();
		//Map<String, String> runAsRoleMappingsRef = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {

			if (files[i].contains("MapRolesToUsers")) {
				Map<String, String> roles = new HashMap<String, String>();
				String groups, isEveryOneAuthenticated, allAuth, roleName, users;
				groups = getValue(file + "\\" + files[i], "groups");
				isEveryOneAuthenticated = getValue(file + "\\" + files[i],
						"everyone");
				allAuth = getValue(file + "\\" + files[i], "allauth");
				roleName = getValue(file + "\\" + files[i], "role");
				users = getValue(file + "\\" + files[i], "users");
				roles.put("groups", groups);
				roles.put("auth", isEveryOneAuthenticated);
				roles.put("users", users);
				roleToUsers.put(roleName, roles);
				// groups auth users
			} else if (files[i].contains("MapRunAsRolesToUsers")) {
				// dplaccount username
				String dplAccount = null;
				String username = null;
				dplAccount = getValue(file + "\\" + files[i], "dplaccount");
				username = getValue(file + "\\" + files[i], "username");
				runAsRoleMappings.put(username, dplAccount);
			}

		}
		Map<String, Map<String, String>> roleToUsersRef = getRolesRef();
		
		/*writeValuesTOExcel(runAsRoleMappings, runAsRoleMappingsRef,
				"DPL Accounts", "User", "DPL account");*/
		writeRoleToUsersToExcel(roleToUsers, roleToUsersRef);
		
	}

	public static Map<String, Map<String, String>> getRolesRef()
			throws IOException {
		File file = new File(rpmRefPropertiesLocation);
		String[] files = file.list();
		Map<String, Map<String, String>> roleToUsers = new HashMap<String, Map<String, String>>();

		for (int i = 0; i < files.length; i++) {

			if (files[i].contains("MapRolesToUsers")) {
				Map<String, String> roles = new HashMap<String, String>();
				String groups, isEveryOneAuthenticated, allAuth, roleName, users;
				groups = getValue(file + "\\" + files[i], "groups");
				isEveryOneAuthenticated = getValue(file + "\\" + files[i],
						"everyone");
				allAuth = getValue(file + "\\" + files[i], "allauth");
				roleName = getValue(file + "\\" + files[i], "role");
				users = getValue(file + "\\" + files[i], "users");
				roles.put("groups", groups);
				roles.put("auth", isEveryOneAuthenticated);
				roles.put("users", users);
				roleToUsers.put(roleName, roles);
				// groups auth users
			} else if (files[i].contains("MapRunAsRolesToUsers")) {
				runAsRoleToUsers(files[i], file);
			}

		}
		return roleToUsers;
	}

	static void runAsRoleToUsers(String files, File file) throws IOException {
		// dplaccount username
		String dplAccount = null;
		String username = null;
		dplAccount = getValue(file + "\\" + files, "dplaccount");
		username = getValue(file + "\\" + files, "username");
		runAsRoleMappingsRef.put(username, dplAccount);
	}

	static void writeRoleToUsersToExcel(
			Map<String, Map<String, String>> roleToUsers,
			Map<String, Map<String, String>> roleToUserRef) throws Exception {
		Date date = new Date();
		String dateToName = date.toString();
		dateToName = dateToName.replaceAll(":", "_");

		OutputfileName = OutputfileName + dateToName + ".xlsx";
		os = new FileOutputStream(OutputfileName);
		//os = new FileOutputStream("C:\\DEV\\Validations\\Test.xls");
		XSSFSheet sheet = excel.createSheet("RoleToUserMappings");
		XSSFRow row = sheet.createRow(0);

		
		
		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue("Role");

		XSSFCell cell1 = row.createCell(1);
		//cell1.setCellType(CellType.STRING);
		cell1.setCellValue("groups");

		XSSFCell cell2 = row.createCell(2);
		//cell2.setCellType(CellType.STRING);
		cell2.setCellValue("allAuth");

		XSSFCell cell3 = row.createCell(3);
		//cell3.setCellType(CellType.STRING);
		cell3.setCellValue("users");

		XSSFCell cell4 = row.createCell(5);
		//cell4.setCellType(CellType.STRING);
		cell4.setCellValue("Role_Ref");

		XSSFCell cell5 = row.createCell(6);
		//cell5.setCellType(CellType.STRING);
		cell5.setCellValue("groups_Ref");

		XSSFCell cell6 = row.createCell(7);
		//cell6.setCellType(CellType.STRING);
		cell6.setCellValue("allAuth_Ref");

		XSSFCell cell7 = row.createCell(8);
		//cell7.setCellType(CellType.STRING);
		cell7.setCellValue("users_Ref");

		XSSFCell cell8 = row.createCell(9);
		//cell8.setCellType(CellType.STRING);
		cell8.setCellValue("If Mapping is correct");

		int i = 1;
		for (String role : roleToUsers.keySet()) {
			XSSFRow row1 = sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(role);
			// groups auth users
			Map<String, String> roles = roleToUsers.get(role);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(roles.get("groups"));

			XSSFCell cel2 = row1.createCell(2);
			cel2.setCellValue(roles.get("auth"));

			XSSFCell cel3 = row1.createCell(3);
			cel3.setCellValue(roles.get("users"));
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= roleToUserRef.size(); k++) {
			XSSFRow row1 = sheet.createRow(k);
		}
		int j = 0;
		for (String role : roleToUserRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(5);
			cel0.setCellValue(role);
			// groups auth users
			Map<String, String> roles = roleToUserRef.get(role);

			XSSFCell cel1 = row1.createCell(6);
			cel1.setCellValue(roles.get("groups"));

			XSSFCell cel2 = row1.createCell(7);
			cel2.setCellValue(roles.get("auth"));

			XSSFCell cel3 = row1.createCell(8);
			cel3.setCellValue(roles.get("users"));

			j++;
		}
		int k = 0;
		for (String roles : roleToUserRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			Map<String, String> role = roleToUserRef.get(roles);
			String auth = " " + role.get("auth");
			String users = " " + role.get("users");
			String groups = " " + role.get("groups");
			Map<String, String> roleRef = roleToUsers.get(roles);
			String authRef = " " + roleRef.get("auth");
			String usersRef = " " + roleRef.get("users");
			String groupsRef = " " + roleRef.get("groups");
			// System.out.println(auth + users + authRef + usersRef);
			if ((auth.equals(authRef)) && (users.equals(usersRef))
					&& (groups.equals(groupsRef))) {
				XSSFCell cel3 = row1.createCell(9);
				cel3.setCellValue("True");
			} else {
				XSSFCell cel3 = row1.createCell(9);
				cel3.setCellValue("False");
			}
			k++;
		}
		cellFilterColumn(sheet,k,9);
		//excel.write(os);

	}

	static void retriveValues() throws Exception {
		Map<String, String> activationSpecValues = new HashMap<String, String>();
		Map<String, String> dBValues = new HashMap<String, String>();
		Map<String, Map<String, String>> cPValues = new HashMap<String, Map<String, String>>();
		Map<String, String> iMSValues = new HashMap<String, String>();
		Map<String, String> mQValues = new HashMap<String, String>();
		Map<String, String> j2CValues = new HashMap<String, String>();
		Map<String, String> lDAPValues = new HashMap<String, String>();
		Map<String, String> qcfValues = new HashMap<String, String>();
		Map<String, String> activationSpecValuesRef = new HashMap<String, String>();
		Map<String, String> dBValuesRef = new HashMap<String, String>();
		Map<String, Map<String, String>> cPValuesRef = new HashMap<String, Map<String, String>>();
		Map<String, String> iMSValuesRef = new HashMap<String, String>();
		Map<String, String> mQValuesRef = new HashMap<String, String>();
		Map<String, String> j2CValuesRef = new HashMap<String, String>();
		Map<String, String> lDAPValuesRef = new HashMap<String, String>();
		Map<String, String> qcfValuesRef = new HashMap<String, String>();
		File file = new File(rpmMappingLocation);
		File fileRef = new File(rpmRefMappingLocation);
		String[] files = file.list();
		String[] filesRef = fileRef.list();
		int count = 0;
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			if (fileName.contains("sfDB2jccdatasourceXa.props")) {
				count++;
				String qualifier = getValue(file + "\\" + files[i],
						"${qualifier}");
				String SubSystem = getValue(file + "\\" + files[i],
						"${databaseName}");
				String resourcee = fileName.split("\\.")[2].substring(10,
						fileName.split("\\.")[2].length());
				Map<String, String> qualifiers = new HashMap<String, String>();
				// qualifiers.put(qualifer, SubSystem);
				qualifier = qualifier + "#" + SubSystem;
				dBValues.put(resourcee, qualifier);

			} else if (fileName.contains("sfCPConnectionfactory.props")) {
				String url, prop1, prop2, prop3, prop4 = null, timeout;
				url = getValue(file + "\\" + files[i], "url");
				prop1 = getValue(file + "\\" + files[i], "prop1");
				prop2 = getValue(file + "\\" + files[i], "prop2");
				prop3 = getValue(file + "\\" + files[i], "prop3");
				timeout = prop1 = getValue(file + "\\" + files[i], "timeout");
				Map<String, String> cpProperties = new HashMap<String, String>();
				cpProperties.put("prop1", prop1);
				cpProperties.put("prop2", prop2);
				cpProperties.put("prop3", prop3);
				cpProperties.put("prop4", prop4);
				cpProperties.put("timeout", timeout);
				cpProperties.put("url", url);
				cPValues.put(
						fileName.split("\\.")[2].substring(10,
								fileName.split("\\.")[2].length()),
						cpProperties);
			} else if (fileName.contains("sfIMSConnectionfactory.props")) {
				iMSValues.put(
						fileName.split("\\.")[2].substring(10,
								fileName.split("\\.")[2].length()),
						getValue(file + "\\" + files[i], "${dataStoreName}"));
			} else if (fileName.contains("sfMQQueueDestination.props")) {
				mQValues.put(
						fileName.split("\\.")[2].substring(10,
								fileName.split("\\.")[2].length()),
						getValue(file + "\\" + files[i], "${baseQueueName"));
			} else if (fileName.contains("sfJ2C.props")) {

				j2CValues.put(
						fileName.split("\\.")[2].substring(10,
								fileName.split("\\.")[2].length()),
						getValue(file + "\\" + files[i], "${userID}"));
			} else if (fileName.contains("sfLDAP2Connectionfactory.props")) {
				lDAPValues.put(
						fileName.split("\\.")[2].substring(10,
								fileName.split("\\.")[2].length()),
						getValue(file + "\\" + files[i], "${url}"));

			} else if (fileName.contains("sfActivationSpec.props.ear")) {
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(file + "\\" + files[i],
						"${resourceName}");
				hostName = getValue(file + "\\" + files[i], "${hostName}");
				channel = getValue(file + "\\" + files[i], "${channel}");
				activationSpecValues
						.put(resourceName, hostName + "#" + channel);
			} else if (fileName
					.contains("sfMQQueueConnectionfactory.props.ear")) {
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(file + "\\" + files[i],
						"${resourceName}");
				hostName = getValue(file + "\\" + files[i], "${hostName}");
				channel = getValue(file + "\\" + files[i], "${channel}");
				qcfValues.put(resourceName, hostName + "#" + channel);
			}
		}
		// Reference Environment RPM file mapping 
		for (int i = 0; i < filesRef.length; i++) {
			String fileNameRef = filesRef[i];
			if (fileNameRef.contains("sfDB2jccdatasourceXa.props")) {
				count++;
				String qualifier = getValue(fileRef + "\\" + filesRef[i],
						"${qualifier}");
				String SubSystem = getValue(fileRef + "\\" + filesRef[i],
						"${databaseName}");
				String resourcee = fileNameRef.split("\\.")[2].substring(10,
						fileNameRef.split("\\.")[2].length());
				Map<String, String> qualifiers = new HashMap<String, String>();
				// qualifiers.put(qualifer, SubSystem);
				qualifier = qualifier + "#" + SubSystem;
				dBValuesRef.put(resourcee, qualifier);

			} else if (fileNameRef.contains("sfCPConnectionfactory.props")) {
				String url, prop1, prop2, prop3, prop4 = null, timeout;
				url = getValue(fileRef + "\\" + filesRef[i], "url");
				prop1 = getValue(fileRef + "\\" + filesRef[i], "prop1");
				prop2 = getValue(fileRef + "\\" + filesRef[i], "prop2");
				prop3 = getValue(fileRef + "\\" + filesRef[i], "prop3");
				timeout = prop1 = getValue(fileRef + "\\" + filesRef[i],
						"timeout");
				Map<String, String> cpProperties = new HashMap<String, String>();
				cpProperties.put("prop1", prop1);
				cpProperties.put("prop2", prop2);
				cpProperties.put("prop3", prop3);
				cpProperties.put("prop4", prop4);
				cpProperties.put("timeout", timeout);
				cpProperties.put("url", url);
				cPValuesRef.put(
						fileNameRef.split("\\.")[2].substring(10,
								fileNameRef.split("\\.")[2].length()),
						cpProperties);
			} else if (fileNameRef.contains("sfIMSConnectionfactory.props")) {
				iMSValuesRef.put(
						fileNameRef.split("\\.")[2].substring(10,
								fileNameRef.split("\\.")[2].length()),
						getValue(fileRef + "\\" + filesRef[i],
								"${dataStoreName}"));
			} else if (fileNameRef.contains("sfMQQueueDestination.props")) {
				mQValuesRef.put(
						fileNameRef.split("\\.")[2].substring(10,
								fileNameRef.split("\\.")[2].length()),
						getValue(fileRef + "\\" + filesRef[i],
								"${baseQueueName"));
			} else if (fileNameRef.contains("sfJ2C.props")) {

				j2CValuesRef.put(
						fileNameRef.split("\\.")[2].substring(10,
								fileNameRef.split("\\.")[2].length()),
						getValue(fileRef + "\\" + filesRef[i], "${userID}"));
			} else if (fileNameRef.contains("sfLDAP2Connectionfactory.props")) {
				lDAPValuesRef.put(
						fileNameRef.split("\\.")[2].substring(10,
								fileNameRef.split("\\.")[2].length()),
						getValue(fileRef + "\\" + filesRef[i], "${url}"));

			} else if (fileNameRef.contains("sfActivationSpec.props.ear")) {
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(fileRef + "\\" + filesRef[i],
						"${resourceName}");
				hostName = getValue(fileRef + "\\" + filesRef[i], "${hostName}");
				channel = getValue(fileRef + "\\" + filesRef[i], "${channel}");
				activationSpecValuesRef.put(resourceName, hostName + "#"
						+ channel);
			} else if (fileNameRef
					.contains("sfMQQueueConnectionfactory.props.ear")) {
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(fileRef + "\\" + filesRef[i],
						"${resourceName}");
				hostName = getValue(fileRef + "\\" + filesRef[i], "${hostName}");
				channel = getValue(fileRef + "\\" + filesRef[i], "${channel}");
				qcfValuesRef.put(resourceName, hostName + "#" + channel);
			}
		}
		

		writeValuesTOExcelNoRef_IMSMapping(iMSValues, iMSValuesRef,
				"IMS Values", "Resource", "Qualifier");

		writeValuesTOExcelNoRef_MQMapping(mQValues, mQValuesRef, "MQ Values",
				"Resource", "QD");
		writeValuesTOExcel(j2CValues, j2CValuesRef, "J2C Values", "Resource",
				"J2C Id");
		writeValuesTOExcel(lDAPValues, lDAPValuesRef, "LDAP Values",
				"Resource", "LDAP Server");
		writeDBValuesToExcel(dBValues, dBValuesRef);
		writeCPValuesToExcel(cPValues, cPValuesRef);
		writeActivationSpecValuesToExcel("Activation Spec",
				activationSpecValues, activationSpecValuesRef);
		writeActivationSpecValuesToExcel("QCF", qcfValues, qcfValuesRef);
	}



	static void writeDBValuesToExcel(Map<String, String> DBValues,
			Map<String, String> DBValuesRef) throws Exception {
		os = new FileOutputStream(new File("C:\\DEV\\test.xlsx"));
		XSSFSheet sheet = excel.createSheet("DB2Mappings");
		XSSFFont headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		XSSFCellStyle cellStyle = excel.createCellStyle();
		XSSFRow row = (XSSFRow) sheet.createRow(0);
		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue("DB Resource");
		XSSFCell cell1 = row.createCell(1);
		cell1.setCellValue("Qualifier");
		XSSFCell cell2 = row.createCell(2);
		cell2.setCellValue("System");

		XSSFCell cell3 = row.createCell(4);
		cell3.setCellValue("DB_Resource_Reference");
		XSSFCell cell4 = row.createCell(5);
		cell4.setCellValue("Qualifier_Reference");
		XSSFCell cell5 = row.createCell(6);
		cell5.setCellValue("System_Reference");

		XSSFCell cell20 = row.createCell(7);
		cell20.setCellValue("If Mapping is correct");

		int i = 1;
		for (Map.Entry<String, String> resource : DBValues.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource.getKey());
			String Value = resource.getValue();
			String[] qualifier = Value.split("#");
			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(qualifier[0]);
			XSSFCell cel2 = row1.createCell(2);
			cel2.setCellValue(qualifier[1]);

			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= DBValuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (Map.Entry<String, String> resource : DBValuesRef.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(4);
			cel0.setCellValue(resource.getKey());
			// groups auth users
			String Value = resource.getValue();
			String[] qualifier = Value.split("#");

			XSSFCell cel1 = row1.createCell(5);
			cel1.setCellValue(qualifier[0]);

			XSSFCell cel2 = row1.createCell(6);
			cel2.setCellValue(qualifier[1]);

			j++;
		}

		int k = 0;
		for (Map.Entry<String, String> roles : DBValues.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			String Value = roles.getValue();
			String[] role = Value.split("#");
			String qualifier = " " + role[0];
			String system = " " + role[1];

			String ValueRef = DBValuesRef.get(roles.getKey());
			String[] roleRef = ValueRef.split("#");
			String qualifierRef = " " + roleRef[0];
			String systemRef = " " + roleRef[1];

			if (!systemRef.equals(system)) {
				systemRef = systemRef.replaceAll(DBSubsystemRef, DBSubsystem);
				systemRef = systemRef.replaceAll(DBSubsystemWestRef,
						DBSubsystemWest);
				if (ECSProd.equalsIgnoreCase("Yes")){
					systemRef = systemRef.replaceAll(DBSubsystemEastRef,
							DBSubsystem);

				}
			}

			if (!qualifierRef.equals(qualifier)) {
				qualifierRef = qualifierRef.replaceAll(ECSQualifierRef,
						ECSQualifier);
				if ((roles.getKey().contains("HPLX_5"))
						&& (qualifier.contains(ECSQualifierRU99West))) {
					qualifierRef = qualifierRef.replaceAll(
							ECSQualifierRU99WestRef, ECSQualifierRU99West);
				} else if (roles.getKey().contains("RU99")) {
					if (roles.getKey().contains("CORP")) {
						qualifierRef = qualifierRef.replaceAll("RU99", RU99QualifierCorp);
					} else {
						qualifierRef = qualifierRef.replaceAll(
								ECSQualifierRU99Ref, ECSQualifierRU99);
					}
				}
				if (ECSProd.equalsIgnoreCase("Yes")) {

					if ((roles.getKey().contains("HPLX_3"))) {
						if ((qualifierRef.contains("E1"))
								&& (roles.getKey().contains("ECSE1"))) {
							qualifierRef = qualifierRef.replaceAll("E1", "C1");
						}
						qualifierRef = qualifierRef.replaceAll(
								ECSQualifierEastRef, ECSQualifier);
						systemRef = systemRef.replaceAll(ECSQualifierEastRef,
								ECSQualifier);
					}

					else {
						systemRef = systemRef.replaceAll(ECSSubSystemCorp1Ref,
								DBSubsystem);
						systemRef = systemRef.replaceAll(ECSSubSystemCorp2Ref,
								DBSubsystem);
					}
				}
			}

			if ((qualifier.equals(qualifierRef)) && (system.equals(systemRef))) {
				XSSFCell cel3 = row1.createCell(7);
				cel3.setCellValue("True");
			} else if ((roles.getKey().contains("ISEIT"))) {
				if (!qualifier.contains("RU99")) {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("ISEIT - Qualifier should be RU99");
				} else {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("True");
				}

			} else if ((roles.getKey().contains("LCL_RU99"))
					&& (ECSProd.equalsIgnoreCase("Yes"))) {
				if ((qualifier.equalsIgnoreCase(qualifierRef))) {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("True");
				}

			} else if ((roles.getKey().contains("LCL_ECS"))
					&& (ECSProd.equalsIgnoreCase("Yes"))) {
				if ((qualifier.equalsIgnoreCase(qualifierRef))) {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("True");
				}
			} else if ((roles.getKey().contains("RO_03"))) {

				XSSFCell cel3 = row1.createCell(7);
				cel3.setCellValue("Region");
			} else if (ECSProd.equalsIgnoreCase("Yes")) {
				qualifierRef = qualifierRef.trim();
				if (qualifierRef.equalsIgnoreCase("CLM")) {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("True");
				} else if ((systemRef.contains("DDFHAPXS"))
						&& (roles.getKey().contains("STWD_DVPLX"))) {
					if (qualifier.contains(ECSQualifier)) {
						XSSFCell cel3 = row1.createCell(7);
						cel3.setCellValue("True");
					}
				} else {
					XSSFCell cel3 = row1.createCell(7);
					cel3.setCellValue("False");
					System.out.println(qualifier + system);
					System.out.println(qualifierRef + systemRef);
					System.out.println("***************");
				}
			}

			else {
				XSSFCell cel3 = row1.createCell(7);
				cel3.setCellValue("False");
				/*System.out.println(qualifier + system);
				System.out.println(qualifierRef + systemRef);
				System.out.println("***************");*/
			}
			k++;
		}
		cellFilterColumn(sheet,k,7);
		//excel.write(os);

	}

	static void writeCPValuesToExcel(Map<String, Map<String, String>> CPValues,
			Map<String, Map<String, String>> CPValuesRef) throws Exception {
		os = new FileOutputStream(new File("C:\\DEV\\test.xlsx"));

		XSSFSheet sheet = excel.createSheet("CP Values");
		XSSFFont headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		XSSFRow row = (XSSFRow) sheet.createRow(0);

		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue("Resource");

		XSSFCell cell1 = row.createCell(1);
		cell1.setCellValue("Prop1");

		XSSFCell cell2 = row.createCell(2);
		cell2.setCellValue("Prop2");

		XSSFCell cell3 = row.createCell(3);
		cell3.setCellValue("Prop3");

		XSSFCell cell4 = row.createCell(4);
		cell4.setCellValue("Prop4");

		XSSFCell cell5 = row.createCell(5);
		cell5.setCellValue("timeout");

		XSSFCell cell6 = row.createCell(6);
		cell6.setCellValue("URL");

		XSSFCell cell8 = row.createCell(8);
		cell8.setCellValue("Resource_Ref");

		XSSFCell cell9 = row.createCell(9);
		cell9.setCellValue("Prop1_Ref");

		XSSFCell cell10 = row.createCell(10);
		cell10.setCellValue("Prop2_ref");

		XSSFCell cell11 = row.createCell(11);
		cell11.setCellValue("Prop3_ref");

		XSSFCell cell12 = row.createCell(12);
		cell12.setCellValue("Prop4_ref");

		XSSFCell cell13 = row.createCell(13);
		cell13.setCellValue("timeout_ref");

		XSSFCell cell14 = row.createCell(14);
		cell14.setCellValue("URL_ref");

		XSSFCell cell20 = row.createCell(15);
		cell20.setCellValue("If Mapping is correct");

		int i = 1;
		for (String resource : CPValues.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource);

			Map<String, String> cpValues = CPValues.get(resource);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(cpValues.get("prop1"));

			XSSFCell cel2 = row1.createCell(2);
			cel2.setCellValue(cpValues.get("prop2"));

			XSSFCell cel3 = row1.createCell(3);
			cel3.setCellValue(cpValues.get("prop3"));

			XSSFCell cel4 = row1.createCell(4);
			cel4.setCellValue(cpValues.get("prop4"));

			XSSFCell cel5 = row1.createCell(5);
			cel5.setCellValue(cpValues.get("timeout"));

			XSSFCell cel6 = row1.createCell(6);
			cel6.setCellValue(cpValues.get("url"));
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= CPValuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (String resource : CPValuesRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(8);
			cel0.setCellValue(resource);
			// groups auth users
			Map<String, String> qualifier = CPValuesRef.get(resource);

			XSSFCell cel1 = row1.createCell(9);
			cel1.setCellValue(qualifier.get("prop1"));

			XSSFCell cel2 = row1.createCell(10);
			cel2.setCellValue(qualifier.get("prop2"));

			XSSFCell cel3 = row1.createCell(11);
			cel3.setCellValue(qualifier.get("prop3"));

			XSSFCell cel4 = row1.createCell(12);
			cel4.setCellValue(qualifier.get("prop4"));

			XSSFCell cel5 = row1.createCell(13);
			cel5.setCellValue(qualifier.get("timeout"));

			XSSFCell cel6 = row1.createCell(14);
			cel6.setCellValue(qualifier.get("url"));
			j++;
		}

		int k = 0;
		for (String roles : CPValues.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			Map<String, String> role = CPValues.get(roles);
			String prop1 = " " + role.get("prop1");
			String prop2 = " " + role.get("prop2");
			String prop3 = " " + role.get("prop3");
			String prop4 = " " + role.get("prop4");
			String timeout = " " + role.get("timeout");
			String url = " " + role.get("url");
			String[] env = CPEnv.split("#");
			Map<String, String> roleRef = CPValuesRef.get(roles);
			String prop1Ref = " " + roleRef.get("prop1");
			String prop2Ref = " " + roleRef.get("prop2");
			String prop3Ref = " " + roleRef.get("prop3");
			String prop4Ref = " " + roleRef.get("prop4");
			String timeoutRef = " " + roleRef.get("timeout");
			String urlRef = " " + roleRef.get("url");
			
			String[] envRef = {"",""};
			
			if (ECSProd.equalsIgnoreCase("NO")) {
				envRef = CPEnvRef.split("#");
				if ((url.contains(env[0]))
						|| (url.contains(env[0].toLowerCase()))) {
					urlRef = urlRef.replaceAll(envRef[0], env[0]);
					urlRef = urlRef.replaceAll(envRef[0].toLowerCase(),
							env[0].toLowerCase());
					urlRef = urlRef.replaceAll(envRef[1], env[0]);
					urlRef = urlRef.replaceAll(envRef[1].toLowerCase(),
							env[0].toLowerCase());
				} else if ((url.contains(env[1]))
						|| (url.contains(env[1].toLowerCase()))) {
					urlRef = urlRef.replaceAll(envRef[0], env[1]);
					urlRef = urlRef.replaceAll(envRef[0].toLowerCase(),
							env[1].toLowerCase());
					urlRef = urlRef.replaceAll(envRef[1], env[1]);
					urlRef = urlRef.replaceAll(envRef[1].toLowerCase(),
							env[1].toLowerCase());
					urlRef = urlRef.replaceAll(envRef[1], env[1]);
					urlRef = urlRef.replaceAll(envRef[1].toLowerCase(),
							env[1].toLowerCase());
				}
				
			} else if (ECSProd.equalsIgnoreCase("Yes")) {
				url = url.replaceAll("/"+env[0], " ");
				url = url.replaceAll("/"+env[1], " ");
				url = url.replaceAll("/"+env[0].toLowerCase(), " ");
				url = url.replaceAll("/"+env[1].toLowerCase(), " ");
				
			}

			urlRef = urlRef.replaceAll(CPVIPRef, CPVIP);
			url = url.trim();
			urlRef = urlRef.trim();
			
			if ((prop1.equals(prop1Ref)) && (prop2.equals(prop2Ref))
					&& (prop3.equals(prop3Ref)) && (prop4.equals(prop4Ref))
					&& (timeout.equals(timeoutRef)) && (url.equalsIgnoreCase(urlRef))) {
				XSSFCell cel3 = row1.createCell(15);
				cel3.setCellValue("True");
			} else {
				XSSFCell cel3 = row1.createCell(15);
				
				cel3.setCellValue("False");
			}
			k++;
		}
		cellFilterColumn(sheet,k,15);
		//excel.write(os);
	}

	static void writeValuesTOExcel(Map<String, String> values,
			Map<String, String> valuesRef, String SheetName, String header1,
			String header2) throws Exception {
		os = new FileOutputStream(new File("C:\\DEV\\test.xlsx"));

		XSSFSheet sheet = excel.createSheet(SheetName);
		XSSFFont headerFont = excel.createFont();
		XSSFRow row = (XSSFRow) sheet.createRow(0);

		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue(header1);

		XSSFCell cell1 = row.createCell(1);
		cell1.setCellValue(header2);

		XSSFCell cell3 = row.createCell(3);
		cell3.setCellValue(header1 + "_Ref");

		XSSFCell cell4 = row.createCell(4);
		cell4.setCellValue(header2 + "_Ref");

		XSSFCell cell20 = row.createCell(5);
		cell20.setCellValue("If Mapping is correct");
		int i = 1;
		for (String resource : values.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(values.get(resource));
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= valuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (String resource : valuesRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(3);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(4);
			cel1.setCellValue(valuesRef.get(resource));

			j++;
		}

		int k = 0;
		for (String roles : values.keySet()) {			
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			String resource = " " + values.get(roles).trim();
			String resourceRef = " " + valuesRef.get(roles).trim();
			//System.out.println(resource + resourceRef);
			if (resource.equals(resourceRef)) {
				XSSFCell cel3 = row1.createCell(5);
				cel3.setCellValue("True");
			} else {
				XSSFCell cel3 = row1.createCell(5);
				cel3.setCellValue("False");
			}
			k++;
		}
		cellFilterColumn(sheet,k,5);
		//excel.write(os);
	}

	static void writeValuesTOExcelNoRef_IMSMapping(Map<String, String> values,
			Map<String, String> valuesRef, String SheetName, String header1,
			String header2) throws Exception {
		os = new FileOutputStream(new File("C:\\DEV\\test.xlsx"));

		XSSFSheet sheet = excel.createSheet(SheetName);
		XSSFFont headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		XSSFCellStyle cellStyle = excel.createCellStyle();
		XSSFRow row = (XSSFRow) sheet.createRow(0);

		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue(header1);

		XSSFCell cell1 = row.createCell(1);
		cell1.setCellValue(header2);

		XSSFCell cell3 = row.createCell(3);
		cell3.setCellValue(header1 + "_Ref");

		XSSFCell cell4 = row.createCell(4);
		cell4.setCellValue(header2 + "_Ref");

		XSSFCell cell20 = row.createCell(5);
		cell20.setCellValue("If Mapping is correct");

		Map<String, String> differenceMap = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : values.entrySet()) {
			if (!valuesRef.containsKey(entry.getKey())) {
				differenceMap.put(entry.getKey(), entry.getValue());
			}
		}

		Map<String, String> differenceMapRef = new HashMap<String, String>();

		for (Map.Entry<String, String> entry : valuesRef.entrySet()) {
			if (!values.containsKey(entry.getKey())) {
				differenceMapRef.put(entry.getKey(), entry.getValue());
			}
		}
		for (Map.Entry<String, String> entry : differenceMap.entrySet()) {

			values.remove(entry.getKey());

		}
		for (Map.Entry<String, String> entry : differenceMapRef.entrySet()) {

			valuesRef.remove(entry.getKey());

		}
		int i = 1;
		for (String resource : values.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(values.get(resource));
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= valuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (String resource : valuesRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(3);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(4);
			cel1.setCellValue(valuesRef.get(resource));

			j++;
		}

		int k = 0;
		for (Map.Entry<String, String> roles : values.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			String resource = " " + roles.getValue();
			String resourceRef = " " + valuesRef.get(roles.getKey());

			resourceRef = resourceRef.replaceAll(IMSHaplexRef, IMSHaplex);
			//System.out.println(resource+ resourceRef +roles.getKey()+roles.getKey().contains("HPLX")+roles.getKey().contains("CORP"));
			
			if (roles.getKey().equals("IMS_HPLX_5")) {
				resourceRef = resourceRef.replaceAll(IMSWestRef, IMSWest);
			} else if ((roles.getKey().equals("IMS_HPLX_4"))
					&& (ECSProd.equalsIgnoreCase("Yes"))) {
				resourceRef = resourceRef.replaceAll(IMSEastRef, IMSHaplex);
			} else
				resourceRef = resourceRef.replaceAll(IMSCorpRef, IMSCorp);
			if (resource.equals(resourceRef)) {
				XSSFCell cel3 = row1.createCell(5);
				cel3.setCellValue("True");
			} else if ((roles.getKey().contains("RO"))) {
				XSSFCell cel3 = row1.createCell(5);
				cel3.setCellValue("Region");
			}  else {
				XSSFCell cel3 = row1.createCell(5);
				cel3.setCellValue("False");
			}
			k++;
		}

		int l = i;
		for (String resource : differenceMap.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(l);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(differenceMap.get(resource));
			l++;
		}

		for (int n = j; n <= (differenceMapRef.size() + valuesRef.size()); n++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(n);
		}
		int m = j;
		for (String resource : differenceMapRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + m);
			XSSFCell cel0 = row1.createCell(3);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(4);
			cel1.setCellValue(differenceMapRef.get(resource));

			m++;
		}
		cellFilterColumn(sheet,k,5);
		//excel.write(os);
	}

	static void writeValuesTOExcelNoRef_MQMapping(Map<String, String> values,
			Map<String, String> valuesRef, String SheetName, String header1,
			String header2) throws Exception {
		os = new FileOutputStream(new File("C:\\DEV\\test.xlsx"));

		XSSFSheet sheet = excel.createSheet(SheetName);
		XSSFFont headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		XSSFCellStyle cellStyle = excel.createCellStyle();
		XSSFRow row = (XSSFRow) sheet.createRow(0);

		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue(header1);

		XSSFCell cell1 = row.createCell(1);
		cell1.setCellValue(header2);

		XSSFCell cell3 = row.createCell(3);
		cell3.setCellValue(header1 + "_Ref");

		XSSFCell cell4 = row.createCell(4);
		cell4.setCellValue(header2 + "_Ref");

		XSSFCell cell20 = row.createCell(5);
		cell20.setCellValue("If Mapping is correct");

		int i = 1;
		for (String resource : values.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(values.get(resource));
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= valuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (String resource : valuesRef.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(3);
			XSSFCellStyle style = excel.createCellStyle();
			//style.setWrapText(true);
			cel0.setCellValue(resource);

			XSSFCell cel1 = row1.createCell(4);
			XSSFCellStyle style1 = excel.createCellStyle();
//			style1.setWrapText(true);
			cel1.setCellValue(valuesRef.get(resource));

			j++;
		}
		// excel.write(os);
		int k = 0;

		for (String roles : values.keySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			String QD = " " + values.get(roles);
			String QDRef = " " + valuesRef.get(roles);

			if (ECSProd.equalsIgnoreCase("NO")){
				QD = QD.replaceAll(QDEnv, QDEnvRef);
			}
			else if (ECSProd.equalsIgnoreCase("YES")){
				QD = QD.replaceAll(QDEnv+".","" );
			}
			
			XSSFCell cel3 = row1.createCell(5);
			if (QD.equals(QDRef)) {
				cel3.setCellValue("True");
			} else {
				cel3.setCellValue("False");
			}
			
			
			k++;
		}
		cellFilterColumn(sheet,k,5);
		//excel.write(os);
	}

	

	static void writeActivationSpecValuesToExcel(String fileName,
			Map<String, String> activationSpecValues,
			Map<String, String> activationSpecValuesRef) throws Exception {
		XSSFSheet sheet = excel.createSheet(fileName);
		XSSFFont headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		XSSFCellStyle cellStyle = excel.createCellStyle();
		XSSFRow row = (XSSFRow) sheet.createRow(0);
		XSSFCell cell0 = row.createCell(0);
		cell0.setCellValue("Resource Name");
		XSSFCell cell2 = row.createCell(1);
		cell2.setCellValue("Host Name");
		XSSFCell cell3 = row.createCell(2);
		cell3.setCellValue("Channel");

		XSSFCell cell4 = row.createCell(4);
		cell4.setCellValue("Resource_Name_Ref");
		XSSFCell cell5 = row.createCell(5);
		cell5.setCellValue("Host_Name_Ref");
		XSSFCell cell6 = row.createCell(6);
		cell6.setCellValue("Channel_Ref");

		XSSFCell cell20 = row.createCell(7);
		cell20.setCellValue("If Mapping is correct");

		int i = 1;
		for (Map.Entry<String, String> resource : activationSpecValues
				.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(i);
			XSSFCell cel0 = row1.createCell(0);
			cel0.setCellValue(resource.getKey());
			String Value = resource.getValue();
			String[] val = Value.split("#");
			XSSFCell cel1 = row1.createCell(1);
			cel1.setCellValue(val[0]);
			XSSFCell cel2 = row1.createCell(2);
			cel2.setCellValue(val[1]);
			i++;
		}
		// to create rows for Ref role to user map.
		for (int k = i; k <= activationSpecValuesRef.size(); k++) {
			XSSFRow row1 = (XSSFRow) sheet.createRow(k);
		}
		int j = 0;
		for (Map.Entry<String, String> resource : activationSpecValuesRef
				.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + j);
			XSSFCell cel0 = row1.createCell(4);
			cel0.setCellValue(resource.getKey());
			String Value = resource.getValue();
			String[] val = Value.split("#");
			XSSFCell cel1 = row1.createCell(5);
			cel1.setCellValue(val[0]);
			XSSFCell cel2 = row1.createCell(6);
			cel2.setCellValue(val[1]);

			j++;
		}
		int k = 0;
		for (Map.Entry<String, String> roles : activationSpecValues.entrySet()) {
			XSSFRow row1 = (XSSFRow) sheet.getRow(1 + k);
			String Val = roles.getValue();
			String[] value = Val.split("#");

			String ValRef = activationSpecValues.get(roles.getKey());
			String[] valueRef = ValRef.split("#");

			if ((value[0].equals(valueRef[0]))
					&& (value[1].equals(valueRef[1]))) {
				XSSFCell cel3 = row1.createCell(7);
				cel3.setCellValue("True");
			} else {
				XSSFCell cel3 = row1.createCell(7);
				cel3.setCellValue("False");
			}
			k++;
		}
		cellFilterColumn(sheet,k,7);
		//excel.write(os);

		
	}
	
	static String getValue(String fileNamee, String seperator)
			throws IOException {
		FileInputStream fstream = new FileInputStream(fileNamee);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		String subSystem = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith(seperator)) {
				if (line.split("=").length > 1)
					subSystem = line.split("=")[1];
				else
					subSystem = null;
				return subSystem;
			}
		}
		return subSystem;
	}
	
	
	static void getECSEnv() throws IOException {
		
		QDEnv = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSEnv");
		QDEnvRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSEnvRef");
		DBSubsystem = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSSubSystem");
		DBSubsystemRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSSubSystemRef");
		DBSubsystemWest = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSSubSystemWest");
		DBSubsystemWestRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSSubSystemWestRef");
		ECSQualifier = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSQualifier");
		ECSQualifierRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"ECSQualifierRef");
		ECSQualifierRU99 = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"RU99Qualifier");
		ECSQualifierRU99Ref = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"RU99QualifierRef");
		ECSQualifierRU99West = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"RU99QualifierWest");
		RU99QualifierCorp = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"RU99QualifierCorp");
		RU99QualifierCorpRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "RU99QualifierCorpRef");
		ECSQualifierRU99WestRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"RU99QualifierWestRef");
		IMSHaplex = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSHaplex");
		IMSCorp = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSCorp");
		IMSWest = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSWest");
		IMSHaplexRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt",
				"IMSHaplexRef");
		IMSCorpRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSCorpRef");
		IMSWestRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSWestRef");

		CPEnv = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "CPEnv");
		CPEnvRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "CPEnvRef");
		CPVIP = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "CPVIP");
		CPVIPRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "CPVIPRef");
		
		ECSProd = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSProd");
		DBSubsystemEastRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSSubSystemEastRef");
		ECSQualifierEastRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSQualifierEastRef");
		ECSSubSystemCorp1Ref = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSSubSystemCorp1Ref");
		ECSSubSystemCorp2Ref = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "ECSSubSystemCorp2Ref");
		IMSEastRef = getValue("C:\\DEV\\Validations\\ECSEnv.txt", "IMSEastRef");
	}

	static void cellFilterColumn(XSSFSheet sheet, int k, int l) {

		CellRangeAddress range = new CellRangeAddress(0, k, 0, l);

		sheet.setAutoFilter(range);
		CTAutoFilter sheetFilter = sheet.getCTWorksheet().getAutoFilter();
		CTFilterColumn filterColumn = sheetFilter.insertNewFilterColumn(0);
		filterColumn.setColId(l);
		CTFilter filter = filterColumn.addNewFilters().insertNewFilter(0);
		filter.setVal("True");

		// We have to apply the filter ourselves by hiding the rows: 
		for (Row row : sheet) {
			for (Cell c : row) {
				if ((c.getStringCellValue().equals("True"))||c.getStringCellValue().equals("Region")) {
					XSSFRow r1 = (XSSFRow) c.getRow();
					if (r1.getRowNum() != 0) {
						r1.getCTRow().setHidden(true);
					}
				}
			}
		}

	}
	
}
