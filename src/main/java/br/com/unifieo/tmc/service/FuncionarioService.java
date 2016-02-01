package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.*;
import br.com.unifieo.tmc.repository.*;
import br.com.unifieo.tmc.security.AuthoritiesConstants;
import br.com.unifieo.tmc.web.rest.dto.FuncionarioDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final CepRepository cepRepository;
    private final CondominioRepository condominioRepository;
    private final CondominioService condominioService;
    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final TelefoneFuncionarioRepository telefoneFuncionarioRepository;

    @Inject
    public FuncionarioService(FuncionarioRepository funcionarioRepository, CepRepository cepRepository,
                              CondominioRepository condominioRepository, CondominioService condominioService, AuthorityRepository authorityRepository,
                              UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder,
                              MailService mailService, TelefoneFuncionarioRepository telefoneFuncionarioRepository) {
        this.funcionarioRepository = funcionarioRepository;
        this.cepRepository = cepRepository;
        this.condominioRepository = condominioRepository;
        this.condominioService = condominioService;
        this.authorityRepository = authorityRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.telefoneFuncionarioRepository = telefoneFuncionarioRepository;
    }

    public Funcionario save(FuncionarioDTO funcionarioDTO, String baseUrl) {

        Funcionario funcionario = new Funcionario(funcionarioDTO);
        funcionario.setAtivo(true);
        funcionario.setResponsavel(false);

        Cep cep = Optional
            .ofNullable(cepRepository.findOneByCep(funcionario.getCep().getCep()))
            .orElseGet(() -> cepRepository.save(funcionario.getCep()));

        cep.setLogradouro(funcionario.getCep().getLogradouro());
        cep.setBairro(funcionario.getCep().getBairro());
        cep.setCidade(funcionario.getCep().getCidade());
        cep.setCep(funcionario.getCep().getCep());
        cep.setUf(funcionario.getCep().getUf());

        cep = cepRepository.save(cep);

        funcionario.setCep(cep);

        Funcionario funcionarioAdmin = funcionarioRepository.findOneByEmail(userService.getUserDTO().getEmail());
        Condominio condominio = funcionarioAdmin.getCondominio();

        funcionario.setCondominio(condominio);

        telefoneFuncionarioRepository.save(funcionario.getTelefoneFuncionarios());

        Funcionario funcionarioSaved;
        if (funcionario.getId() == null) {

            User newUser = new User();

            Authority authority = authorityRepository.findOne(AuthoritiesConstants.FUNCIONARIO);
            HashSet<Authority> authorities = new HashSet<>();
            authorities.add(authority);
            newUser.setAuthorities(authorities);

            String encryptedPassword = passwordEncoder.encode("$SYSTEM");

            newUser.setPassword(encryptedPassword);

            funcionario.setSenha(encryptedPassword);

            newUser.setFirstName(funcionario.getNome());
            newUser.setEmail(funcionario.getEmail());
            newUser.setLangKey("pt-br");
            newUser.setActivated(false);

            User userSaved = userRepository.save(newUser);
            userService.requestPasswordReset(userSaved.getEmail());

            funcionarioSaved = funcionarioRepository.save(funcionario);

            mailService.sendNewFuncionarioEmail(funcionarioSaved, userSaved, baseUrl);

        } else {
            funcionarioSaved = funcionarioRepository.save(funcionario);
        }
        return funcionarioSaved;
    }

    public List<Funcionario> findAllByCondominioAtual() {
        Condominio condominio = condominioService.getCurrentCondominio();
        return funcionarioRepository.findAllByCondominio(condominio).stream().filter(f -> f.getAtivo()).collect(Collectors.toList());
    }
}
