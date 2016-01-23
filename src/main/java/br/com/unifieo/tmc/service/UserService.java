package br.com.unifieo.tmc.service;

import br.com.unifieo.tmc.domain.*;
import br.com.unifieo.tmc.repository.*;
import br.com.unifieo.tmc.security.AuthoritiesConstants;
import br.com.unifieo.tmc.security.SecurityUtils;
import br.com.unifieo.tmc.service.util.RandomUtil;
import br.com.unifieo.tmc.web.rest.dto.UserDTO;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CondominioRepository condominioRepository;

    @Inject
    private FuncionarioRepository funcionarioRepository;

    @Inject
    private MoradorRepository moradorRepository;

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
//            .filter(user -> {
//                DateTime oneDayAgo = DateTime.now().minusHours(24);
//                return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
//            })
            .map(user -> {
                String password = passwordEncoder.encode(newPassword);

                user.setPassword(password);
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);

                this.doChagePasswordForFuncionarioAndMorador(password, user.getEmail());

                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(user -> user.getActivated() == true)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(DateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserAndFuncionarioAndPreCondominio(UserDTO userDTO) {

        User newUser = new User();

        Authority autAdminCondominio = authorityRepository.findOne(AuthoritiesConstants.ADMIN_CONDOMINIO);

        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(autAdminCondominio);

        newUser.setAuthorities(authorities);

        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(encryptedPassword);

        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setLangKey(userDTO.getLangKey());
        newUser.setActivated(false);
        newUser.setActivationKey(RandomUtil.generateActivationKey());

        User userSaved = userRepository.save(newUser);

        Condominio condominio = new Condominio(userDTO.getCondominio());
        condominio = condominioRepository.save(condominio);

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(userSaved.getFirstName());
        funcionario.setEmail(userSaved.getEmail());
        funcionario.setSenha(userSaved.getPassword());
        funcionario.setAtivo(true);
        funcionario.setCondominio(condominio);
        funcionario.setResponsavel(true);
        funcionarioRepository.save(funcionario);

        log.debug("Created Information for User: {}", newUser);

        return newUser;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        userRepository.findOneByEmail(SecurityUtils.getCurrentLogin()).ifPresent(u -> {
            u.setFirstName(firstName);
            u.setLastName(lastName);
            u.setEmail(email);
            u.setLangKey(langKey);

            User userSaved = userRepository.save(u);

            Funcionario funcionario = funcionarioRepository.findOneByEmail(userSaved.getEmail());
            if (funcionario == null) {
                Morador morador = moradorRepository.findOneByEmail(userSaved.getEmail());
                if (morador != null) {
                    morador.setNome(firstName + " " + lastName);
                    morador.setEmail(email);
                    moradorRepository.save(morador);
                }
            } else {
                funcionario.setNome(firstName + " " + lastName);
                funcionario.setEmail(email);
                funcionarioRepository.save(funcionario);
            }

            log.debug("Changed Information for User: {}", u);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByEmail(SecurityUtils.getCurrentLogin()).ifPresent(u -> {

            String encryptedPassword = passwordEncoder.encode(password);
            u.setPassword(encryptedPassword);

            User userSaved = userRepository.save(u);

            this.doChagePasswordForFuncionarioAndMorador(encryptedPassword, userSaved.getEmail());

            log.debug("Changed password for User: {}", u);
        });
    }

    private void doChagePasswordForFuncionarioAndMorador(String encryptedPassword, String email) {
        Funcionario funcionario = funcionarioRepository.findOneByEmail(email);
        if (funcionario == null) {
            Morador morador = moradorRepository.findOneByEmail(email);
            if (morador != null) {
                morador.setSenha(encryptedPassword);
                morador.setAtivo(true);
                moradorRepository.save(morador);
            }
        } else {
            funcionario.setSenha(encryptedPassword);
            funcionario.setAtivo(true);
            funcionarioRepository.save(funcionario);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneByEmail(login).map(u -> {
            u.getAuthorities().size();
            return u;
        });
    }


    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        User user = userRepository.findOne(id);
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        User user = userRepository.findOneByEmail(SecurityUtils.getCurrentLogin()).get();
        user.getAuthorities().size(); // eagerly load the association
        return user;
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     * </p>
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = new LocalDate();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).stream().forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getFirstName());
            userRepository.delete(user);
        }
    }

    /**
     * Obtem o usuário corrente.
     *
     * @return
     */
    public UserDTO getUserDTO() {

        UserDTO user = new UserDTO(this.getUserWithAuthorities());

        Funcionario funcionario = funcionarioRepository.findOneByEmail(user.getEmail());
        if (funcionario == null) {
            Morador morador = moradorRepository.findOneByEmail(user.getEmail());
            if (morador != null)
                user.setCondominio(morador.getCondominio().getRazaoSocial());
        } else {
            user.setCondominio(funcionario.getCondominio().getRazaoSocial());
        }

        return user;
    }

    public Morador getMoradorAtual() {
        User user = this.getUserWithAuthorities();
        return moradorRepository.findOneByEmail(user.getEmail());
    }
}
