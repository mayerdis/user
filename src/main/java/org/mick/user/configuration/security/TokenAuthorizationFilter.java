package org.mick.user.configuration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import org.mick.user.Dto.ErrorMessage;
import org.mick.user.configuration.JsonWebTokenUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class TokenAuthorizationFilter extends GenericFilterBean {

    private final JsonWebTokenUtils tokenUtils;

    public TokenAuthorizationFilter(JsonWebTokenUtils tokenUtils) {
        this.tokenUtils = tokenUtils;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String headerAuthorization = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);//response.getHeader(HttpHeaders.AUTHORIZATION);
        ObjectMapper mapper = new ObjectMapper();
        if (headerAuthorization != null) {
            headerAuthorization = headerAuthorization.substring(headerAuthorization.indexOf(" ")+1);
            if (tokenUtils.isValidToken(headerAuthorization)) {
                Claims c = tokenUtils.getAllClaimsFromToken(headerAuthorization);
                List<String> authorities = (List) c.get("authorities");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(c.getSubject(), null,
                        authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                ErrorMessage er = new ErrorMessage("EX09", "El Token proporcionado no es valido.");
                response.getWriter().write(mapper.writeValueAsString(er));
            }
        } else {
            ErrorMessage er = new ErrorMessage("EX09", "El Token proporcionado no es valido.");
            response.getWriter().write(mapper.writeValueAsString(er));
        }
        chain.doFilter(request, response);
    }
}
