package br.com.thiagoodev.authjwt.infrastructure.config

import br.com.thiagoodev.authjwt.infrastructure.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtAuthenticationFilter(
    private val handlerExceptionResolver: HandlerExceptionResolver,
    private val jwtService: JwtService,
    private val userDetailsService: UserDetailsService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader: String = request.getHeader("Authorization")

        if(authHeader.isEmpty() || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val jwt: String = authHeader.substring(7)
            val userEmail: String = jwtService.extractUsername(jwt)

            val authentication: Authentication = SecurityContextHolder.getContext().authentication

            if(userEmail.isNotEmpty() && authentication.isAuthenticated) {
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(userEmail)

                if(jwtService.isTokenValid(jwt, userDetails)) {
                    val authToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.authorities,
                    )

                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }

            filterChain.doFilter(request, response)
        } catch(error: Exception) {
            handlerExceptionResolver.resolveException(request, response, null, error)
        }
    }
}