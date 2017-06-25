package statistic.Colections;

import objects.Kasa;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import statistic.Obj.EvRozpocznijPrzerwe;
import statistic.Obj.UpdateKasa;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Marcin on 23.06.2017.
 */
public class UpdateKasaList extends LinkedList<UpdateKasa> {
    public int ExportData(int rownum, Sheet sheet, Map<String, CellStyle> styles) {
        boolean styl = true;
        for (UpdateKasa sr : this
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
            cell.setCellValue(sr.getKasa().NumerKasy);


            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKasa().Dlugosc);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKasa().CzyPelna);

            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getKasa().CzyOtwarta);


            styl=!styl;
        }
        return rownum;
    }
}
