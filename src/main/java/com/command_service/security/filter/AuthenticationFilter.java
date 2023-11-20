package com.command_service.security.filter;

import com.command_service.dao.UserDAO;
import com.command_service.model.User;
import com.command_service.service.UtilService;
import com.command_service.security.TokenManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
public class AuthenticationFilter implements Filter {

    private final Logger LOG = LogManager.getLogger(AuthenticationFilter.class);

    private final TokenManager tokenManager = new TokenManager();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String tokenHeader = httpRequest.getHeader("Authorization");


        try {
            if (tokenHeader != null && tokenHeader.startsWith("Basic ")) {
                //Basic
                String basicToken = tokenHeader.substring(6);
                byte[] encodedString = Base64.getDecoder().decode(basicToken);
                String[] user = new String(encodedString).split(":");
                String username = user[0];
                String password = user[1];

                User userEntity = UserDAO.getUserByNameAndPassword(username, password);

                if(userEntity == null) {
                    LOG.error("Invalid username or password");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ResponseEntity<Object> responseEntity = new ResponseEntity<>("Invalid username or password", HttpStatus.FORBIDDEN);
                    response.getWriter().write(UtilService.convertObjectToJson(responseEntity));
                } else {
                    List<String> permissions = UserDAO.getUserPermissionsByEmail(userEntity.getEmail());

                    if(!permissions.isEmpty()) {
                        List<GrantedAuthority> authList = tokenManager.getAuthorities(permissions);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userEntity, null, authList);
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        filterChain.doFilter(httpRequest, response);
                    } else {
                        LOG.error("User has no permissions granted");
                        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                        ResponseEntity<Object> responseEntity = new ResponseEntity<>("No Authorization provided", HttpStatus.UNAUTHORIZED);
                        response.getWriter().write(UtilService.convertObjectToJson(responseEntity));
                    }
                }
            } else if (tokenHeader != null && tokenHeader.startsWith("Bearer ")){
                //JWT
                String userEmail = tokenManager.getAuthentication(httpRequest); // return String
                List<String> permissions = UserDAO.getUserPermissionsByEmail(userEmail);
                if(!permissions.isEmpty()) {
                    List<GrantedAuthority> authList = tokenManager.getAuthorities(permissions);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userEmail, httpRequest.getHeader("Authorization").replace("Bearer ", ""), authList);

//                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(httpRequest, response);
                } else {
                    LOG.error("User has no permissions granted");
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    ResponseEntity<Object> responseEntity = new ResponseEntity<>("No Authorization provided", HttpStatus.UNAUTHORIZED);
                    response.getWriter().write(UtilService.convertObjectToJson(responseEntity));
                }

            } else {
                LOG.error("No Authorization provided");
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                ResponseEntity<Object> responseEntity = new ResponseEntity<>("No Authorization provided", HttpStatus.UNAUTHORIZED);
                response.getWriter().write(UtilService.convertObjectToJson(responseEntity));
            }

        } catch (Exception e) {
            LOG.error(e.getMessage());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            ResponseEntity responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            response.getWriter().write(UtilService.convertObjectToJson(responseEntity));
        }
    }

    @Override
    public void destroy() {

    }

}
