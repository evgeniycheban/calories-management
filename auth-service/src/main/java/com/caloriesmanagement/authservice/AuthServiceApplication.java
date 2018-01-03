package com.caloriesmanagement.authservice;

import com.caloriesmanagement.authservice.model.Role;
import com.caloriesmanagement.authservice.model.User;
import com.caloriesmanagement.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.util.EnumSet;
import java.util.stream.Stream;

/**
 * Microservice configuration class.
 *
 * @author Evgeniy Cheban
 */
@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	@Autowired
	public CommandLineRunner runner(UserRepository repository) {
		return args -> {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

			User user = new User(null, "user", encoder.encode("user"), true, EnumSet.of(Role.ROLE_USER));
			User admin = new User(null, "admin", encoder.encode("admin"), true, EnumSet.of(Role.ROLE_ADMIN));

			Stream.of(user, admin).forEach(repository::save);
		};
	}

	/**
	 * Web security configuration
	 */
	@Configuration
	@EnableWebSecurity
	protected static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

		private final UserDetailsService userDetailsService;

		/**
		 * @param userDetailsService - user details service implementation.
		 */
		@Autowired
		public WebSecurityConfig(UserDetailsService userDetailsService) {
			this.userDetailsService = userDetailsService;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off
			http
					.authorizeRequests().anyRequest().authenticated()
			.and()
					.csrf().disable();
			// @formatter:on
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
			auth.userDetailsService(userDetailsService)
					.passwordEncoder(new BCryptPasswordEncoder());
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManager();
		}
	}

	/**
	 * OAuth 2 Configuration
	 */
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2Config extends AuthorizationServerConfigurerAdapter {

		private final AuthenticationManager authenticationManager;
		private final UserDetailsService userDetailsService;
		private final Environment env;

		/**
		 * @param authenticationManager - spring security auth manager.
		 * @param userDetailsService - user details service implementation.
		 * @param env - accessing environment variables.
		 */
		@Autowired
		public OAuth2Config(@Qualifier("authenticationManagerBean")
									AuthenticationManager authenticationManager,
							UserDetailsService userDetailsService,
							Environment env) {
			this.authenticationManager = authenticationManager;
			this.userDetailsService = userDetailsService;
			this.env = env;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			// TODO: use jdbc instead of inMemory.
			// TODO: store secrets as env variables.

			// @formatter:off
			clients.inMemory()
							.withClient("browser")
							.secret("browser")
							.authorizedGrantTypes("refresh_token", "password")
							.scopes("ui")
			.and()
							.withClient("account-service")
							.secret(env.getProperty("ACCOUNT_SERVICE_PASSWORD"))
							.authorizedGrantTypes("client_credentials", "refresh_token")
							.scopes("server");
			// @formatter:on
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints
					.tokenStore(new InMemoryTokenStore())
					.authenticationManager(authenticationManager)
					.userDetailsService(userDetailsService);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
			oauthServer
					.tokenKeyAccess("permitAll()")
					.checkTokenAccess("isAuthenticated()");
		}
	}

}
