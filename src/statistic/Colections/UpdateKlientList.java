package statistic.Colections;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import statistic.Obj.UpdateKasa;
import statistic.Obj.UpdateKlient;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Karolina on 23.06.2017.
 */
public class UpdateKlientList extends LinkedList<UpdateKlient> {


    public int ExportData(int rownum, Sheet sheet, Map<String, CellStyle> styles) {
        boolean styl = true;
        for (UpdateKlient sr : this
                ) {
            Row row = sheet.createRow(rownum++);
            int nr=0;

            Cell cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getTime());


            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKlient().IDKlienta);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKlient().Gotowka);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKlient().NumerKolejki);


            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKlient().NumerWKolejce);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKlient().uprzywilejowany);



            styl=!styl;
        }
        return rownum;
    }
}
