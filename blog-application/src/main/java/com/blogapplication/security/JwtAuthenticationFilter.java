package com.blogapplication.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private JwtTokenHelper jth;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //1. get token
        String requestToken = request.getHeader("Authorization");
        System.out.println(requestToken);

        String username = null;
        String token = null;

        if(requestToken != null && requestToken.startsWith("Bearer")){

            token = requestToken.substring(7);

            try {
                username = this.jth.getUsernameFromToken(token);

            }catch(IllegalArgumentException e){
                System.out.println("Unable to get Jwt token");
            }catch(ExpiredJwtException e){
                System.out.println("Jwt token has expired");
            }catch(MalformedJwtException e){
                System.out.println("Invalid Jwt");
            }
        }else{
            System.out.println("Jwt token does not begin with Bearer");
        }

        //once we get the token, now validate

        if(username !=  null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.uds.loadUserByUsername(username);

            if(this.jth.validateToken(token, userDetails)){

                //everything is okay till now
                //let's do authentication

                UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(upat);

            }else{
                System.out.println("Invalid jwt token");
            }

        }else{
            System.out.println("username is null or context is not null");
        }

        filterChain.doFilter(request, response);

    }
}
