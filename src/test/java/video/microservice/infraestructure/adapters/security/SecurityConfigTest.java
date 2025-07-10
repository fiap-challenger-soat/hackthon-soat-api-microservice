package video.microservice.infraestructure.adapters.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecurityConfigTest {
    private SecurityConfig securityConfig;
    private HttpSecurity httpSecurity;
    @BeforeEach
    public void setup(){
        securityConfig = new SecurityConfig();
        httpSecurity = mock(HttpSecurity.class);
    }

    @Test
    @DisplayName("Deve construir SecurityFilterChain sem lançar exceção")
    void shouldBuildSecurityFilterChain() throws Exception {
       when(httpSecurity.sessionManagement(any())).thenReturn(httpSecurity);
        when(httpSecurity.csrf(any())).thenReturn(httpSecurity);
        when(httpSecurity.authorizeHttpRequests(any())).thenReturn(httpSecurity);
        when(httpSecurity.oauth2ResourceServer(any())).thenReturn(httpSecurity);
        when(httpSecurity.httpBasic(any())).thenReturn(httpSecurity);
        when(httpSecurity.build()).thenReturn(mock(DefaultSecurityFilterChain.class));


        SecurityFilterChain result = securityConfig.securityFilterChain(httpSecurity);
        verify(httpSecurity).sessionManagement(any());
        verify(httpSecurity).authorizeHttpRequests(any());
        assertNotNull(result);
    }
}
