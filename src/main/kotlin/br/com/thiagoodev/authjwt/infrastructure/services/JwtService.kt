package br.com.thiagoodev.authjwt.infrastructure.services

import com.nimbusds.jose.util.Base64URL
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService(
) {
    @Value("\${security.jwt.secret-key}")
    private lateinit var secretKey: String
    @Value("\${security.jwt.expiration-time}")
    private var expiration: Long = 0

    fun getExpiration(): Long = expiration

    fun generateToken(details: UserDetails): String {
        return buildToken(emptyMap(), details, expiration)
    }

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

    private fun getSignInKey(): Key {
        val keyBytes: ByteArray = Base64URL.from(secretKey).decode()
        return Keys.hmacShaKeyFor(keyBytes)
    }

    fun isTokenValid(token: String, details: UserDetails): Boolean {
        val username: String = extractUsername(token)
        return username == details.username && !isTokenExpired(token)
    }

    fun extractUsername(token: String): String {
        return extractClaim(token, Claims::getSubject)
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token!!)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        val key: SecretKey = Keys.hmacShaKeyFor(Base64URL.from(token).decode())
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .payload
    }
}