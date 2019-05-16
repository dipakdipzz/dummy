package helpers;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AppData extends Browsers {

	FileInputStream fis;
	XSSFWorkbook wb;
	private static XSSFSheet s;
	int rowSize;
	String vmName;
		public AppData (String sheetname) throws IOException {
			fis=new FileInputStream("C:\\automation\\Technical_Checkout\\src\\test\\resources\\Console.xlsx");
			wb= new XSSFWorkbook(fis);
			s= wb.getSheet(sheetname);
		}
		public int numberOfData() {
			rowSize=s.getLastRowNum();
			return rowSize;
		}
		public String getVM(int row) {
			try {
			vmName=s.getRow(row).getCell(0).getStringCellValue();
				}
			catch(Exception e) {
				System.out.println("end of row");
				driver.close();
			}
			return vmName;

		}
			
		public static String getApplication(int row) throws IOException {
				String applicationName=s.getRow(row).getCell(1).getStringCellValue();
				//s.getRow(row).getCell(1).setCellType(HSSFCell.CELL_TYPE_STRING);
				return applicationName;
			
		}
		public static String getEnvironment(int row) throws IOException {
			String environmentName=s.getRow(row).getCell(2).getStringCellValue();
			return environmentName;
		
	}

}
