/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.gob.gadmsc.spmp.jsf.modulos;

import ec.gob.gadmsc.spmp.ejb.entidades.Chofer;
import ec.gob.gadmsc.spmp.ejb.entidades.Equipo;
import ec.gob.gadmsc.spmp.ejb.entidades.FechaTransporte;
import ec.gob.gadmsc.spmp.ejb.entidades.Material;
import ec.gob.gadmsc.spmp.ejb.entidades.Usuario;
import ec.gob.gadmsc.spmp.jsf.base.BaseControlador;
import ec.gob.gadmsc.spmp.jsf.base.NavegacionControlador;
import ec.gob.gadmsc.spmp.servicios.CargaTransportadaServicio;
import ec.gob.gadmsc.spmp.servicios.ChoferServicio;
import ec.gob.gadmsc.spmp.servicios.EquipoFechaServicio;
import ec.gob.gadmsc.spmp.servicios.EquipoServicio;
import ec.gob.gadmsc.spmp.servicios.FechaTransporteServicio;
import ec.gob.gadmsc.spmp.servicios.MaterialServicio;
import ec.gob.gadmsc.spmp.servicios.UsuarioServicio;
import ec.gob.gadmsc.spmp.tools.AgrupaVolquetas;
import ec.gob.gadmsc.spmp.tools.BusquedaEquipos;
import ec.gob.gadmsc.spmp.tools.ManejoFechas;
import ec.gob.gadmsc.spmp.tools.Meses;
import ec.gob.gadmsc.spmp.tools.ReporteException;
import ec.gob.gadmsc.spmp.tools.TablaCarga;
import ec.gob.gadmsc.spmp.tools.TotalesCarga;
import static ec.gob.gadmsc.spmp.tools.Validaciones.*;
import java.util.Date;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.text.ParseException;
import java.util.*;
import javax.faces.bean.ManagedProperty;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.primefaces.context.RequestContext;

/**
 *
 * @author desoeco
 */
@ManagedBean
@ViewScoped
public class ReporteControlador {

    //<editor-fold desc="Atributos-propiedades" defaultstate="collapsed">
    private static final Logger LOGGER = Logger.getLogger(ReporteControlador.class.getName());
    private List<Object[]> listaCargaTransportada;
    private List<Object[]> listaEquipoFecha;
    private List<Object[]> listaCarga;
    private List<Object[]> listaResumenCombustible;
    private List<Usuario> listaVolquetas;
    private List<Chofer> listaChoferes;
    private List<Chofer> listaChoferesNoAsignados;
    private List<Equipo> listaEquipos;
    private List<FechaTransporte> listaFechasTransporte;
    private LinkedList<String> listaFechasString;
    private List<String> listaFechasRango;
    private List<Integer> listaAnios;
    private List<AgrupaVolquetas> listaAgrupacionVolq;
    private List<Material> listaMateriales;
    private List<TablaCarga> listaTablaCargaResumen;
    private Date fechaDesde;
    private Date fechaHasta;
    private final ManejoFechas fechaCadena;
    private TablaCarga cargaResumen;
    private TotalesCarga totales;
    private Usuario volquetaSeleccionada;
    private Usuario volqueta;
    private Equipo equipo;
    private Equipo equipoSeleccionado;
    private Material material;
    private Chofer chofer;
    private Chofer choferSeleccionado;
    private String maquinaria;
    private String horaActual;
    private boolean ingreso;
    private boolean actualiza;
    private boolean habilitaChoferAsig;
    private boolean textoIngreso;
    private int anioResumen;
    private int buscaVolqueta;
    private int buscaEquipo;
    private int idChoferVolq;
    private int idChoferEq;
    private int idChoferAsignado;
    //</editor-fold>

    //<editor-fold desc="Servicios" defaultstate="collapsed">
    @EJB
    private CargaTransportadaServicio cargaTransportadaServicio;
    @EJB
    private FechaTransporteServicio fechaTransporteServicio;
    @EJB
    private MaterialServicio materialServicio;
    @EJB
    private EquipoFechaServicio equiFechaServicio;
    @EJB
    private EquipoServicio equipoServicio;
    @EJB
    private UsuarioServicio usuarioServicio;
    @EJB
    private ChoferServicio choferServicio;

    @ManagedProperty("#{baseControlador}")
    private BaseControlador baseControlador;

    @ManagedProperty("#{navegacionControlador}")
    private NavegacionControlador navegacionControlador;
    //</editor-fold>  

    //<editor-fold desc="Constructor" defaultstate="collapsed">
    public ReporteControlador() {
        fechaCadena = new ManejoFechas();
        cargaResumen = new TablaCarga();
        chofer = new Chofer();
        totales = new TotalesCarga();
//        equipoSeleccionado = new Equipo();
//        volquetaSeleccionada = new Usuario();
        listaFechasString = new LinkedList<>();
        listaTablaCargaResumen = new ArrayList<>();
        ingreso = false;
        actualiza = true;
    }
    //</editor-fold>

