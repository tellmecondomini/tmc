package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.Authority;
import br.com.unifieo.tmc.domain.Condominio;
import br.com.unifieo.tmc.domain.Morador;
import br.com.unifieo.tmc.domain.User;
import br.com.unifieo.tmc.repository.AuthorityRepository;
import br.com.unifieo.tmc.repository.CondominioRepository;
import br.com.unifieo.tmc.repository.MoradorRepository;
import br.com.unifieo.tmc.repository.UserRepository;
import br.com.unifieo.tmc.security.AuthoritiesConstants;
import br.com.unifieo.tmc.web.rest.dto.UserDTO;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.HashSet;
import java.util.UUID;

@Service
@Transactional
public class MoradorService {

    private final Logger log = LoggerFactory.getLogger(MoradorService.class);

    private final MoradorRepository moradorRepository;

    private final UserService userService;

    private final CondominioService condominioService;

    private final CondominioRepository condominioRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    @Inject
    public MoradorService(MoradorRepository moradorRepository, UserService userService,
                          CondominioService condominioService, CondominioRepository condominioRepository,
                          PasswordEncoder passwordEncoder, MailService mailService, UserRepository userRepository,
                          AuthorityRepository authorityRepository) {
        this.moradorRepository = moradorRepository;
        this.userService = userService;
        this.condominioService = condominioService;
        this.condominioRepository = condominioRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
    }

    public Morador save(Morador morador, Long idCondominio, String baseUrl) throws Exception {

        Condominio condominio = condominioRepository.findOne(idCondominio);
        if (condominio == null)
            throw new Exception("Condomínio não encontrado.");

        morador.setAtivo(false);
        morador.setCondominio(condominio);

        User newUser = new User();

        Authority authority = authorityRepository.findOne(AuthoritiesConstants.MORADOR);
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authority);
        newUser.setAuthorities(authorities);

        String encryptedPassword = passwordEncoder.encode("$SYSTEM");

        newUser.setPassword(encryptedPassword);

        morador.setSenha(encryptedPassword);

        newUser.setFirstName(morador.getNome());
        newUser.setEmail(morador.getEmail());
        newUser.setLangKey("pt-br");
        newUser.setActivated(true);

        User userSaved = userRepository.save(newUser);
        userService.requestPasswordReset(userSaved.getEmail());

        Morador moradorSaved = moradorRepository.save(morador);

        mailService.sendNewMoradorEmail(moradorSaved, userSaved, baseUrl);

        return moradorSaved;
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
            parameters.put("URL_REGISTER", baseUrl + "/#/register/morador/" + condominio.getId());
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
