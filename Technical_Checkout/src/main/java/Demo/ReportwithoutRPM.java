package rpm.best.propertytype;

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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class ReportwithoutRPM {

	static OutputStream os;
	static Workbook excel = new HSSFWorkbook();
	static String OutputFileName = "";
	public static void reportRPM(String rpmMappingLocation,String rpmPropertiesLocation, String OutputfileName) throws Exception {

		OutputFileName = OutputfileName;
		OutputFileName = OutputFileName.replaceAll("Verify", "Report");
		retriveValues(rpmMappingLocation);
		getRoles(rpmPropertiesLocation);
		

	}
	static void getRoles(String rpmPropertiesLocation) throws Exception{
		File file = new File(rpmPropertiesLocation);
		String[] files = file.list();
		Map<String,Map<String, String>> roleToUsers = new HashMap<String, Map<String,String>>();
		Map<String, String> runAsRoleMappings = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {

			if(files[i].contains("MapRolesToUsers")){
				Map<String, String> roles = new HashMap<String, String>();
				String groups,isEveryOneAuthenticated,allAuth,roleName,users;
				groups = getValue(file+"\\"+files[i], "groups");
				isEveryOneAuthenticated = getValue(file+"\\"+files[i], "everyone");
				allAuth = getValue(file+"\\"+files[i], "allauth");
				roleName = getValue(file+"\\"+files[i], "role");
				users = getValue(file+"\\"+files[i], "users");
				roles.put("groups", groups);
				roles.put("auth", isEveryOneAuthenticated);
				roles.put("users", users);
				roleToUsers.put(roleName, roles);
				//groups auth users
			}
			else if (files[i].contains("MapRunAsRolesToUsers")) {
				//dplaccount username
				String dplAccount = null;
				String username = null;
				dplAccount = getValue(file+"\\"+files[i], "dplaccount");
				username = getValue(file+"\\"+files[i], "username");
				runAsRoleMappings.put(username,dplAccount);
			}

		}
		if (roleToUsers.size() > 0) {
			writeRoleToUsersToExcel(roleToUsers);
		}
		if (runAsRoleMappings.size() > 0) {
			writeValuesTOExcel(runAsRoleMappings, "DPL Accounts", "User",
					"DPL account");
		}
	}
	static void writeRoleToUsersToExcel(Map<String,Map<String, String>> roleToUsers) throws Exception{
		Date date = new Date();
		String dateToName = date.toString();
		dateToName = dateToName.replaceAll(":","_");
		OutputFileName = OutputFileName + "_" +dateToName;
		os = new FileOutputStream(new File(OutputFileName + ".xls"));
		Sheet sheet = excel.createSheet("RoleToUserMappings");
		Font headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle cellStyle = excel.createCellStyle();
		cellStyle.setFont(headerFont);
		Row row = sheet.createRow(0);

		Cell cell0 = row.createCell(0);
		cell0.setCellType(CellType.STRING);
		cell0.setCellValue("Role");


		Cell cell1 = row.createCell(1);
		cell1.setCellType(CellType.STRING);
		cell1.setCellValue("groups");

		Cell cell2 = row.createCell(2);
		cell2.setCellType(CellType.STRING);
		cell2.setCellValue("allAuth");

		Cell cell3 = row.createCell(3);
		cell3.setCellType(CellType.STRING);
		cell3.setCellValue("users");


		int i=1;
		for(String role: roleToUsers.keySet()){
			Row row1 = sheet.createRow(i);
			Cell cel0 = row1.createCell(0);
			cel0.setCellType(CellType.STRING);
			cel0.setCellValue(role);
			//groups auth users
			Map<String, String> roles = roleToUsers.get(role);

			Cell cel1 = row1.createCell(1);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(roles.get("groups"));

			Cell cel2 = row1.createCell(2);
			cel2.setCellType(CellType.STRING);
			cel2.setCellValue(roles.get("auth"));

			Cell cel3 = row1.createCell(3);
			cel3.setCellType(CellType.STRING);
			cel3.setCellValue(roles.get("users"));
			i++;
		}
		excel.write(os);


	}
	static void retriveValues(String rpmMappingLocation) throws Exception{
		Map<String, String> activationSpecValues = new HashMap<String,String>();
		Map<String, Map<String, String>> dBValues = new HashMap<String, Map<String,String>>();
		Map<String,Map<String, String>> cPValues = new HashMap<String, Map<String, String>>();
		Map<String, String> iMSValues = new HashMap<String, String>();
		Map<String, String> mQValues = new HashMap<String, String>();
		Map<String,String> j2CValues = new HashMap<String, String>();
		Map<String,String> lDAPValues = new HashMap<String, String>();
		Map<String, String> qcfValues = new HashMap<String, String>();
		File file = new File(rpmMappingLocation);
		String[] files = file.list();
		int count =0;
		for (int i = 0; i < files.length; i++) {
			String fileName = files[i];
			if(fileName.contains("sfDB2jccdatasourceXa.props")){
				count++;
				String qualifer = getValue(file+"\\"+files[i],"${qualifier}");
				String SubSystem = getValue(file+"\\"+files[i],"${databaseName}");
				String resourcee = fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length());
				Map<String, String>  qualifiers = new HashMap<String, String>();
				qualifiers.put(qualifer, SubSystem);
				dBValues.put(resourcee, qualifiers);

			}
			if(fileName.contains("sfDB2datasourceXa.props")){
				count++;
				String qualifer = getValue(file+"\\"+files[i],"${qualifier}");
				String SubSystem = getValue(file+"\\"+files[i],"${databaseName}");
				String resourcee = fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length());
				Map<String, String>  qualifiers = new HashMap<String, String>();
				qualifiers.put(qualifer, SubSystem);
				dBValues.put(resourcee, qualifiers);

			}
			else if(fileName.contains("sfCPConnectionfactory.props")){
				String url,prop1,prop2,prop3,prop4 = null,timeout;
				url = getValue(file+"\\"+files[i],"url");
				prop1 = getValue(file+"\\"+files[i],"prop1");
				prop2 = getValue(file+"\\"+files[i],"prop2");
				prop3 = getValue(file+"\\"+files[i],"prop3");
				timeout = prop1 = getValue(file+"\\"+files[i],"timeout");
				Map<String, String> cpProperties = new HashMap<String, String>();
				cpProperties.put("prop1", prop1);
				cpProperties.put("prop2", prop2);
				cpProperties.put("prop3", prop3);
				cpProperties.put("prop4", prop4);
				cpProperties.put("timeout", timeout);
				cpProperties.put("url", url);
				cPValues.put(fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length()),cpProperties);
			}
			else if(fileName.contains("sfIMSConnectionfactory.props")){
				iMSValues.put(fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length()), getValue(file+"\\"+files[i], "${dataStoreName}"));
			}
			else if(fileName.contains("sfMQQueueDestination.props")){
				mQValues.put(fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length()), getValue(file+"\\"+files[i],"${baseQueueName"));
			}
			else if(fileName.contains("sfJ2C.props")){

				j2CValues.put(fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length()), getValue(file+"\\"+files[i],"${userID}"));
			}
			else if(fileName.contains("sfLDAP2Connectionfactory.props")){
				lDAPValues.put(fileName.split("\\.")[2].substring(10,fileName.split("\\.")[2].length()), getValue(file+"\\"+files[i],"${url}"));

			}
			else if(fileName.contains("sfActivationSpec.props.ear")){
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(file+"\\"+files[i],"${resourceName}");
				hostName = getValue(file+"\\"+files[i],"${hostName}");
				channel = getValue(file+"\\"+files[i],"${channel}");
				activationSpecValues.put(resourceName, hostName+"#"+channel);
			}
			else if(fileName.contains("sfMQQueueConnectionfactory.props.ear")){
				String resourceName = null;
				String hostName = null;
				String channel = null;
				resourceName = getValue(file+"\\"+files[i],"${resourceName}");
				hostName = getValue(file+"\\"+files[i],"${hostName}");
				channel = getValue(file+"\\"+files[i],"${channel}");
				qcfValues.put(resourceName, hostName+"#"+channel);
			}
		}
		if (iMSValues.size() > 0) {
			writeValuesTOExcel(iMSValues, "IMS Values", "Resourcce",
					"Qualifier");
		}
		if (mQValues.size() > 0) {
			writeValuesTOExcel(mQValues, "MQ Values", "Resourcce", "QD");
		}
		if (j2CValues.size() > 0) {
			writeValuesTOExcel(j2CValues, "J2C Values", "Resourcce", "J2C Id");
		}
		if (lDAPValues.size() > 0) {
			writeValuesTOExcel(lDAPValues, "LDAP Values", "Resourcce",
					"LDAP Server");
		}
		if (dBValues.size() > 0) {
			writeDBValuesToExcel(dBValues);
		}
		if (cPValues.size() > 0) {
			writeCPValuesToExcel(cPValues);
		}
		if (activationSpecValues.size() > 0) {
			writeActivationSpecValuesToExcel("Activation Spec",
					activationSpecValues);
		}
		if (qcfValues.size() > 0) {
			writeActivationSpecValuesToExcel("QCF", qcfValues);
		}
	}
	static String getValue(String fileNamee,String seperator) throws IOException{
		FileInputStream fstream = new FileInputStream(fileNamee);
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String line;
		String subSystem = null;
		while ((line = br.readLine()) != null) {
			if(line.startsWith(seperator)){
				if(line.split("=").length>1)
					subSystem = line.split("=")[1];
				else  subSystem = null;
				return subSystem;
			}
		}
		return subSystem;
	}
	static void writeDBValuesToExcel(Map<String, Map<String, String>> DBValues) throws Exception{
		os = new FileOutputStream(new File("C:\\Users\\rsnz\\Desktop\\test.xls"));
		Sheet sheet = excel.createSheet("DB2Mappings");
		Font headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.RED.getIndex());
		CellStyle cellStyle = excel.createCellStyle();
		cellStyle.setFont(headerFont);
		Row row = sheet.createRow(0);
		Cell cell0 = row.createCell(0);
		cell0.setCellType(CellType.STRING);
		cell0.setCellValue("DB Resource");
		Cell cell1 = row.createCell(1);
		cell1.setCellType(CellType.STRING);
		cell1.setCellValue("Qualifier");
		Cell cell2 = row.createCell(2);
		cell2.setCellType(CellType.STRING);
		cell2.setCellValue("System");
		int i=1;
		for(String resource:DBValues.keySet()){
			Row row1 = sheet.createRow(i);
			Cell cel0 = row1.createCell(0);
			cel0.setCellType(CellType.STRING);
			cel0.setCellValue(resource);
			Map<String, String> qualifier = DBValues.get(resource);
			Cell cel1 = row1.createCell(1);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(qualifier.keySet().toArray()[0].toString());
			Cell cel2 = row1.createCell(2);
			cel2.setCellType(CellType.STRING);
			cel2.setCellValue(qualifier.get(qualifier.keySet().toArray()[0]));

			i++;

		}
		excel.write(os);

	}
	static void writeCPValuesToExcel(Map<String, Map<String, String>> CPValues) throws Exception{
		os = new FileOutputStream(new File("C:\\Users\\rsnz\\Desktop\\test.xls"));

		Sheet sheet = excel.createSheet("CP Values");
		Font headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle cellStyle = excel.createCellStyle();
		cellStyle.setFont(headerFont);
		Row row = sheet.createRow(0);

		Cell cell0 = row.createCell(0);
		cell0.setCellType(CellType.STRING);
		cell0.setCellValue("Resource");


		Cell cell1 = row.createCell(1);
		cell1.setCellType(CellType.STRING);
		cell1.setCellValue("Prop1");

		Cell cell2 = row.createCell(2);
		cell2.setCellType(CellType.STRING);
		cell2.setCellValue("Prop2");

		Cell cell3 = row.createCell(3);
		cell3.setCellType(CellType.STRING);
		cell3.setCellValue("Prop3");

		Cell cell4 = row.createCell(4);
		cell4.setCellType(CellType.STRING);
		cell4.setCellValue("Prop4");

		Cell cell5 = row.createCell(5);
		cell5.setCellType(CellType.STRING);
		cell5.setCellValue("timeout");

		Cell cell6 = row.createCell(6);
		cell6.setCellType(CellType.STRING);
		cell6.setCellValue("URL");
		int i=1;
		for(String resource: CPValues.keySet()){
			Row row1 = sheet.createRow(i);
			Cell cel0 = row1.createCell(0);
			cel0.setCellType(CellType.STRING);
			cel0.setCellValue(resource);

			Map<String, String> cpValues = CPValues.get(resource);

			Cell cel1 = row1.createCell(1);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(cpValues.get("prop1"));

			Cell cel2 = row1.createCell(2);
			cel2.setCellType(CellType.STRING);
			cel2.setCellValue(cpValues.get("prop2"));

			Cell cel3 = row1.createCell(3);
			cel3.setCellType(CellType.STRING);
			cel3.setCellValue(cpValues.get("prop3"));

			Cell cel4 = row1.createCell(4);
			cel4.setCellType(CellType.STRING);
			cel4.setCellValue(cpValues.get("prop4"));

			Cell cel5 = row1.createCell(5);
			cel5.setCellType(CellType.STRING);
			cel5.setCellValue(cpValues.get("timeout"));

			Cell cel6 = row1.createCell(6);
			cel6.setCellType(CellType.STRING);
			cel6.setCellValue(cpValues.get("url"));
			i++;
		}
		excel.write(os);
	}
	static void writeValuesTOExcel(Map<String, String> values,String SheetName,String header1,String header2) throws Exception{
		os = new FileOutputStream(new File("C:\\Users\\rsnz\\Desktop\\test.xls"));

		Sheet sheet = excel.createSheet(SheetName);
		Font headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle cellStyle = excel.createCellStyle();
		cellStyle.setFont(headerFont);
		Row row = sheet.createRow(0);

		Cell cell0 = row.createCell(0);
		cell0.setCellType(CellType.STRING);
		cell0.setCellValue(header1);


		Cell cell1 = row.createCell(1);
		cell1.setCellType(CellType.STRING);
		cell1.setCellValue(header2);
		int i=1;
		for(String resource: values.keySet()){
			Row row1 = sheet.createRow(i);
			Cell cel0 = row1.createCell(0);
			cel0.setCellType(CellType.STRING);
			cel0.setCellValue(resource);


			Cell cel1 = row1.createCell(1);
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(values.get(resource));
			i++;
		}

		excel.write(os);
	}
	static void writeActivationSpecValuesToExcel(String fileName, Map<String, String> activationSpecValues) throws Exception{
		Sheet sheet = excel.createSheet(fileName);
		Font headerFont = excel.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 14);
		headerFont.setColor(IndexedColors.BLACK.getIndex());
		CellStyle cellStyle = excel.createCellStyle();
		cellStyle.setFont(headerFont);
		Row row = sheet.createRow(0);
		Cell cell0 = row.createCell(0);
		cell0.setCellType(CellType.STRING);
		cell0.setCellValue("Resource Name");
		Cell cell2 = row.createCell(1);
		cell2.setCellType(CellType.STRING);
		cell2.setCellValue("Host Name");
		Cell cell3 = row.createCell(2);
		cell3.setCellType(CellType.STRING);
		cell3.setCellValue("Channel");
		int i=1;
		for(Map.Entry<String, String> resource:activationSpecValues.entrySet()){
			Row row1 = sheet.createRow(i);
			Cell cel0 = row1.createCell(0);
			cel0.setCellType(CellType.STRING);
			cel0.setCellValue(resource.getKey());
			String Value= resource.getValue();
			String[] val = Value.split("#");
			Cell cel1 = row1.createCell(1); 
			cel1.setCellType(CellType.STRING);
			cel1.setCellValue(val[0]);
			Cell cel2 = row1.createCell(2);
			cel2.setCellType(CellType.STRING);
			cel2.setCellValue(val[1]);
			i++;
		}
		excel.write(os);

	} 
}
