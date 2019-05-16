package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class WordAttachment {
	
	public static void attachScreenshotsToWord(String wordDocName,String envName,File screenshotfile,String textValue) throws Exception {
		
		//String screenshotFolderPath="C:\\automation\\Technical_Checkout\\screenshots";
    	String wordDocPath="C:\\automation\\Technical_Checkout\\target";
    	wordDocName=wordDocName+"_"+envName+"_Checkout.docx";
    	File wordFile=new File(wordDocPath, wordDocName);
/*    	File screenshotFolder=new File(screenshotFolderPath);
    	File[] screenshots=screenshotFolder.listFiles();*/
    	XWPFDocument docum;
    	
    	if(wordFile.exists())
    	{
    	FileInputStream fis=new FileInputStream(wordFile);
    	docum= new XWPFDocument(fis);
    	// docum= new XWPFDocument(OPCPackage.open(fis)); 
    	}
    	else
    	{
    	 docum=new XWPFDocument();
    	}
    	
    	XWPFParagraph p=docum.createParagraph();
    	XWPFRun r=p.createRun();
	    	r.addBreak();
	    	r.setText(textValue);
	    	r.addPicture(new FileInputStream(screenshotfile), XWPFDocument.PICTURE_TYPE_PNG, screenshotfile.getName(), Units.toEMU(500) , Units.toEMU(300));

    	
    	FileOutputStream out = new FileOutputStream(wordFile);
        docum.write(out);
        out.close();
	}

}
