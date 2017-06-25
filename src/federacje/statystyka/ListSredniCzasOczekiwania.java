package federacje.statystyka;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Marcin on 24.06.2017.
 */
public class ListSredniCzasOczekiwania extends LinkedList<SredniCzasOczekiwania>{
    public SredniCzasOczekiwania getbyIDKlient(int IDKlient) throws Exception {
        for (SredniCzasOczekiwania sr: this
             ) {
            if(sr.IDKlienta==IDKlient)return sr;
        }
        throw new Exception("Nie znaleziono wpisu");
    }
    public double ObliczSredni()
    {
        int suma=0;
        int iter=0;
        for (SredniCzasOczekiwania sr: this
                ) {
            if(sr.CzasWyjsciaZKolejki!=-1.0)
            {
                suma+=sr.CzasWyjsciaZKolejki-sr.CzasWejsciaDoKolejki;
                iter++;
            }

        }
       // System.out.println("sume: "+suma+" iter: "+iter+" Rozmiar listy: "+this.size());
        return 1.0*suma/(1.0*iter);
    }
    public int ExportDataAndCountDelta(int rownum, Sheet sheet, Map<String, CellStyle> styles)
    {
        boolean styl=true;
        for (SredniCzasOczekiwania sr:this
             ) {
            if(sr.CzasWyjsciaZKolejki!=-1)
            {
                Row row = sheet.createRow(rownum++);


                Cell cell = row.createCell(0);
                if(styl) cell.setCellStyle(styles.get("cell"));
                else cell.setCellStyle(styles.get("cell2"));

                cell.setCellValue(sr.CzasWejsciaDoKolejki);
                cell = row.createCell(1);
                if(styl) cell.setCellStyle(styles.get("cell"));
                else cell.setCellStyle(styles.get("cell2"));
                cell.setCellValue(sr.CzasWyjsciaZKolejki);

                cell = row.createCell(2);
                if(styl) cell.setCellStyle(styles.get("cell"));
                else cell.setCellStyle(styles.get("cell2"));
                cell.setCellFormula(sr.CzasWyjsciaZKolejki+"-"+sr.CzasWejsciaDoKolejki);
                styl=!styl;
            }

        }
        return rownum;
    }
}
