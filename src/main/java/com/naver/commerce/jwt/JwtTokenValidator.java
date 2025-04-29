package com.naver.commerce.jwt;


import com.naver.commerce.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.rmi.ServerException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenValidator {

    @Value("${naver.api.public-key}")
    private String PUBLIC_KEY;


    public Map<String, Object> getClaimWithVerify(String jwt, String issuer, String subjectType) throws ServerException {
        JwtConsumer jwtConsumer = null;
        try {
            jwtConsumer = new JwtConsumerBuilder()
                    .setRequireExpirationTime()
                    .setExpectedIssuer(issuer)
                    .setExpectedSubject(subjectType)
                    .setVerificationKey(RsaUtil.toPublicKeyFromBase64(PUBLIC_KEY))
                    .setJwsAlgorithmConstraints(
                            AlgorithmConstraints.ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) // which is only RS256 here
                    .build();
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
            throw new ServerException("JWT publicKey verify exception", e);
        }

        try {
            //  Validate the JWT and process it to the Claims
            JwtClaims jwtClaims = jwtConsumer.processToClaims(jwt);
            log.debug("JWT validation succeeded! " + jwtClaims);
            return jwtClaims.getClaimsMap();
        } catch (InvalidJwtException e) {
            String code = "Invalid JWT";
            String errorMsg = "Invalid JWT! " + e.getMessage();
            log.debug(errorMsg, e);

            if (e.hasExpired()) {
                code = "JWT expired";
                try {
                    errorMsg = "JWT expired at " + e.getJwtContext().getJwtClaims().getExpirationTime();
                    log.debug(errorMsg);
                } catch (MalformedClaimException malformedClaimException) {
                    errorMsg = malformedClaimException.getMessage();
                    log.error(malformedClaimException.getMessage(), malformedClaimException);
                }
            }
            throw new RuntimeException(String.format("%s - %s", code, errorMsg), e);
        }
    }

}
