package com.ruco.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.ruco.model.InventoryIn;
import com.ruco.model.InventoryOut;
import com.ruco.repo.InventoryInRepository;
import com.ruco.repo.InventoryOutRepository;
import com.ruco.request.ReportRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportService {

	private final InventoryInRepository inventoryInRepository;
	private final InventoryOutRepository inventoryOutRepository;

	public ResponseEntity<?> getReports(ReportRequest request) {
		String type = request.getType();
		String from = request.getFrom();
		String to = request.getTo();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(from, formatter);
		LocalDateTime fromDate = date.atStartOfDay();

		date = LocalDate.parse(to, formatter);
		LocalDateTime toDate = date.atStartOfDay();

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Reports");

			String[] headers = { "S.No", "Branch Name", "Qty" };

			int rowIdx = 0;
			Row headerRow = sheet.createRow(rowIdx++);
			for (int i = 0; i < headers.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(getHeaderCellStyle(workbook));
			}

			switch (type) {

			case "Inventory Inward": {
				List<InventoryIn> data = inventoryInRepository.findAllByIsDeleteFalseAndSaveDateBetween(fromDate,
						toDate);
				return readWriteIn(data, sheet, headers, workbook, out);
			}

			case "Inventory Outward": {
				List<InventoryOut> data = inventoryOutRepository.findAllByIsDeleteFalseAndSaveDateBetween(fromDate,
						toDate);
				return readWriteOut(data, sheet, headers, workbook, out);
			}

			default:
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating Excel file");
		}
	}

	private ResponseEntity<?> readWriteOut(List<InventoryOut> data, Sheet sheet, String[] headers, Workbook workbook,
			ByteArrayOutputStream out) throws IOException {

		int rowIdx = 1;
		int sno = 1;

		for (InventoryOut order : data) {
			int col = 0;
			Row row = sheet.createRow(rowIdx++);
			row.createCell(col++).setCellValue(sno++);
			row.createCell(col++).setCellValue(order.getBranchId());
			row.createCell(col++).setCellValue(order.getQty());
		}

		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		workbook.write(out);

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx");
		return ResponseEntity.ok().headers(headersResponse).body(out.toByteArray());
	}

	private ResponseEntity<byte[]> readWriteIn(List<InventoryIn> data, Sheet sheet, String[] headers, Workbook workbook,
			ByteArrayOutputStream out) throws IOException {

		int rowIdx = 0, sno = 1;

		for (InventoryIn order : data) {
			int col = 0;
			Row row = sheet.createRow(rowIdx++);
			row.createCell(col++).setCellValue(sno++);
			row.createCell(col++).setCellValue(order.getBranchId());
			row.createCell(col++).setCellValue(order.getInwardQty());
		}

		for (int i = 0; i < headers.length; i++) {
			sheet.autoSizeColumn(i);
		}

		workbook.write(out);

		HttpHeaders headersResponse = new HttpHeaders();
		headersResponse.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reports.xlsx");
		return ResponseEntity.ok().headers(headersResponse).body(out.toByteArray());
	}

	private CellStyle getHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		style.setFont(font);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		return style;
	}

}
