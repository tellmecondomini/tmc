package br.com.unifieo.tmc.web.rest;

import br.com.unifieo.tmc.Application;
import br.com.unifieo.tmc.domain.Authority;
import br.com.unifieo.tmc.domain.User;
import br.com.unifieo.tmc.repository.AuthorityRepository;
import br.com.unifieo.tmc.repository.UserRepository;
import br.com.unifieo.tmc.security.AuthoritiesConstants;
import br.com.unifieo.tmc.service.MailService;
import br.com.unifieo.tmc.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class AccountResourceTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private AuthorityRepository authorityRepository;

    @Inject
    private UserService userService;

    @Mock
    private UserService mockUserService;

    @Mock
    private MailService mockMailService;

    private MockMvc restUserMockMvc;

    private MockMvc restMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AccountResource accountResource = new AccountResource();
        ReflectionTestUtils.setField(accountResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountResource, "userService", userService);
        ReflectionTestUtils.setField(accountResource, "mailService", mockMailService);

        AccountResource accountUserMockResource = new AccountResource();
        ReflectionTestUtils.setField(accountUserMockResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(accountUserMockResource, "userService", mockUserService);
        ReflectionTestUtils.setField(accountUserMockResource, "mailService", mockMailService);

        this.restMvc = MockMvcBuilders.standaloneSetup(accountResource).build();
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(accountUserMockResource).build();
    }

    @Test
    public void testNonAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/authenticate")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }

    @Test
    public void testAuthenticatedUser() throws Exception {
        restUserMockMvc.perform(get("/api/authenticate")
            .with(request -> {
                request.setRemoteUser("test");
                return request;
            })
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().string("test"));
    }

    @Test
    public void testGetExistingAccount() throws Exception {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        User user = new User();
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("john.doe@jhipter.com");
        user.setAuthorities(authorities);
        when(mockUserService.getUserWithAuthorities()).thenReturn(user);

        restUserMockMvc.perform(get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.login").value("test"))
            .andExpect(jsonPath("$.firstName").value("john"))
            .andExpect(jsonPath("$.lastName").value("doe"))
            .andExpect(jsonPath("$.email").value("john.doe@jhipter.com"))
            .andExpect(jsonPath("$.authorities").value(AuthoritiesConstants.ADMIN));
    }

    @Test
    public void testGetUnknownAccount() throws Exception {
        when(mockUserService.getUserWithAuthorities()).thenReturn(null);

        restUserMockMvc.perform(get("/api/account")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError());
    }

}
