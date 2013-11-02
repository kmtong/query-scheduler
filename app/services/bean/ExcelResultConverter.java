package services.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import data.OutputContext;
import data.OutputResult;
import data.QueryResult;

public class ExcelResultConverter implements IConverter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see services.bean.IConverter#extractResult(data.QueryResult,
	 * org.apache.camel.Message)
	 */
	@Override
	public OutputResult extractResult(OutputContext context, QueryResult result) {

		Workbook wb = new XSSFWorkbook();
		Map<String, CellStyle> styles = createStyles(wb);
		Sheet sheet = wb.createSheet(result.getName());
		// turn off gridlines
		sheet.setDisplayGridlines(false);
		sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the header row: centered text in 48pt font
		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(12.75f);
		int col = 0;
		for (String header : result.getHeaders()) {
			Cell cell = headerRow.createCell(col++);
			cell.setCellValue(header);
			cell.setCellStyle(styles.get("header"));
		}

		int rownum = 1;
		for (data.Row r : result.getData()) {
			Row row = sheet.createRow(rownum++);
			int datacol = 0;
			for (Object column : r.getData()) {
				Cell cell = row.createCell(datacol++);
				String styleName = "cell_normal";
				cell.setCellValue(column.toString());
				cell.setCellStyle(styles.get(styleName));
			}
		}
		// freeze the first row
		sheet.createFreezePane(0, 1);

		// auto content sizing (need Java2D)
		col = 0;
		for (String header : result.getHeaders()) {
			sheet.autoSizeColumn(col++);
		}

		return new OutputResult(
				result.getName(),
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				getInputStream(wb), true);
	}

	/**
	 * create a library of cell styles
	 */
	private static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE
				.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(false);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(false);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(false);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		return styles;
	}

	private static CellStyle createBorderedStyle(Workbook wb) {
		CellStyle style = wb.createCellStyle();
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setRightBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return style;
	}

	private InputStream getInputStream(Workbook wb) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			wb.write(out);
			ByteArrayInputStream in = new ByteArrayInputStream(
					out.toByteArray());
			return in;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
