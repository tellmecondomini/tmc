package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.service.CondominioService;
import br.com.unifieo.tmc.web.rest.dto.ReportDTO;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ReportResource {

    private final Logger log = LoggerFactory.getLogger(ReportResource.class);

    @Inject
    private CondominioService condominioService;

    @Inject
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/reports/topicos/categorias",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<String> topicosCategorias(@RequestBody ReportDTO reportDTO) {
        HashMap<String, Object> parameters = this.newMapParameter();
        parameters.put("DATA_INICIO", reportDTO.getDataInicio().toDate());
        parameters.put("DATA_FIM", reportDTO.getDataFim().toDate());
        JsonObject json = new JsonObject();
        json.addProperty("url", this.getPdfUrl("reports/reportTopicosCategoria.jasper", parameters));
        return new ResponseEntity<>(new Gson().toJson(json), HttpStatus.OK);
    }

    private final String getPdfUrl(final String reportPathName, final Map<String, Object> parameters) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(reportPathName);
            if (inputStream == null)
                throw new RuntimeException("Arquivo do relatório não encontrado");
            File pdf = File.createTempFile(UUID.randomUUID().toString(), ".pdf");
            Connection connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jasperPrint, pdf.getAbsolutePath());
            java.nio.file.Path path = Paths.get(pdf.getAbsolutePath());
            byte[] data = Files.readAllBytes(path);
            String fileBase64 = Base64.getEncoder().encodeToString(data);
            return "data:application/pdf;base64,".concat(fileBase64);
        } catch (Exception exception) {
            log.info(exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    private final HashMap<String, Object> newMapParameter() {
        HashMap<String, Object> map = new HashMap<>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        map.put("TMC_LOGO", loader.getResource("images/logo_tmc.png").getPath());
        map.put("CONDOMINIO", "Condomínio " + condominioService.getCurrentCondominio().getRazaoSocial());
        return map;
    }

}
