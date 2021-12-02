package cat.tecnocampus.rooms.configuration.Security;

import cat.tecnocampus.rooms.configuration.Security.jwt.JwtConfig;
import cat.tecnocampus.rooms.configuration.Security.jwt.JwtTokenVerifierFilter;
import cat.tecnocampus.rooms.configuration.Security.jwt.JwtUsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
	
	PasswordEncoder passwordEncoder;
	DataSource dataSource;
	JwtConfig jwtConfig;

	private static final String USERS_QUERY = "SELECT name, password, enabled FROM student WHERE name = ?";
	private static final String AUTHORITIES_QUERY = "SELECT username, role FROM authorities WHERE username = ?";
	
	public ApplicationSecurityConfig (PasswordEncoder passwordEncoder, DataSource dataSource,
			JwtConfig jwtConfig) {
        
		this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.jwtConfig = jwtConfig;
		
	}

	/*
	TODO 8: you need to update the configuration so that:
		* the files inside /js and files /*.html and h2-console can be accessed by all users, even unregistered ones. This is ALREADY DONE
		Admins can access
			* /classrooms
			* /classrooms/allocations can only be accessed by admins. Also the one to create a new allocation
	 		* /students
	 		* /students/id
	 		* /classrooms/capacity
	 		* /students/house (see TO-DO 6)
	 		* /students/{name}/house (see TO-DO 7)
		 Students can access
	 		* /students/me
			* /students/house (see TO-DO 6)
			* /classrooms
	 	For the rest of the request the user must be authenticated
	 */

    @Override
	protected void configure (HttpSecurity http) throws Exception {
    	http
        .csrf().disable()
		.cors().and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and().headers().frameOptions().sameOrigin()
        .and()
        .addFilter(new JwtUsernamePasswordAuthenticationFilter(authenticationManager(), jwtConfig))
        .addFilterAfter(new JwtTokenVerifierFilter(jwtConfig),JwtUsernamePasswordAuthenticationFilter.class)
		.authorizeRequests()
				.antMatchers("/**").permitAll()  //TODO delete this one????
		.antMatchers("/js/*", "/*.html").permitAll()
		.antMatchers("/h2-console/**").permitAll()
		.antMatchers("/classrooms", "/students/house").hasAnyRole("STUDENT", "ADMIN")
		.antMatchers("/students/me/**").hasRole("STUDENT")
		.antMatchers("/classrooms/allocations/**", "/students", "/students/{id}").hasRole("ADMIN")
		.antMatchers("/classrooms/capacity", "/students/*/house").hasRole("ADMIN")
		
		.anyRequest().authenticated();
	}
    
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(USERS_QUERY)
                .authoritiesByUsernameQuery(AUTHORITIES_QUERY)
                .passwordEncoder(passwordEncoder);
    }
}
