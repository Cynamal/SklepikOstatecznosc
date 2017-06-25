package statistic.ExcelExporter;

import federacje.statystyka.ListSredniCzasOczekiwania;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import statistic.Colections.*;
import statistic.Obj.*;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Marcin on 25.06.2017.
 */
public class Exporter {

    private XSSFWorkbook wb;

    public Exporter(ListSredniCzasOczekiwania listSredniCzasOczekiwania,RozpoczeciaOblugi rozp,RozpoczeciaPrzerwy rozpoczeciaPrzerwy,
                    UpdateKasaList updateKasaList,UpdateKlientList updateKlientList,WejsciaDoKolejki wejsciaDoKolejki,
                    ZadaniaUruchomieniaKasy zadaniaUruchomieniaKasy,ZakoczeniaPrzerwy zakoczeniaPrzerwy,ZakonczeniaObslugiKliena zakonczeniaObslugiKliena)
    {
        this.zakonczeniaObslugiKliena=zakonczeniaObslugiKliena;
        this.zakoczeniaPrzerwy=zakoczeniaPrzerwy;
        this.zadaniaUruchomieniaKasy=zadaniaUruchomieniaKasy;
        this.wejsciaDoKolejki=wejsciaDoKolejki;
        this.updateKlientList=updateKlientList;
        this.updateKasaList=updateKasaList;
        this.rozpoczeciaOblugi=rozp;
        this.rozpoczeciaPrzerwy=rozpoczeciaPrzerwy;
        li=listSredniCzasOczekiwania;
        chozeDirectorySwing();


    }
    private void chozeDirectorySwing()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Choose directory to save monitored values");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                this.directory=chooser.getSelectedFile().toString();
                if(directory.substring(directory.length() - 1)!="\\")
                {
                    directory=directory+"\\";
                }
                exportXLSX();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("No Selection ");
        }
    }
    private String directory;
    Map<String, CellStyle> styles;
    private static Map<String, CellStyle> createStyles(Workbook wb){
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)18);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("title", style);

        Font monthFont = wb.createFont();
        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);

        styles.put("header", style);


        monthFont.setFontHeightInPoints((short)11);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);

        styles.put("header with ", style);



        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put("cell", style);


        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);

        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("cell2", style);



        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }
    private void exportXLSX() throws Exception {
        this.wb= new XSSFWorkbook();
        styles=createStyles(wb);

        if(li!=null)exportSredniCzasOczekiwania();
       if(rozpoczeciaOblugi!=null) exportRozpoczeciaOblugi();
       if(rozpoczeciaPrzerwy!=null)exportRozpoczeciaPrzerwy();
        if(updateKasaList!=null)exportKasaUpdate();
        if(updateKlientList!=null)exportKlientUpdate();
        if(wejsciaDoKolejki!=null)exportWejsciaDoKolejki();
        if(zadaniaUruchomieniaKasy!=null)exportZadaniaUruchomieniaKasy();
        if(zakoczeniaPrzerwy!=null)exportZakoczeniaPrzerwy();
        if(zakonczeniaObslugiKliena!=null)exportZakonczeniaObslugiKliena();

        Save();
    }
    ListSredniCzasOczekiwania li;
    RozpoczeciaOblugi rozpoczeciaOblugi;
    UpdateKasaList updateKasaList;
    UpdateKlientList updateKlientList;
    WejsciaDoKolejki wejsciaDoKolejki;
    ZadaniaUruchomieniaKasy zadaniaUruchomieniaKasy;
    ZakoczeniaPrzerwy zakoczeniaPrzerwy;
    ZakonczeniaObslugiKliena zakonczeniaObslugiKliena;


    private void exportZakonczeniaObslugiKliena()
    {
        Sheet sheet = wb.createSheet("ZakonczeniaObslugiKliena");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("IDKlienta");
        hedders.add("CzasObslugi");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= zakonczeniaObslugiKliena.ExportData(rownum,sheet,styles);
    }
    private void exportZakoczeniaPrzerwy()
    {
        Sheet sheet = wb.createSheet("ZakoczeniaPrzerwy");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("CzasPrzerwy");
        hedders.add("NumerKasy");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= zakoczeniaPrzerwy.ExportData(rownum,sheet,styles);
    }
    private void exportZadaniaUruchomieniaKasy()
    {
        Sheet sheet = wb.createSheet("ZadaniaUruchomieniaKasy");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        rownum= createHedders(rownum,sheet ,hedders);
        rownum= zadaniaUruchomieniaKasy.ExportData(rownum,sheet,styles);
    }
    private void exportWejsciaDoKolejki()
    {
        Sheet sheet = wb.createSheet("WejsciaDoKolejki");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("IDKlienta");
        hedders.add("CzasZakupow");
        hedders.add("NumerKasy");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= wejsciaDoKolejki.ExportData(rownum,sheet,styles);
        int ileKas=0;
        for (EvWejscieDoKolejki ex:wejsciaDoKolejki
                ) {
            if(ileKas<ex.getWejscieDoKolejki().NumerKasy)ileKas=ex.getWejscieDoKolejki().NumerKasy;
        }
        if(ileKas>0)
        {
            LinkedList<Formulas> formulas=new LinkedList<>();
            formulas=CountIFFormula("b3","b"+(rownum),"Kasa",formulas,ileKas);


            int ile= CreateStatisticvar(sheet,styles,"b3","b"+(rownum),1,6,formulas);
        }
    }


    private void exportKlientUpdate()
    {
        Sheet sheet = wb.createSheet("Klient");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("IDKlienta");
        hedders.add("Gotowka");
        hedders.add("NumerKolejki");
        hedders.add("NumerWKolejce");
        hedders.add("uprzywilejowany");
        rownum= createHedders(rownum,sheet ,hedders);
        rownum= updateKlientList.ExportData(rownum,sheet,styles);
        int ileKas=0;
        for (UpdateKlient ex:updateKlientList
                ) {
            if(ileKas<ex.getKlient().IDKlienta)ileKas=ex.getKlient().IDKlienta;
        }
        if(ileKas>0)
        {
            LinkedList<Formulas> formulas=new LinkedList<>();
            formulas=CountIFFormula("b3","b"+(rownum),"Klient",formulas,ileKas);


            int ile= CreateStatisticvar(sheet,styles,"b3","b"+(rownum),1,6,formulas);
        }
    }
    private void exportKasaUpdate()
    {
        Sheet sheet = wb.createSheet("Kasa");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("Numer Kasy");
        hedders.add("Dlugosc");
        hedders.add("CzyPelna");
        hedders.add("CzyOtwarta");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= updateKasaList.ExportData(rownum,sheet,styles);
        int ileKas=0;
        for (UpdateKasa ex:updateKasaList
                ) {
            if(ileKas<ex.getKasa().NumerKasy)ileKas=ex.getKasa().NumerKasy;
        }
        if(ileKas>0)
        {
            LinkedList<Formulas> formulas=new LinkedList<>();
            formulas=CountIFFormula("b3","b"+(rownum),"Kasa",formulas,ileKas);


            int ile= CreateStatisticvar(sheet,styles,"b3","b"+(rownum),1,6,formulas);
        }
    }

    private void exportRozpoczeciaOblugi()
    {
        Sheet sheet = wb.createSheet("rozpoczeciaOblugi");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("Numer Kasy");
        hedders.add("IDKlienta");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= rozpoczeciaOblugi.ExportData(rownum,sheet,styles);
        int ileKas=0;
        for (EvRozpoczecieObslugi ex:rozpoczeciaOblugi
             ) {
            if(ileKas<ex.getRozpoczecieObslugi().NumerKasy)ileKas=ex.getRozpoczecieObslugi().NumerKasy;
        }
        if(ileKas>0)
        {
            LinkedList<Formulas> formulas=new LinkedList<>();
            formulas=CountIFFormula("b3","b"+(rownum),"Kasa",formulas,ileKas);


            int ile= CreateStatisticvar(sheet,styles,"b3","b"+(rownum),1,5,formulas);
        }


    }

    RozpoczeciaPrzerwy rozpoczeciaPrzerwy;
    LinkedList<Formulas> CountIFFormula(String StartRow,String EndRow,String co,LinkedList<Formulas> formulas,int ileKas)
    {
        for (int ii=0;ii<ileKas;ii++)
        {
            formulas.add(new Formulas("COUNTIF("+StartRow+":"+EndRow+","+ii+")", co+":"+ii+" ile zadan"));
        }
        return formulas;
    }
    private void exportRozpoczeciaPrzerwy()
    {
        Sheet sheet = wb.createSheet("RozpoczeciaPrzerwy");
        defPrintSetup(sheet);
        int rownum=0;
        rownum= createTitleRowData(rownum,sheet ,"$A$1:$E$1");
        LinkedList<String> hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("Numer Kasy");

        rownum= createHedders(rownum,sheet ,hedders);
        rownum= rozpoczeciaPrzerwy.ExportData(rownum,sheet,styles);
        int ileKas=0;
        for (EvRozpocznijPrzerwe ex:rozpoczeciaPrzerwy
                ) {
            if(ileKas<ex.getRozpocznijPrzerwe().NumerKasy)ileKas=ex.getRozpocznijPrzerwe().NumerKasy;
        }
        if(ileKas>0)
        {
            LinkedList<Formulas> formulas=new LinkedList<>();
            formulas=CountIFFormula("b3","b"+(rownum),"Kasa",formulas,ileKas);


            int ile= CreateStatisticvar(sheet,styles,"b3","b"+(rownum),1,5,formulas);
        }
    }
    private void exportSredniCzasOczekiwania()
    {
        Sheet sheet = wb.createSheet("SredniCzasOczekiwania");
        defPrintSetup(sheet);
        int rownum=0;
       rownum= createTitleRowData(rownum,sheet ,"$A$1:$B$1");
        LinkedList<String > hedders= new LinkedList<>();
        hedders.add("Czas rozpoczecia");
        hedders.add("Czas zakonczenia");
        hedders.add("delta");
       rownum= createHedders(rownum,sheet ,hedders);
        rownum= li.ExportDataAndCountDelta(rownum,sheet,styles);
       int created= CreateStatisticvar(sheet,styles,"C3","C"+(rownum),1,5);
        createcommentForStatistic(sheet,styles,created,new String[]{"Delta"},5);


    }
    private void createcommentForStatistic(Sheet sheet,Map<String, CellStyle> styles,int created,String[] names,int startColumn)
    {
        //Title
        Row titleRow = sheet.getRow(0);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(startColumn);
        titleCell.setCellValue("Statistic");
        titleCell.setCellStyle(styles.get("title"));
        // System.out.println();
        sheet.addMergedRegion(CellRangeAddress.valueOf(getCellName(titleCell)+":"+getCellName(sheet.getRow(0).createCell(created+startColumn))));
        int number=1;
        for(String stat:names)
        {

            Row HeaderRow = sheet.getRow(number);
            Row Nextrow= sheet.getRow(number+1);
            Cell nexcell = Nextrow.createCell(startColumn-1);
            titleRow.setHeightInPoints(45);
            Cell headerCell = HeaderRow.createCell(startColumn-1);
            headerCell.setCellValue(stat);
            //   System.out.println(getCellName(headerCell));
            headerCell.setCellStyle(styles.get("header"));
         //   System.out.println(getCellName(sheet.getRow(number+1).getCell(startColumn-1)));
            sheet.addMergedRegion(CellRangeAddress.valueOf(getCellName(headerCell)+":"+getCellName(nexcell)));
            number+=2;
        }


    }
    private class Formulas
    {
        Formulas(String formula, String headerName)
        {
            this.formula=formula;
            this.headerName=headerName;
        }
        String formula;
        String headerName;
    }
    private static String getCellName(Cell cell)
    {
        return CellReference.convertNumToColString(cell.getColumnIndex()) + (cell.getRowIndex() + 1);
    }
    private int CreateStatisticvar(Sheet sheet,Map<String, CellStyle> styles,Cell startcell,Cell endcell,int startRow,int startColumn) {

    return CreateStatisticvar(sheet,styles,getCellName(startcell),getCellName(endcell),startRow,startColumn);
    }
    private int CreateStatisticvar(Sheet sheet,Map<String, CellStyle> styles,String startcell,String endcell,int startRow,int startColumn)
    {
        int place=startColumn;
        int Created=0;
        Row headerRowt;
        Row Row;

        int LastRow=sheet.getLastRowNum();
        LinkedList<Formulas> formulas =new LinkedList<Formulas>();
        formulas.add(new Formulas("AVERAGE("+startcell+":"+endcell+")", "Arithmetic mean"));
        formulas.add(new Formulas("HARMEAN("+startcell+":"+endcell+")", "Harmonic mean"));
        formulas.add(new Formulas("max("+startcell+":"+endcell+")-min("+startcell+":"+endcell+")", "Value Range"));
        formulas.add(new Formulas("VAR("+startcell+":"+endcell+")", "Variance"));
        formulas.add(new Formulas("STDEVP("+startcell+":"+endcell+")", "Standard deviation"));
        formulas.add(new Formulas("min("+startcell+":"+endcell+")", "Minimal value"));
        formulas.add(new Formulas("max("+startcell+":"+endcell+")", "Maximal value"));
        formulas.add(new Formulas("COUNT("+startcell+":"+endcell+")", "Number of samples"));
       // formulas.add(new Formulas("IF(K3<120,K3-1,120)", "Free Degrees"));
       // formulas.add(new Formulas("T.DIST(D10,L3,TRUE)", "T Distribution"));
       // formulas.add(new Formulas("(H3*M3)/(SQRT(K3))", "Interval"));
       // formulas.add(new Formulas("(D3-N3)", "interval Estimation1"));
       // formulas.add(new Formulas("(D3+N3)", "interval Estimation2"));
        //formulas.add(new Formulas("CONFIDENCE(B3:B"+(LastRow+1)+")", "Estimated value"));



        headerRowt= sheet.getRow(startRow);
        Row= sheet.getRow(startRow+1);
        for(Formulas formula:formulas)
        {

            Cell cell = headerRowt.createCell(place);

            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(formula.headerName);


            cell = Row.createCell(place);
            cell.setCellFormula(formula.formula);
            cell.setCellStyle(styles.get("cell"));

            place++;
            Created++;
        }

        return Created;

    }
    private int CreateStatisticvar(Sheet sheet,Map<String, CellStyle> styles,String startcell,String endcell,int startRow,int startColumn,LinkedList<Formulas> formulas)
    {
        int place=startColumn;
        int Created=0;
        Row headerRowt;
        Row Row;

        int LastRow=sheet.getLastRowNum();
        headerRowt= sheet.getRow(startRow);
        Row= sheet.getRow(startRow+1);
        for(Formulas formula:formulas)
        {

            Cell cell = headerRowt.createCell(place);

            cell.setCellStyle(styles.get("header"));
            cell.setCellValue(formula.headerName);


            cell = Row.createCell(place);
            cell.setCellFormula(formula.formula);
            cell.setCellStyle(styles.get("cell"));

            place++;
            Created++;
        }

        return Created;

    }


    private int createHedders(int rownum,Sheet sheet ,LinkedList<String > hedders)
    {
        //hedders
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(25);
        Cell headerCell;
        for(int i=0;i<hedders.size();i++)
        {
            headerCell = headerRow.createCell(i);
            headerCell.setCellStyle(styles.get("header"));
            headerCell.setCellValue(hedders.get(i));
        }
        return rownum;
    }
    private int createTitleRowData(int rownum,Sheet sheet ,String Zakres)
    {
        //Title
        Row titleRow = sheet.createRow(rownum++);
        titleRow.setHeightInPoints(45);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Row data");
        titleCell.setCellStyle(styles.get("title"));
        sheet.addMergedRegion(CellRangeAddress.valueOf(Zakres));
        return rownum;
    }
    private void Save() throws IOException {
        String file = directory+"export.xlsx";

        FileOutputStream out = new FileOutputStream(file);
        wb.write(out);
        out.close();
        wb.close();
    }
    public void defPrintSetup( Sheet sheet)
    {
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);

        sheet.setHorizontallyCenter(true);
    }
}
