package br.com.thiagoodev.authjwt.infrastructure.services

import com.nimbusds.jose.util.Base64URL
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
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

    fun getRefreshExpiration(): Long = expiration + 1000 * 60 * 60 * 24 * 7

    fun generateToken(details: UserDetails, newExpiration: Long? = null): String {
        return buildToken(emptyMap(), details, newExpiration ?: expiration)
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

    fun isTokenValid(token: String, details: UserDetails, newExpiration: Long? = null): Boolean {
        val username: String = extractUsername(token)
        return username == details.username && !isTokenExpired(token, newExpiration)
    }

    fun extractUsername(token: String): String {
        return extractClaim(extractToken(token), Claims::getSubject)
    }

    private fun isTokenExpired(token: String, newExpiration: Long?): Boolean {
        return extractExpiration(token).before(Date(newExpiration ?: expiration))
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String?, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token!!)
        return claimsResolver(claims)
    }

    private fun extractAllClaims(token: String): Claims {
        val extractedToken: String? = extractToken(token)
        val key: SecretKey = Keys.hmacShaKeyFor(Base64URL.from(secretKey).decode())
        return Jwts
            .parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(extractedToken)
            .payload
    }

    private fun extractToken(token: String): String? {
        val regex = Regex("""\b[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\.[A-Za-z0-9-_]+\b""")
        return regex.find(token)?.value
    }
}