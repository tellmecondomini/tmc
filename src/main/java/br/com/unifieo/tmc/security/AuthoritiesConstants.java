package br.com.unifieo.tmc.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String ADMIN_CONDOMINIO = "ROLE_ADMIN_CONDOMINIO";

    public static final String FUNCIONARIO = "ROLE_FUNCIONARIO";

    public static final String MORADOR = "ROLE_MORADOR";

    private AuthoritiesConstants() {
    }
}
