package com.ruco.service.audit.hibernate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.ruco.service.audit.AuditLog;
import com.ruco.service.audit.AuditLogRepository;

@Service
public class AuditPDFService {

	@Autowired
	private AuditLogRepository auditLogRepository;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
	private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);
	private static final Font FIELD_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD);
	private static final Font OLD_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.RED);
	private static final Font NEW_FONT = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, new BaseColor(0, 110, 0));

	@Transactional(readOnly = true)
	public File writeAuditPdf(Long userId) {

		try (Stream<AuditLog> logs = auditLogRepository.streamByUserId(userId)) {
			File tempFile = File.createTempFile("audit_report.csv", ".csv");
			tempFile.deleteOnExit();
			Document document = new Document(PageSize.A4, 36, 36, 36, 36);
			OutputStream out = new FileOutputStream(tempFile);
			PdfWriter.getInstance(document, out);
			document.open();

			// Title
			Paragraph title = new Paragraph("Audit Report", HEADER_FONT);
			title.setAlignment(Element.ALIGN_CENTER);
			title.setSpacingAfter(15f);
			document.add(title);

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

			logs.forEach(audit -> {
				try {
					PdfPTable headerTable = new PdfPTable(2);
					headerTable.setTotalWidth(225f);
					headerTable.setLockedWidth(true); 
					headerTable.setHorizontalAlignment(Element.ALIGN_CENTER);
					headerTable.getDefaultCell().setBorder(Rectangle.BOX);
					headerTable.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

					// General info
					addRow(headerTable, "Table", audit.getTableName());
					addRow(headerTable, "Action", audit.getAction().name());
					addRow(headerTable, "Timestamp", formatter.format(audit.getTimestamp()));
					
					PdfPTable table = new PdfPTable(2);
					table.setWidthPercentage(100);
					table.setSpacingBefore(10f);
					table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

					// Details
					PdfPCell detailsCell = new PdfPCell();
					detailsCell.setColspan(2);
					detailsCell.setBorder(Rectangle.NO_BORDER);
					detailsCell.addElement(formatDetailsAsPdf(audit.getDetails()));
					table.addCell(detailsCell);

					document.add(headerTable);
					document.add(table);

					// Separator line
					LineSeparator line = new LineSeparator();
					line.setLineColor(BaseColor.LIGHT_GRAY);
					document.add(new Chunk(line));

				} catch (Exception e) {
					System.err.println("Error writing audit record: " + e.getMessage());
				}
			});

			document.close();
			out.flush();
			return tempFile;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating PDF file", e);
		}
	}

	private void addRow(PdfPTable table, String label, String value) {
		PdfPCell cell1 = new PdfPCell(new Phrase(label, FIELD_FONT));
		PdfPCell cell2 = new PdfPCell(new Phrase(value, FIELD_FONT));
		cell1.setBorder(Rectangle.BOX);
		cell2.setBorder(Rectangle.BOX);
		cell1.setPadding(4f);
		cell2.setPadding(4f);
		table.addCell(cell1);
		table.addCell(cell2);
	}

	private Element formatDetailsAsPdf(String detailsJson) {
		Paragraph p = new Paragraph();
		p.setSpacingBefore(5f);
		if (detailsJson == null || detailsJson.isEmpty()) {
			p.add(new Phrase("No details", NORMAL_FONT));
			return p;
		}

		try {
			JsonNode root = objectMapper.readTree(detailsJson);
			root.fields().forEachRemaining(entry -> {
				String fieldName = entry.getKey();
				JsonNode valueNode = entry.getValue();

				if (fieldName.equals("id")
						|| (!valueNode.toString().contains("old") && !valueNode.toString().contains("new"))) {
					p.add(new Chunk(fieldName + " : { ", FIELD_FONT));
					p.add(new Chunk(valueNode.asText(), NEW_FONT));
					p.add(new Chunk(" }\n", NORMAL_FONT));
				}

				else {
					String oldVal = valueNode.has("old") ? valueNode.get("old").asText("") : "";
					String newVal = valueNode.has("new") ? valueNode.get("new").asText("") : "";

					p.add(new Chunk(fieldName + " : { ", FIELD_FONT));
					p.add(new Chunk(oldVal, OLD_FONT));
					p.add(new Chunk(" } { ", FIELD_FONT));
					p.add(new Chunk(newVal, NEW_FONT));
					p.add(new Chunk(" }\n", FIELD_FONT));
				}
			});
		} catch (Exception e) {
			p.add(new Phrase(detailsJson, NORMAL_FONT));
		}
		return p;
	}
}
