package org.example.ecommerce.configuration.jwt;

import com.nimbusds.jose.JOSEException;
import org.example.ecommerce.user.model.User;

import java.text.ParseException;
import java.util.List;

public interface JwtService {
//    AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    String generateToken(User user);
    void verifyToken(String token) throws JOSEException, ParseException;
    List<String> buildScope(User user);

}
