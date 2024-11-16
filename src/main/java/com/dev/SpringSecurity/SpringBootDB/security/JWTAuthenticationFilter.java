
package com.dev.SpringSecurity.SpringBootDB.security;
import com.dev.SpringSecurity.SpringBootDB.models.PermissionUrl;
import com.dev.SpringSecurity.SpringBootDB.services.AuthTokenService;
import com.dev.SpringSecurity.SpringBootDB.services.PermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private AuthTokenService authTokenService;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PermissionService permissionService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //extract token
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwtToken")) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {

                String username = jwtHelper.getUsernameFromToken(token);
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                List<PermissionUrl> permissions = permissionService.getPermissions();

                if (jwtHelper.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,/*authorities*/ userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.info("Invalid or expired token");
                }
            } catch (Exception ex) {
                logger.error("Error while validating token: {}", ex.getMessage());
            }
        }
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            List<PermissionUrl> permissions = permissionService.getPermissions(); // Get all permissions
            String requestUrl = request.getRequestURI(); // Get requested URL
            Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities(); // Get the user's authorities (roles)

            // List of public URLs that should be allowed for everyone (like login, register, etc.)
            List<String> publicUrls = List.of("/","/auth/login", "/auth/create-user","/api/**","/permissions/**"); // Add other public URLs as needed
            List<String> tokenAuthenticatedUrl = List.of("/home/homes", "/auth/logout");

            // Check if the request URL matches any of the public URLs using AntPathMatcher
            boolean isPublicUrl = publicUrls.stream()
                    .anyMatch(pattern -> new AntPathMatcher().match(pattern, requestUrl)); // Use new AntPathMatcher instance

            if (isPublicUrl) {
                filterChain.doFilter(request, response); // Allow access to public URLs without checking permissions
                return;
            }
            if (tokenAuthenticatedUrl.contains(requestUrl)) {
                filterChain.doFilter(request, response); // Allow access to public URLs without checking permissions
                return;
            }

            // Get user roles
            List<String> userRoles = authorities.stream()
                    .map(GrantedAuthority::getAuthority) // Extract the user's roles
                    .collect(Collectors.toList());

            // Check if any permission matches the requested URL and if the user has the required role
            boolean accessGranted = false;

            for (PermissionUrl permission : permissions) {
                if (requestUrl.equals(permission.getUrl())) {
                    // Check if the user has any of the required roles for this URL
                    String requiredRole = "ROLE_" + permission.getPermissionRole().toUpperCase();
                    accessGranted = userRoles.stream()
                            .anyMatch(role -> role.equals(requiredRole)); // Check if the user has the required role

                    if (accessGranted) {
                        break; // Access granted, no need to check further permissions
                    }
                }
            }
            if (!accessGranted) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    }

