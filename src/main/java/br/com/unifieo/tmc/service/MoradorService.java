package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.repository.MoradorRepository;
import br.com.unifieo.tmc.web.rest.dto.UserDTO;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

@Service
@Transactional
public class MoradorService {

    private final Logger log = LoggerFactory.getLogger(MoradorService.class);

    private final MoradorRepository moradorRepository;

    private final UserService userService;

    private final CondominioService condominioService;

    @Inject
    public MoradorService(MoradorRepository moradorRepository, UserService userService, CondominioService condominioService) {
        this.moradorRepository = moradorRepository;
        this.userService = userService;
        this.condominioService = condominioService;
    }

    public Morador save(Morador morador) {
        morador.setAtivo(true);
        return this.moradorRepository.save(morador);
    }

    private String getPdfUrl(String baseUrl, final String reportPathName) {
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream inputStream = loader.getResourceAsStream(reportPathName);
            if (inputStream == null)
                throw new RuntimeException("Arquivo do relatório não encontrado");

            File pdf = File.createTempFile(UUID.randomUUID().toString(), ".pdf");

            UserDTO userDTO = userService.getUserDTO();
            Condominio condominio = condominioService.findOneByRazaoSocial(userDTO.getCondominio());

            HashMap<String, Object> parameters = new HashMap<>(10);
            parameters.put("URL_REGISTER", baseUrl + "/#/moradors/new?condominio=" + condominio.getId());
            parameters.put("CONDOMINIO", "Moradores " + condominio.getRazaoSocial());
            parameters.put("TMC_LOGO", loader.getResource("images/logo_tmc.png").getPath());

            Connection connection = null;

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

    public String getUrlRegisterCode(String baseUrl) {
        return this.getPdfUrl(baseUrl, "reports/reportMoradorRegisterQRCode.jasper");
    }
}
