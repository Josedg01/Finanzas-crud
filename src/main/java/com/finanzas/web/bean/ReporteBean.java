package com.finanzas.web.bean;

import com.finanzas.entity.Transaccion;
import com.finanzas.entity.Usuario;
import com.finanzas.service.TransaccionService;
import com.finanzas.service.UsuarioService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class ReporteBean implements Serializable {

    @Inject private TransaccionService transaccionService;
    @Inject private UsuarioService usuarioService;

    private Long usuarioId;
    private Date desde;
    private Date hasta;

    private List<Usuario> usuarios;
    private List<Transaccion> lista;

    @PostConstruct
    public void init() {
        usuarios = usuarioService.listar();
        buscar();
    }

    public void buscar() {
        
        if (desde != null && hasta != null && hasta.before(desde)) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Validación",
                    "La fecha 'Hasta' no puede ser menor que la fecha 'Desde'.");
            FacesContext.getCurrentInstance().validationFailed();
            return;
        }

        Date d = normalizarInicioDia(desde);
        Date h = normalizarFinDia(hasta);
        lista = transaccionService.buscarReporte(usuarioId, d, h);
    }

    // ===== Export =====
    public StreamedContent getExcelFile() {
        try {
            byte[] bytes = buildExcel();
            return DefaultStreamedContent.builder()
                    .name("reporte-transacciones.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> new ByteArrayInputStream(bytes))
                    .build();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo generar Excel: " + e.getMessage());
            return DefaultStreamedContent.builder()
                    .name("reporte-transacciones.xlsx")
                    .contentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                    .stream(() -> new ByteArrayInputStream(new byte[0]))
                    .build();
        }
    }

    public StreamedContent getPdfFile() {
        try {
            byte[] bytes = buildPdf();
            return DefaultStreamedContent.builder()
                    .name("reporte-transacciones.pdf")
                    .contentType("application/pdf")
                    .stream(() -> new ByteArrayInputStream(bytes))
                    .build();
        } catch (Exception e) {
            addMsg(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo generar PDF: " + e.getMessage());
            return DefaultStreamedContent.builder()
                    .name("reporte-transacciones.pdf")
                    .contentType("application/pdf")
                    .stream(() -> new ByteArrayInputStream(new byte[0]))
                    .build();
        }
    }

    private byte[] buildExcel() throws IOException {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Transacciones");

            CellStyle headerStyle = wb.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            Row header = sheet.createRow(0);
            String[] cols = {"ID", "Tipo", "Usuario", "Concepto", "Monto", "Fecha"};
            for (int i = 0; i < cols.length; i++) {
                Cell c = header.createCell(i);
                c.setCellValue(cols[i]);
                c.setCellStyle(headerStyle);
            }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            int r = 1;
            if (lista != null) {
                for (Transaccion t : lista) {
                    Row row = sheet.createRow(r++);
                    row.createCell(0).setCellValue(t.getId() != null ? t.getId() : 0);
                    row.createCell(1).setCellValue(nvl(t.getTipoTransaccion()));
                    row.createCell(2).setCellValue(t.getUsuario() != null ? nvl(t.getUsuario().getNombre()) : "");
                    row.createCell(3).setCellValue(getConcepto(t));
                    row.createCell(4).setCellValue(t.getMonto() != null ? t.getMonto().doubleValue() : 0d);
                    row.createCell(5).setCellValue(t.getFechaTransaccion() != null ? df.format(t.getFechaTransaccion()) : "");
                }
            }

            for (int i = 0; i < cols.length; i++) sheet.autoSizeColumn(i);

            wb.write(out);
            return out.toByteArray();
        }
    }

    private byte[] buildPdf() throws IOException, DocumentException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Document doc = new Document();
            PdfWriter.getInstance(doc, out);
            doc.open();

            com.lowagie.text.Font title = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            doc.add(new Paragraph("Reporte de Transacciones", title));
            doc.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.2f, 1.6f, 2.6f, 3.2f, 1.6f, 2.2f});

            addHeader(table, "ID");
            addHeader(table, "Tipo");
            addHeader(table, "Usuario");
            addHeader(table, "Concepto");
            addHeader(table, "Monto");
            addHeader(table, "Fecha");

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            if (lista != null) {
                for (Transaccion t : lista) {
                    table.addCell(val(t.getId()));
                    table.addCell(val(t.getTipoTransaccion()));
                    table.addCell(t.getUsuario() != null ? val(t.getUsuario().getNombre()) : "");
                    table.addCell(val(getConcepto(t)));
                    table.addCell(val(money(t.getMonto())));
                    table.addCell(t.getFechaTransaccion() != null ? val(df.format(t.getFechaTransaccion())) : "");
                }
            }

            doc.add(table);
            doc.close();
            return out.toByteArray();
        }
    }

    private void addHeader(PdfPTable t, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setBackgroundColor(new java.awt.Color(230, 230, 230));
        t.addCell(cell);
    }

    private String getConcepto(Transaccion t) {
        if (t.getEgreso() != null) return nvl(t.getEgreso().getDescripcion());
        if (t.getIngreso() != null) return nvl(t.getIngreso().getDescripcion());
        return "-";
    }

    private String nvl(String s) { return s == null ? "" : s; }

    private String money(BigDecimal v) { return v == null ? "" : v.toPlainString(); }

    private String val(Object o) { return o == null ? "" : String.valueOf(o); }

    // ===== fechas =====
    private Date normalizarInicioDia(Date d) {
        if (d == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    private Date normalizarFinDia(Date d) {
        if (d == null) return null;
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    private void addMsg(FacesMessage.Severity sev, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(sev, summary, detail));
    }

    // ===== Getters/Setters =====
    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public Date getDesde() { return desde; }
    public void setDesde(Date desde) { this.desde = desde; }

    public Date getHasta() { return hasta; }
    public void setHasta(Date hasta) { this.hasta = hasta; }

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Transaccion> getLista() { return lista; }
}