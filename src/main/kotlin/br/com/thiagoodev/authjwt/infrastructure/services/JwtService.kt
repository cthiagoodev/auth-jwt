package br.com.thiagoodev.authjwt.infrastructure.services

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import java.security.Key
import java.util.Date
import javax.crypto.SecretKey

class JwtService {
    @Value("app.jwt-secret")
    private lateinit var secretKey: SecretKey
    @Value("app.jwt-expiration-milliseconds")
    private var expiration: Long = 0

    fun generateToken(details: UserDetails): String {
        return buildToken(emptyMap(), details, expiration)
    }

    fun getExpiration(): Long = expiration

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long,
    ): String {
        return Jwts
            .builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey())
            .compact()
    }

    private fun <T> extractClaim(token: String, claimsResolver: () -> Unit): () -> T {
        val claims: Claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    private fun extractAllClaims(token: String): Claims {
         return Jwts
            .parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Decoders.BASE64.decode(secretKey.toString())
        return Keys.hmacShaKeyFor(keyBytes)
    }
}