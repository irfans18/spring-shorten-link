// package com.enigma.shorten_link.config.security;
//
// import com.enigma.wmb_api.entity.UserCredential;
// import com.enigma.wmb_api.model.response.JwtClaims;
// import com.enigma.wmb_api.service.JwtService;
// import com.enigma.wmb_api.service.UserCredentialService;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.web.authentication.WebAuthenticationDetails;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;
//
// import java.io.IOException;
//
// @Component
// @Slf4j
// @RequiredArgsConstructor
// public class AuthenticationFilter extends OncePerRequestFilter {
//     private final JwtService jwtService;
//     private final UserCredentialService userCredentialService;
//     @Override
//     protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//         try {
//             String bearerToken = request.getHeader("Authorization");
//             if (bearerToken != null && jwtService.verifyToken(bearerToken)) {
//                 JwtClaims claims = jwtService.getClaimsFromToken(bearerToken);
//                 UserCredential userCredential = userCredentialService.findOrFail(claims.getUserCredentialId());
//
//                 UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                         userCredential.getId(),
//                         null,
//                         userCredential.getAuthorities()
//                 );
//                 authentication.setDetails(new WebAuthenticationDetails(request));
//                 SecurityContextHolder.getContext().setAuthentication(authentication);
//             }
//         } catch (Exception e) {
//             log.error("Cannot set user authentication: {}", e.getMessage());
//         }
//         filterChain.doFilter(request, response);
//     }
// }
