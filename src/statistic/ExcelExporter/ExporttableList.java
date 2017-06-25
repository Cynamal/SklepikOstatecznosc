package statistic.ExcelExporter;

import java.util.LinkedList;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Created by Marcin on 25.06.2017.
 */
public abstract class ExporttableList extends LinkedList{
    LinkedList<String> hedders=new LinkedList<>(); //hedders for data or calculated walues
   public  Workbook wb;
   public Sheet sheet;
    public void defPrintSetup()
    {
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        sheet.setHorizontallyCenter(true);
    }
    public ExporttableList(String sheet)
    {
        this.wb= new XSSFWorkbook();
        this.sheet = this.wb.createSheet(sheet);
    }
    public ExporttableList(Workbook wb,String sheet)
    {
        this.wb=wb;
        this.sheet = wb.createSheet(sheet);
    }
    public ExporttableList(Workbook wb,Sheet sheet)
    {
        this.wb=wb;
        this.sheet=sheet;
    }

    public abstract void exportdataXLSX() throws Exception;
    public abstract void countStatistic() throws Exception;


}