    //<editor-fold desc="Post Constructor" defaultstate="collapsed">
    @PostConstruct
    public void inicializar() {
        listaFechasTransporte = fechaTransporteServicio.findAll();
        listaMateriales = materialServicio.findAll();
        listaVolquetas = usuarioServicio.buscarVolquetas();
        listaEquipos = equipoServicio.findAll();
        listaChoferes = choferServicio.findAll();
        listarFechas();
        listaAnios = fechaTransporteServicio.listarAnios();
    }
    //</editor-fold>

    //<editor-fold desc="Metodos" defaultstate="collapsed">
    public void postProcessXLSAnual(Object document) {

        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        wb.setSheetName(0, "Resumen carga");

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//        cellStyle.setFillForegroundColor((short) 70);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        int rows = sheet.getLastRowNum();
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 4, 4), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 4, 4), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 5, 5), sheet, wb);

        sheet.shiftRows(0, sheet.getLastRowNum(), 2);
        HSSFRow hssfRowNew;
        HSSFCell cellNew;
        HSSFCellStyle estiloCelda = wb.createCellStyle();
        estiloCelda.setAlignment(HSSFCellStyle.ALIGN_LEFT);

        HSSFFont fuenteAnio = wb.createFont();
        fuenteAnio.setBold(true);
        fuenteAnio.setFontName("Arial");
        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        fuenteAnio.setFontHeightInPoints((short) 13);
        cellStyle.setFont(fuenteAnio);

        hssfRowNew = sheet.createRow(0);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Año de resumen: ");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(anioResumen);
        cellNew.setCellStyle(cellStyle);

        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
//        cellStyle.setFillForegroundColor((short) 70);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        rows = sheet.getLastRowNum();
        hssfRowNew = sheet.createRow(rows + 1);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Total volquetas");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalVolquetadasRelleno());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(2);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalVolquetadasArena());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(3);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalVolquetadasBloque());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(4);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalVolquetadasRipio());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(5);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalVolquetadasPolvo());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        hssfRowNew = sheet.createRow(rows + 2);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Total m3");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalM3Relleno());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(2);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalM3Arena());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(3);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalM3Bloque());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(4);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalM3Ripio());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(5);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(totales.getTotalM3Polvo());
        cellNew.setCellStyle(estiloCelda);
        cellNew.setCellStyle(cellStyle);

        //colocar fórmulas en Excel
