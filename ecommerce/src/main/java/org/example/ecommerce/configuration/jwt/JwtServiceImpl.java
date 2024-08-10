package org.example.ecommerce.configuration.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.example.ecommerce.exception.AppException;
import org.example.ecommerce.exception.ErrorCode;
import org.example.ecommerce.shop.repository.ShopRepository;
import org.example.ecommerce.user.model.User;
import org.example.ecommerce.user.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtServiceImpl implements JwtService {
    private final ShopRepository shopRepository;
    @Value("${jwt.signerKey}")
    private String signerKey;
    private final UserRoleRepository userRoleRepository;

    public JwtServiceImpl(UserRoleRepository userRoleRepository, ShopRepository shopRepository) {
        this.userRoleRepository = userRoleRepository;
        this.shopRepository = shopRepository;
    }

    @Override
    public String generateToken(User user) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        List<String> userRoles = buildScope(user);

        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder()
                .subject(user.getUuidUser())
                .issuer("envytee")
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plusSeconds(3600)))
                .claim("scope", userRoles)
                .claim("uuidCart", user.getUuidCart());

        JWTClaimsSet jwtClaimsSet = buildJWTClaimSet(user.getUuidUser(), userRoles, jwtClaimsSetBuilder);

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            JWSSigner signer = new MACSigner(signerKey);
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("401 unauthenticated: "+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private JWTClaimsSet buildJWTClaimSet(String uuidUser,
                                          List<String> userRoles,
                                          JWTClaimsSet.Builder jwtClaimsSetBuilder) {
        if(userRoles.contains("SELLER")) {
            String uuidShop = shopRepository.findByUuidSeller(uuidUser).getUuidShop();
            return jwtClaimsSetBuilder.claim("uuidShop", uuidShop).build();
        }
        return jwtClaimsSetBuilder.build();
    }

    @Override
    public void verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(signerKey);

        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        Date expirationTime = jwtClaimsSet.getExpirationTime();
        boolean isVerified = signedJWT.verify(verifier);
        if (!(isVerified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    @Override
    public List<String> buildScope(User user) {
        return userRoleRepository.findByUuidUser(user.getUuidUser())
                    .stream().map(userRole -> userRole.getRole().name()).toList();
    }

}
