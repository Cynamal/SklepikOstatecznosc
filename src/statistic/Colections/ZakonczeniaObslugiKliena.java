package statistic.Colections;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import statistic.Obj.EvZakoczeniePrzerwy;
import statistic.Obj.EvZakonczenieObslugiKlienta;
import statistic.Obj.ZadanieUruchomieniaKasy;

import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Marcin on 23.06.2017.
 */
public class ZakonczeniaObslugiKliena extends LinkedList<EvZakonczenieObslugiKlienta> {


    public int ExportData(int rownum, Sheet sheet, Map<String, CellStyle> styles) {
        boolean styl = true;
        for (EvZakonczenieObslugiKlienta sr : this
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
            cell.setCellValue(sr.getZakonczanieObslugiKlienta().IDKlienta);


            cell = row.createCell(nr++);
            if(styl) cell.setCellStyle(styles.get("cell"));
            else cell.setCellStyle(styles.get("cell2"));
            cell.setCellValue(sr.getZakonczanieObslugiKlienta().CzasObslugi);

            styl=!styl;
        }
        return rownum;
    }




}
