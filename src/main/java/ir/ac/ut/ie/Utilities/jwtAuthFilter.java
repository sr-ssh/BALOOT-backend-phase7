package ir.ac.ut.ie.Utilities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

@Component
public class jwtAuthFilter implements Filter {

//    private static String SECRET_KEY = "baloot1402";
    public static final String KEY = "baloot1402";
    private static String ISSUER = "info@baloot.com";

    private static final ArrayList<String> excludedUrls = new ArrayList<>() {
        {
            add("login");
            add("signup");
        }
    };

    @Override
    public void init(FilterConfig filterConfig) {}

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        String uri = httpRequest.getRequestURI();

        String[] path = ((HttpServletRequest) servletRequest).getRequestURI().split("/");
        if (path.length > 2 && path[1].equals("api") && excludedUrls.contains(path[2])) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String header = httpRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }
        String token = header.substring(7);

        Claims jswClaims;
        try {
            jswClaims = decodeJWT(token);

            if (jswClaims.getExpiration().before(new Date()))
                throw new Exception("Token is expired.");

            servletRequest.setAttribute("userId", jswClaims.getSubject());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            httpResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpResponse.setHeader("Content-Type", "application/json;charset=UTF-8");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public static Claims decodeJWT(String jwt) {
        SecretKey key = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(jwt).getBody();
    }

    @Override
    public void destroy() {}
}


