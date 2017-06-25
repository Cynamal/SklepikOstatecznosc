package statistic.Colections;

import federacje.statystyka.SredniCzasOczekiwania;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import statistic.Obj.EvRozpoczecieObslugi;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Karolina on 23.06.2017.
 */
public class RozpoczeciaOblugi extends LinkedList<EvRozpoczecieObslugi> {
   // public int CzasOczekiwania;
   // public int NumerKasy;
  //  public int IDKlienta;
    //double czas;
  public LinkedList<String >  getHedders()
    {
        LinkedList<String > hedders= new LinkedList<>();
        hedders.add("Czas");
        hedders.add("Numer Kasy");
        hedders.add("IDKlienta");
        return hedders;
    }



   public int ExportData(int rownum, Sheet sheet, Map<String, CellStyle> styles) {
       boolean styl = true;
       for (EvRozpoczecieObslugi sr : this
               ) {
           Row row = sheet.createRow(rownum++);
                int nr=0;

           Cell cell = row.createCell(nr++);
           if(styl) cell.setCellStyle(styles.get("cell"));
           else cell.setCellStyle(styles.get("cell2"));
           cell.setCellValue(sr.getCzas());


           cell = row.createCell(nr++);
           if(styl) cell.setCellStyle(styles.get("cell"));
           else cell.setCellStyle(styles.get("cell2"));
           cell.setCellValue(sr.getRozpoczecieObslugi().NumerKasy);


           cell = row.createCell(nr++);
           if(styl) cell.setCellStyle(styles.get("cell"));
           else cell.setCellStyle(styles.get("cell2"));
           cell.setCellValue(sr.getRozpoczecieObslugi().IDKlienta);

           styl=!styl;
       }
       return rownum;
   }
}
