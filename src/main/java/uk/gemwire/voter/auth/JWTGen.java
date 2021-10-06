package uk.gemwire.voter.auth;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

/**
 * Create and manage JSON Web Tokens for authentication with Google.
 *
 * Github has a very long and complicated process for managing their Apps' authentication, this class should make the
 * public interface easier to deal with.
 *
 * Use JWTGen.createJWT to make use of this class.
 *
 */
public class JWTGen {

    /**
     * Read the private key from disk.
     * The Private Key is used to sign Authentication Requests.
     * @param filename The path to the private key.
     * @return The PrivateKey Instance of the given path.
     * @throws Exception
     */
    private static PrivateKey getKey(String filename) throws Exception {
        byte[] keyBytes = Files.readAllBytes(Path.of(filename));

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    /**
     * Create a JSON Web Token that can be used to authenticate with the Github Servers.
     * One of the primary authentication procedures.
     * @param githubAppId The ID of the Github App. used as the "issuer" field of the token.
     * @param ttlMillis Time To Live. Can be 0 for infinite, or a length of milliseconds.
     * @return A string representing the JWT.
     * @throws Exception
     */
    public static String createJWT(String githubAppId, long ttlMillis) throws Exception {
        // The JWT signature algorithm to sign the token with
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        // Sign the JWT with our private key
        Key signingKey = getKey("github-api-app.private-key.der");

        // Set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setIssuer(githubAppId)
                .signWith(signingKey, signatureAlgorithm);

        // if it has been specified, add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        } else {
            // Default to 10 minutes
            Date exp = new Date(nowMillis + (1000 * 60 * 10));
            builder.setExpiration(exp);
        }

        // Build the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }
}
