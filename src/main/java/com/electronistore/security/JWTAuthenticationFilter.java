package com.electronistore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * todo:Step 4:
 *        Create JWTAuthenticationFilter that extends OncePerRequestFilter and override method, write the logic to
 *        check the token that is coming in header. We have to write 5 important logic
 *          ->  Get Token from request
 *          ->  Validate Token
 *          ->  Get username from token
 *          ->  Load user associated with this token
 *          ->  set authentication to security context
 *
 *
 * Ye API means controller se pahle chalega taki JWT token ko verify kar sake jo request ke sath aa rahi hai.
 * Request + JWT Token
 *
 */
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Authorization with request Token : Bearer dvbsyujfqeruaiwbcdn;
        String requestHeader = request.getHeader("Authorization");
        logger.info("Header {} ",requestHeader);

        String username = null;
        String token = null;

        if(requestHeader != null && requestHeader.startsWith("Bearer")){
            // Pahle ham request se token nikalenge means Bearer ko remove karenge
            token = requestHeader.substring(7);

            // ab ham token se username retrieve karenge
            try{
                username = jwtHelper.getUsernameFromToken(token);
                logger.info("Username : {} ", username);
            }
            catch (IllegalArgumentException ex){
                logger.warn("Illegal Argument while fetching the username!! "+ex.getMessage());
            }
            catch (ExpiredJwtException ex){
                logger.warn("Given JWT token has been expired "+ex.getMessage());
            }
            catch (MalformedJwtException ex){
                logger.warn("Some change has done in token!! Invalid Token "+ex.getMessage());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
        else{
            logger.warn("Invalid Header!! Header is not starting with Bearer or Header is empty");
        }

        // Agar username null nahi hai to
        // Aur security context mein koi pahle se authenticate na hua ho means security context null ho
        if(username != null && SecurityContextHolder.getContext().getAuthentication() ==null){

            // Ab ham user ke details ko nikalenge by username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // request se aaye huye username aur userDetails se aaye huye username ko validate karenge
            // Aur check karenge ki username expired to nhi hai na

            if(username.equals(userDetails.getUsername()) && !jwtHelper.isTokenExpired(token)){
                // token is validated
                //security context k ander authentication set kar denge
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request,response);
    }
}
