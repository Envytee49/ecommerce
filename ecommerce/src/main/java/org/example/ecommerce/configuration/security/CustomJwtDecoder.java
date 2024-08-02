package org.example.ecommerce.configuration.security;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.configuration.jwt.JwtService;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    private NimbusJwtDecoder jwtDecoder;
    @Autowired
    private JwtService jwtService;
    @Value("${jwt.signerKey}")
    private String KEY;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            jwtService.verifyToken(token);
        } catch (JOSEException | ParseException e) {
            log.error("401 Unauthenticated: {}", e.getMessage());
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), "HS512");
        jwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        return jwtDecoder.decode(token);
    }
}