//        hssfRowNew = sheet.createRow(rows + 4);
//        cellNew = hssfRowNew.createCell(0);
//        cellNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
//        cellNew.setCellFormula("SUM(B4:B6)");
        sheet.autoSizeColumn(0);
    }

    public void postProcessXLSAnualCombustible(Object document) {

        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        HSSFRow header = sheet.getRow(0);
        HSSFRow footer = sheet.getRow(rows);
        wb.setSheetName(0, "Resumen combustible");

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        for (int i = 0; i < footer.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = footer.getCell(i);
            cell.setCellStyle(cellStyle);
        }

        //Bordes para la tabla
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows - 1, 0, 0), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows - 1, 0, 0), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows - 1, 2, 2), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows - 1, 2, 2), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows - 1, 3, 3), sheet, wb);

        sheet.shiftRows(0, sheet.getLastRowNum(), 2);
        HSSFRow hssfRowNew;
        HSSFCell cellNew;

        HSSFFont fuenteAnio = wb.createFont();
        fuenteAnio.setBold(true);
        fuenteAnio.setFontName("Arial");
        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        fuenteAnio.setFontHeightInPoints((short) 13);
        cellStyle.setFont(fuenteAnio);

        hssfRowNew = sheet.createRow(0);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Año de resumen: ");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue(anioResumen);
        cellNew.setCellStyle(cellStyle);

        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 1), sheet, wb);
        sheet.autoSizeColumn(0);
    }

    public void postProcessXLSCarga(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow header = sheet.getRow(0);
        int rows = sheet.getLastRowNum();

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");
        fuente.setFontHeightInPoints((short) 11);

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        cellStyle.setFont(fuente);

        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        if (maquinaria.equals("Volqueta")) {
            //Bordes para la tabla volqueta
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 4, 4), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 4, 4), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 6, 6), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 6, 6), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 7, 7), sheet, wb);
        } else {
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);
        }

        long fecha1 = fechaDesde.getTime();
        long fecha2 = fechaHasta.getTime();
        long restaFecha = fecha1 - fecha2;
        sheet.shiftRows(0, sheet.getLastRowNum(), 3);
        if (volqueta != null) {
            wb.setSheetName(0, "Carga Volqueta");
            HSSFRow hssfRowNew;
            HSSFCell cellNew;
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFont(fuente);

            hssfRowNew = sheet.createRow(0);
            cellNew = hssfRowNew.createCell(0);
            cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellNew.setCellValue("Chofer:");
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(1);
            cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellNew.setCellValue(volqueta.getFkChoferCodigo().getChoferNombre() + " " + volqueta.getFkChoferCodigo().getChoferApellido());
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(2);
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(3);
            cellNew.setCellStyle(estiloCelda);

            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);

            if (restaFecha == 0) {
                Object[] obj = listaCargaTransportada.get(0);
                hssfRowNew = sheet.createRow(1);
                cellNew = hssfRowNew.createCell(0);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue("Hora entrada:");
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(1);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue(obj[8].toString());
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(2);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue("Hora salida:");
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(3);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue(obj[9].toString());
                cellNew.setCellStyle(estiloCelda);

                RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(2);
        } else if (equipo != null) {
            wb.setSheetName(0, "Equipo caminero");
            HSSFRow hssfRowNew;
            HSSFCell cellNew;
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
            estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            estiloCelda.setFont(fuente);

            hssfRowNew = sheet.createRow(0);
            cellNew = hssfRowNew.createCell(0);
            cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellNew.setCellValue("Chofer:");
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(1);
            cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
            cellNew.setCellValue(equipo.getFkChoferCodigo().getChoferNombre() + " " + equipo.getFkChoferCodigo().getChoferApellido());
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(2);
            cellNew.setCellStyle(estiloCelda);

            cellNew = hssfRowNew.createCell(3);
            cellNew.setCellStyle(estiloCelda);

            RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
            RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);

            if (restaFecha == 0) {
                Object[] obj = listaEquipoFecha.get(0);
                hssfRowNew = sheet.createRow(1);
                cellNew = hssfRowNew.createCell(0);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue("Hora entrada:");
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(1);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue(obj[4].toString());
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(2);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue("Hora salida:");
                cellNew.setCellStyle(estiloCelda);

                cellNew = hssfRowNew.createCell(3);
                cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
                cellNew.setCellValue(obj[5].toString());
                cellNew.setCellStyle(estiloCelda);

                RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
                RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, 1, 0, 3), sheet, wb);
            }

            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(2);
        }

        HSSFRow hssfRowNew;
        HSSFCell cellNew;

        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        rows = sheet.getLastRowNum();
        String rangoCombustible = "B" + 5 + ":" + "B" + rows;
        String rangoCantidad = "E" + 5 + ":" + "E" + rows;
        hssfRowNew = sheet.createRow(rows + 1);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("TOTAL");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
        cellNew.setCellFormula("SUM(" + rangoCombustible + ")");
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(2);
        cellNew.setCellStyle(cellStyle);

        cellNew = hssfRowNew.createCell(3);
        cellNew.setCellStyle(cellStyle);

        if (maquinaria.equals("Volqueta")) {
            cellNew = hssfRowNew.createCell(4);
            cellNew.setCellType(HSSFCell.CELL_TYPE_FORMULA);
            cellNew.setCellFormula("SUM(" + rangoCantidad + ")");
            cellNew.setCellStyle(cellStyle);

            HSSFCellStyle estiloBordeSuperior = wb.createCellStyle();
            estiloBordeSuperior.setBorderTop(HSSFCellStyle.BORDER_THIN);

            cellNew = hssfRowNew.createCell(5);
            cellNew.setCellStyle(cellStyle);
            cellNew.setCellStyle(estiloBordeSuperior);

            cellNew = hssfRowNew.createCell(6);
            cellNew.setCellStyle(cellStyle);
            cellNew.setCellStyle(estiloBordeSuperior);

            cellNew = hssfRowNew.createCell(7);
            cellNew.setCellStyle(cellStyle);
            cellNew.setCellStyle(estiloBordeSuperior);
        }

        cellStyle = wb.createCellStyle();
        cellStyle.setFont(fuente);
        hssfRowNew = sheet.createRow(rows + 6);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("FIRMA:");
        cellNew.setCellStyle(cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellNew = hssfRowNew.createCell(1);
        cellNew.setCellStyle(cellStyle);
        cellNew = hssfRowNew.createCell(2);
        cellNew.setCellStyle(cellStyle);
    }

    public void postProcessXLSVolqueta(Object document) {

        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        HSSFRow header = sheet.getRow(0);
        wb.setSheetName(0, "Volquetas");

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        //Bordes para la tabla
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 1, 1), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);

        sheet.shiftRows(0, sheet.getLastRowNum(), 2);
        HSSFRow hssfRowNew;
        HSSFCell cellNew;

        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFont(fuente);

        hssfRowNew = sheet.createRow(0);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Lista de Volquetas");
        cellNew.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
    }

    public void postProcessXLSEquipo(Object document) {

        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        HSSFRow header = sheet.getRow(0);
        wb.setSheetName(0, "Equipo caminero");

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        //Bordes para la tabla
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 1, 1), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);

        sheet.shiftRows(0, sheet.getLastRowNum(), 2);
        HSSFRow hssfRowNew;
        HSSFCell cellNew;

        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFont(fuente);

        hssfRowNew = sheet.createRow(0);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Lista de Equipos");
        cellNew.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));

        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 3), sheet, wb);
    }

    public void postProcessXLSChofer(Object document) {

        HSSFWorkbook wb = (HSSFWorkbook) document;
        HSSFSheet sheet = wb.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        HSSFRow header = sheet.getRow(0);
        wb.setSheetName(0, "Choferes");

        HSSFFont fuente = wb.createFont();
        fuente.setBold(true);
        fuente.setFontName("Arial");

        HSSFCellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);

        fuente.setFontHeightInPoints((short) 11);
        cellStyle.setFont(fuente);
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            HSSFCell cell = header.getCell(i);
            cell.setCellStyle(cellStyle);
            sheet.autoSizeColumn(i);
        }

        //Bordes para la tabla
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 0), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 1, 1), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 2, 2), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 3, 3), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 4), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 4), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 4), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 5), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 5), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 6), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 6), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 6), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 7), sheet, wb);
        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(1, rows, 0, 7), sheet, wb);

        sheet.shiftRows(0, sheet.getLastRowNum(), 2);
        HSSFRow hssfRowNew;
        HSSFCell cellNew;

        cellStyle = wb.createCellStyle();
        cellStyle.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        cellStyle.setFont(fuente);

        hssfRowNew = sheet.createRow(0);
        cellNew = hssfRowNew.createCell(0);
        cellNew.setCellType(HSSFCell.CELL_TYPE_STRING);
        cellNew.setCellValue("Lista de Choferes");
        cellNew.setCellStyle(cellStyle);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

        RegionUtil.setBorderBottom(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 7), sheet, wb);
        RegionUtil.setBorderTop(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 7), sheet, wb);
        RegionUtil.setBorderLeft(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 7), sheet, wb);
        RegionUtil.setBorderRight(HSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 0, 0, 7), sheet, wb);
    }

    public void obtenerCargaTransportada() {
        try {
            equipo = null;
            volqueta = null;
            fechaCadena.separarFecha(fechaDesde, fechaHasta);
            fechaCadena.comprobarFechas(fechaDesde, fechaHasta);
            listaFechasRango = fechaTransporteServicio.listarFechasRango(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                    fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
            validarOneRadio(maquinaria);
            if (maquinaria.equals("Volqueta")) {
                listaEquipoFecha = new ArrayList<>();
                listaCargaTransportada = cargaTransportadaServicio.listarCargaTransportada(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                        fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
                buscaVolqueta = 0;
            } else {
                listaEquipoFecha = equiFechaServicio.listarEquipoTransporte(fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                        fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
                listaCargaTransportada = new ArrayList<>();
                buscaEquipo = 0;
            }
        } catch (ReporteException e) {
            baseControlador.addWarningMessage(e.getMessage());
            listaCargaTransportada = new ArrayList<>();
        } catch (NullPointerException np) {
            baseControlador.addWarningMessage("Seleccione un rango de fechas para iniciar la búsqueda");
        }
    }

    public void obtenerCargaVolqueta() {
        if (buscaVolqueta != 0) {
            listaEquipoFecha = new ArrayList<>();
            listaCargaTransportada = cargaTransportadaServicio.listarCargaTransportada(buscaVolqueta, fechaCadena.getDiaInicio(),
                    fechaCadena.getMesInicio(), fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(),
                    fechaCadena.getAnioFin());
            volqueta = usuarioServicio.find(buscaVolqueta);
            equipo = null;
        } else {
            baseControlador.addErrorMessage("Seleccione Volqueta");
        }
    }

    public void obtenerCargaEqui() {
        if (buscaEquipo != 0) {
            listaCargaTransportada = new ArrayList<>();
            listaEquipoFecha = equiFechaServicio.listarEquipoTransporte(buscaEquipo, fechaCadena.getDiaInicio(), fechaCadena.getMesInicio(),
                    fechaCadena.getAnioInicio(), fechaCadena.getDiaFin(), fechaCadena.getMesFin(), fechaCadena.getAnioFin());
            equipo = equipoServicio.find(buscaEquipo);
            volqueta = null;
        } else {
            baseControlador.addErrorMessage("Seleccione Equipo");
        }
    }

    public void obtenerResumenCombustible() {
        listaResumenCombustible = cargaTransportadaServicio.listarResumenCombustible(anioResumen);
        for (Object[] ob : listaResumenCombustible) {
            switch ((int) ob[0]) {
                case 1:
                    ob[0] = Meses.ENERO;
                    break;
                case 2:
                    ob[0] = Meses.FEBRERO;
                    break;
                case 3:
                    ob[0] = Meses.MARZO;
                    ;
                    break;
                case 4:
                    ob[0] = Meses.ABRIL;
                    break;
                case 5:
                    ob[0] = Meses.MAYO;
                    break;
                case 6:
                    ob[0] = Meses.JUNIO;
                    break;
                case 7:
                    ob[0] = Meses.JULIO;
                    break;
                case 8:
                    ob[0] = Meses.AGOSTO;
                    break;
                case 9:
                    ob[0] = Meses.SEPTIEMBRE;
                    break;
                case 10:
                    ob[0] = Meses.OCTUBRE;
                    break;
                case 11:
                    ob[0] = Meses.NOVIEMBRE;
                    break;
                case 12:
                    ob[0] = Meses.DICIEMBRE;
                    break;
                case 13:
                    ob[0] = Meses.TOTAL;
                    break;
                default:
                    break;
            }
        }
    }

    public void obtenerTablaResumenCarga() {
        if (anioResumen != 0) {
            int contador = 1;
            boolean finMes = false;
            Object objeto[];
            listaCarga = cargaTransportadaServicio.listarResumenCarga(anioResumen);
            listaTablaCargaResumen = new ArrayList<>();
            for (Object[] ob : listaCarga) {
                switch ((int) ob[0]) {
                    case 1:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 2) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.ENERO);
                        break;
                    case 2:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 3) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.FEBRERO);
                        break;
                    case 3:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 4) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.MARZO);
                        break;
                    case 4:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 5) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.ABRIL);
                        break;
                    case 5:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 6) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.MAYO);
                        break;
                    case 6:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 7) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.JUNIO);
                        break;
                    case 7:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 8) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.JULIO);
                        break;
                    case 8:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 9) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.AGOSTO);
                        break;
                    case 9:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 10) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.SEPTIEMBRE);
                        break;
                    case 10:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 11) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.OCTUBRE);
                        break;
                    case 11:
                        try {
                            objeto = listaCarga.get(contador);
                            if ((int) objeto[0] == 12) {
                                finMes = true;
                            }
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.NOVIEMBRE);
                        break;
                    case 12:
                        try {
                            listaCarga.get(contador);
                        } catch (Exception e) {
                            finMes = true;
                        }
                        cargaResumen.setMes(Meses.DICIEMBRE);
                        break;
                }
                switch ((String) ob[1]) {
                    case "Arena fina":
                        cargaResumen.setArenaFina((int) ob[2]);
                        totales.setTotalVolquetadasArena((int) ob[3]);
                        break;
                    case "Material para bloque":
                        cargaResumen.setMaterialBloque((int) ob[2]);
                        totales.setTotalVolquetadasBloque((int) ob[3]);
                        break;
                    case "Polvo de piedra":
                        cargaResumen.setPolvoPiedra((int) ob[2]);
                        totales.setTotalVolquetadasPolvo((int) ob[3]);
                        break;
                    case "Relleno":
                        cargaResumen.setRelleno((int) ob[2]);
                        totales.setTotalVolquetadasRelleno((int) ob[3]);
                        break;
                    case "Ripio":
                        cargaResumen.setRipio((int) ob[2]);
                        totales.setTotalVolquetadasRipio((int) ob[3]);
                        break;
                }
                if (finMes) {//contador == 5
                    listaTablaCargaResumen.add(cargaResumen);
//                    contador = 1;
                    cargaResumen = new TablaCarga();
                    finMes = false;
                }
