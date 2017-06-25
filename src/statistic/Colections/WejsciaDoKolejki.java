package statistic.Colections;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import statistic.Obj.EvWejscieDoKolejki;
import statistic.Obj.UpdateKlient;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Karolina on 23.06.2017.
 */
public class WejsciaDoKolejki extends LinkedList<EvWejscieDoKolejki> {



    public int ExportData(int rownum, Sheet sheet, Map<String, CellStyle> styles) {
        boolean styl = true;
        for (EvWejscieDoKolejki sr : this
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
            cell.setCellValue(sr.getWejscieDoKolejki().IDKlienta);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getWejscieDoKolejki().CzasZakupow);


            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getWejscieDoKolejki().NumerKasy);

            styl=!styl;
        }
        return rownum;
    }


}