//                else {
//                    contador++;
//                }
                contador++;
            }
            totales.calcularTotalesM3();
            obtenerResumenCombustible();
        } else {
            baseControlador.addErrorMessage("Seleccione año de búsqueda");
            listaTablaCargaResumen = new ArrayList<>();
            listaResumenCombustible = new ArrayList<>();
            totales = new TotalesCarga();
        }
    }

    public void listarFechas() {
        for (FechaTransporte fechaTrans : listaFechasTransporte) {
            listaFechasString.add(fechaCadena.formatearFecha(fechaTrans));
        }
    }

    public void listarAgrupacionVolq() {
        listaAgrupacionVolq = new ArrayList<>();
        AgrupaVolquetas agVolq;
//        List<AgrupaVolquetas> listaAgrupaVolq = new ArrayList<>();
        for (String rangos : listaFechasRango) {
            //System.out.println(rangos);
            agVolq = new AgrupaVolquetas(rangos);
            for (Object[] obj : listaCargaTransportada) {
                if (obj[7].equals(rangos)) {
                    agVolq.getListaObjetos().add(obj);
                }
            }
            listaAgrupacionVolq.add(agVolq);
        }
    }

    public void ingresarVolqueta() {
        try {
            if (idChoferVolq != 0) {
                chofer = choferServicio.find(idChoferVolq);
                volquetaSeleccionada.setFkChoferCodigo(chofer);
                volquetaSeleccionada.setUsuTipo("volqueta");
                usuarioServicio.create(volquetaSeleccionada);
                chofer.setChoferAsignado("SI");
                choferServicio.edit(chofer);
                baseControlador.addSuccessMessage("Ingreso exitoso");
                volquetaSeleccionada = new Usuario();
                listaChoferes = choferServicio.findAll();
                listaVolquetas = usuarioServicio.buscarVolquetas();
                idChoferVolq = 0;
                RequestContext.getCurrentInstance().execute("PF('volqDialogo').hide()");
            } else {
                baseControlador.addErrorMessage("Seleccione chofer");
            }
        } catch (Exception e) {
            baseControlador.addErrorMessage("Ya existe la volqueta");
        }
    }

    public void actualizarVolqueta() {
        String nombreChofer = chofer.getChoferNombre() + " " + chofer.getChoferApellido();
        Chofer choferAux = chofer;
        if (idChoferVolq != 0) {
            chofer = choferServicio.find(idChoferVolq);
            volquetaSeleccionada.setFkChoferCodigo(chofer);
            usuarioServicio.edit(volquetaSeleccionada);
            if (!nombreChofer.equals(chofer.getChoferNombre() + " " + chofer.getChoferApellido())) {
                choferAux.setChoferAsignado("NO");
            }
            choferServicio.edit(choferAux);
            chofer.setChoferAsignado("SI");
            choferServicio.edit(chofer);
            baseControlador.addSuccessMessage("Actualización exitosa");
            volquetaSeleccionada = new Usuario();
            listaChoferes = choferServicio.findAll();
            listaVolquetas = usuarioServicio.buscarVolquetas();
            idChoferVolq = 0;
            RequestContext.getCurrentInstance().execute("PF('volqDialogo').hide()");
        } else {
            baseControlador.addErrorMessage("Seleccione chofer");
        }
    }

    public void eliminarVolq() {
        try {
            chofer = choferServicio.find(volquetaSeleccionada.getFkChoferCodigo().getChoferCodigo());
            chofer.setChoferAsignado("NO");
            choferServicio.edit(chofer);
            usuarioServicio.remove(volquetaSeleccionada);
            volquetaSeleccionada = new Usuario();
            listaChoferes = choferServicio.findAll();
            listaVolquetas = usuarioServicio.buscarVolquetas();
            baseControlador.addSuccessMessage("Volqueta eliminada");
        } catch (Exception e) {
            baseControlador.addWarningMessage("No se puede eliminar la volqueta, está siendo usada");
        } finally {
            RequestContext.getCurrentInstance().execute("PF('volqDialogoElimina').hide()");
        }
    }

    public void actualizarEquipo() {
        String nombreChofer = chofer.getChoferNombre() + " " + chofer.getChoferApellido();
        Chofer choferAux = chofer;
        if (idChoferEq != 0) {
            chofer = choferServicio.find(idChoferEq);
            equipoSeleccionado.setFkChoferCodigo(chofer);
            equipoServicio.edit(equipoSeleccionado);
            if (!nombreChofer.equals(chofer.getChoferNombre() + " " + chofer.getChoferApellido())) {
                choferAux.setChoferAsignado("NO");
            }
            choferServicio.edit(choferAux);
            chofer.setChoferAsignado("SI");
            choferServicio.edit(chofer);
            baseControlador.addSuccessMessage("Actualización exitosa");
            equipoSeleccionado = new Equipo();
            listaChoferes = choferServicio.findAll();
            listaEquipos = equipoServicio.findAll();
            idChoferEq = 0;
            RequestContext.getCurrentInstance().execute("PF('eqDialogo').hide()");
        } else {
            baseControlador.addErrorMessage("Seleccione chofer");
        }
    }

    public void eliminarEq() {
        try {
            chofer = choferServicio.find(equipoSeleccionado.getFkChoferCodigo().getChoferCodigo());
            chofer.setChoferAsignado("NO");
            choferServicio.edit(chofer);
            equipoServicio.remove(equipoSeleccionado);
            equipoSeleccionado = new Equipo();
            listaEquipos = equipoServicio.findAll();
            listaChoferes = choferServicio.findAll();
            baseControlador.addSuccessMessage("Equipo caminero eliminado");
        } catch (Exception e) {
            baseControlador.addWarningMessage("No se puede eliminar el equipo, está siendo usado");
        } finally {
            RequestContext.getCurrentInstance().execute("PF('equiDialogoElimina').hide()");
        }
    }

    public void ingresarEquipo() {
        if (idChoferEq != 0) {
            chofer = choferServicio.find(idChoferEq);
            equipoSeleccionado.setFkChoferCodigo(chofer);
            equipoSeleccionado.setEqTipoUs("equipo");
            equipoServicio.create(equipoSeleccionado);
            chofer.setChoferAsignado("SI");
            choferServicio.edit(chofer);
            baseControlador.addSuccessMessage("Ingreso exitoso");
            listaChoferes = choferServicio.findAll();
            listaEquipos = equipoServicio.findAll();
            equipoSeleccionado = new Equipo();
            idChoferEq = 0;
            RequestContext.getCurrentInstance().execute("PF('eqDialogo').hide()");
        } else {
            baseControlador.addErrorMessage("Seleccione chofer");
        }
    }

    public void ingresarChofer() {
        chofer.setChoferAsignado("NO");
        choferServicio.create(chofer);
        baseControlador.addSuccessMessage("Ingreso exitoso");
        listaChoferes = choferServicio.findAll();
        chofer = new Chofer();
        RequestContext.getCurrentInstance().execute("PF('choferDialogo').hide()");
    }

    public void actualizarChofer() {
        if (chofer.getChoferAsignado().equals("NO")) {
            BusquedaEquipos buscaEquipos = new BusquedaEquipos();
            Equipo equi = buscaEquipos.buscarEquipoChofer(listaEquipos, chofer.getChoferCodigo());
            Usuario u = buscaEquipos.buscarVolquetaChofer(listaVolquetas, chofer.getChoferCodigo());
            if (equi != null) {
                equi.getFkChoferCodigo().setChoferCodigo(0);
                equipoServicio.edit(equi);
                listaEquipos = equipoServicio.findAll();
            }
            if (u != null) {
                u.getFkChoferCodigo().setChoferCodigo(0);
                usuarioServicio.edit(u);
                listaVolquetas = usuarioServicio.buscarVolquetas();
            }
        }
        choferServicio.edit(chofer);
        baseControlador.addSuccessMessage("Actualización exitosa");
        chofer = new Chofer();
        listaChoferes = choferServicio.findAll();
        RequestContext.getCurrentInstance().execute("PF('choferDialogo').hide()");
    }

    public void eliminarChofer() {
        try {
            choferServicio.remove(chofer);
            chofer = new Chofer();
            listaChoferes = choferServicio.findAll();
            baseControlador.addSuccessMessage("Chofer eliminado");
        } catch (Exception e) {
            baseControlador.addWarningMessage("No se puede eliminar el chofer, está asignado");
        } finally {
            RequestContext.getCurrentInstance().execute("PF('choferDialogoElimina').hide()");
        }
    }

    public void seleccionarEquipo() {
        idChoferEq = equipoSeleccionado.getFkChoferCodigo().getChoferCodigo();
        chofer = choferServicio.find(idChoferEq);
        listaChoferesNoAsignados = choferServicio.buscarChoferNoAsignado();
        listaChoferesNoAsignados.add(chofer);
        textoIngreso = true;
        ingreso = true;
        actualiza = false;
    }

    public void seleccionarVolq() {
        idChoferVolq = volquetaSeleccionada.getFkChoferCodigo().getChoferCodigo();
        chofer = choferServicio.find(idChoferVolq);
        listaChoferesNoAsignados = choferServicio.buscarChoferNoAsignado();
        listaChoferesNoAsignados.add(chofer);
        textoIngreso = true;
        ingreso = true;
        actualiza = false;
    }

    public void seleccionarChofer() {
        habilitaChoferAsig = false;
        textoIngreso = true;
        ingreso = true;
        actualiza = false;
    }

    public void ingresarNuevo() {
        listaChoferesNoAsignados = choferServicio.buscarChoferNoAsignado();
        idChoferEq = 0;
        idChoferVolq = 0;
        chofer = new Chofer();
        volquetaSeleccionada = new Usuario();
        equipoSeleccionado = new Equipo();
        textoIngreso = false;
        ingreso = false;
        actualiza = true;
        habilitaChoferAsig = true;
    }

    public boolean filtrarMayores(Object value, Object filter, Locale local) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if (filterText == null || filterText.equals("")) {
            return true;
        }

        if (value == null) {
            return false;
        }

        return ((Comparable) value).compareTo(Integer.valueOf(filterText)) >= 0;
    }
    //</editor-fold>

    //<editor-fold desc="Get and Set" defaultstate="collapsed">
    public List<Object[]> getListaCargaTransportada() {
        return listaCargaTransportada;
    }

    public void setListaCargaTransportada(List<Object[]> listaCargaTransportada) {
        this.listaCargaTransportada = listaCargaTransportada;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public CargaTransportadaServicio getCargaTransportadaServicio() {
        return cargaTransportadaServicio;
    }

    public void setCargaTransportadaServicio(CargaTransportadaServicio cargaTransportadaServicio) {
        this.cargaTransportadaServicio = cargaTransportadaServicio;
    }

    public BaseControlador getBaseControlador() {
        return baseControlador;
    }

    public void setBaseControlador(BaseControlador baseControlador) {
        this.baseControlador = baseControlador;
    }

    public NavegacionControlador getNavegacionControlador() {
        return navegacionControlador;
    }

    public void setNavegacionControlador(NavegacionControlador navegacionControlador) {
        this.navegacionControlador = navegacionControlador;
    }

    public List<FechaTransporte> getListaFechasInicio() {
        return listaFechasTransporte;
    }

    public void setListaFechasInicio(List<FechaTransporte> listaFechasInicio) {
        this.listaFechasTransporte = listaFechasInicio;
    }

    public String getMaquinaria() {
        return maquinaria;
    }

    public void setMaquinaria(String maquinaria) {
        this.maquinaria = maquinaria;
    }

    public List<String> getListaFechasString() {
        return listaFechasString;
    }

    public void setListaFechasString(LinkedList<String> listaFechasString) {
        this.listaFechasString = listaFechasString;
    }

    public List<Material> getListaMateriales() {
        return listaMateriales;
    }

    public void setListaMateriales(List<Material> listaMateriales) {
        this.listaMateriales = listaMateriales;
    }

    public List<Object[]> getListaEquipoFecha() {
        return listaEquipoFecha;
    }

    public void setListaEquipoFecha(List<Object[]> listaEquipoFecha) {
        this.listaEquipoFecha = listaEquipoFecha;
    }

    public List<String> getListaFechasRango() {
        return listaFechasRango;
    }

    public void setListaFechasRango(List<String> listaFechasRango) {
        this.listaFechasRango = listaFechasRango;
    }

    public List<AgrupaVolquetas> getListaAgrupacionVolq() {
        return listaAgrupacionVolq;
    }

    public void setListaAgrupacionVolq(List<AgrupaVolquetas> listaAgrupacionVolq) {
        this.listaAgrupacionVolq = listaAgrupacionVolq;
    }

    public String getHoraActual() throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        horaActual = formatter.format(java.util.Calendar.getInstance().getTime());
//        return horaActual;
        Calendar cal = Calendar.getInstance();
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int mes = cal.get(Calendar.MONTH) + 1;
        int anio = cal.get(Calendar.YEAR);
        int hora = cal.get(Calendar.HOUR_OF_DAY);
        int minuto = cal.get(Calendar.MINUTE);
        horaActual = dia + "-" + mes + "-" + anio + "_" + hora + "-" + minuto;
        return horaActual;
    }

    public void setHoraActual(String horaActual) {
        this.horaActual = horaActual;
    }

    public List<Object[]> getListaCarga() {
        return listaCarga;
    }

    public void setListaCarga(List<Object[]> listaCarga) {
        this.listaCarga = listaCarga;
    }

    public List<FechaTransporte> getListaFechasTransporte() {
        return listaFechasTransporte;
    }

    public void setListaFechasTransporte(List<FechaTransporte> listaFechasTransporte) {
        this.listaFechasTransporte = listaFechasTransporte;
    }

    public List<TablaCarga> getListaTablaCargaResumen() {
        return listaTablaCargaResumen;
    }

    public void setListaTablaCargaResumen(List<TablaCarga> listaTablaCargaResumen) {
        this.listaTablaCargaResumen = listaTablaCargaResumen;
    }

    public int getAnioResumen() {
        return anioResumen;
    }

    public void setAnioResumen(int anioResumen) {
        this.anioResumen = anioResumen;
    }

    public List<Integer> getListaAnios() {
        return listaAnios;
    }

    public void setListaAnios(List<Integer> listaAnios) {
        this.listaAnios = listaAnios;
    }

    public TablaCarga getCargaResumen() {
        return cargaResumen;
    }

    public void setCargaResumen(TablaCarga cargaResumen) {
        this.cargaResumen = cargaResumen;
    }

    public TotalesCarga getTotales() {
        return totales;
    }

    public void setTotales(TotalesCarga totales) {
        this.totales = totales;
    }

    public List<Object[]> getListaResumenCombustible() {
        return listaResumenCombustible;
    }

    public void setListaResumenCombustible(List<Object[]> listaResumenCombustible) {
        this.listaResumenCombustible = listaResumenCombustible;
    }

    public List<Usuario> getListaVolquetas() {
        return listaVolquetas;
    }

    public void setListaVolquetas(List<Usuario> listaVolquetas) {
        this.listaVolquetas = listaVolquetas;
    }

    public List<Equipo> getListaEquipos() {
        return listaEquipos;
    }

    public void setListaEquipos(List<Equipo> listaEquipos) {
        this.listaEquipos = listaEquipos;
    }

    public Usuario getVolquetaSeleccionada() {
        return volquetaSeleccionada;
    }

    public void setVolquetaSeleccionada(Usuario volquetaSeleccionada) {
        this.volquetaSeleccionada = volquetaSeleccionada;
    }

    public Equipo getEquipoSeleccionado() {
        return equipoSeleccionado;
    }

    public void setEquipoSeleccionado(Equipo equipoSeleccionado) {
        this.equipoSeleccionado = equipoSeleccionado;
    }

    public List<Chofer> getListaChoferes() {
        return listaChoferes;
    }

    public void setListaChoferes(List<Chofer> listaChoferes) {
        this.listaChoferes = listaChoferes;
    }

    public boolean isIngreso() {
        return ingreso;
    }

    public void setIngreso(boolean ingreso) {
        this.ingreso = ingreso;
    }

    public boolean isActualiza() {
        return actualiza;
    }

    public void setActualiza(boolean actualiza) {
        this.actualiza = actualiza;
    }

    public int getIdChoferVolq() {
        return idChoferVolq;
    }

    public void setIdChoferVolq(int idChoferVolq) {
        this.idChoferVolq = idChoferVolq;
    }

    public int getIdChoferEq() {
        return idChoferEq;
    }

    public void setIdChoferEq(int idChoferEq) {
        this.idChoferEq = idChoferEq;
    }

    public Chofer getChofer() {
        return chofer;
    }

    public void setChofer(Chofer chofer) {
        this.chofer = chofer;
    }

    public Chofer getChoferSeleccionado() {
        return choferSeleccionado;
    }

    public void setChoferSeleccionado(Chofer choferSeleccionado) {
        this.choferSeleccionado = choferSeleccionado;
    }

    public boolean isTextoIngreso() {
        return textoIngreso;
    }

    public void setTextoIngreso(boolean textoIngreso) {
        this.textoIngreso = textoIngreso;
    }

    public List<Chofer> getListaChoferesNoAsignados() {
        return listaChoferesNoAsignados;
    }

    public void setListaChoferesNoAsignados(List<Chofer> listaChoferesNoAsignados) {
        this.listaChoferesNoAsignados = listaChoferesNoAsignados;
    }

    public boolean isHabilitaChoferAsig() {
        return habilitaChoferAsig;
    }

    public void setHabilitaChoferAsig(boolean habilitaChoferAsig) {
        this.habilitaChoferAsig = habilitaChoferAsig;
    }

    public int getBuscaVolqueta() {
        return buscaVolqueta;
    }

    public void setBuscaVolqueta(int buscaVolqueta) {
        this.buscaVolqueta = buscaVolqueta;
    }

    public int getBuscaEquipo() {
        return buscaEquipo;
    }

    public void setBuscaEquipo(int buscaEquipo) {
        this.buscaEquipo = buscaEquipo;
    }
    //</editor-fold>

}
