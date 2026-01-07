package org.shopby_backend.jwt.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.shopby_backend.jwt.model.JwtEntity;
import org.shopby_backend.users.service.UsersService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/// Filtre Jwt lors de l'exécution d'une route
@Service
public class JwtFilterService extends OncePerRequestFilter {
    private HandlerExceptionResolver exceptionResolver;
    private final UsersService usersService;
    private final JwtService jwtService;

    public JwtFilterService(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver exceptionResolver,UsersService userService,JwtService jwtService){
        this.exceptionResolver=exceptionResolver;
        this.usersService=userService;
        this.jwtService=jwtService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String email=null;
    String token=null;
    boolean isTokenExpired=true;
    JwtEntity jwtEntity=null;

    try{
        /// Récupération du token dans le header Authorization
    final String authorization=request.getHeader("Authorization");
    /// Le token apparait après le 7ème caractère
    if(authorization!=null&&authorization.startsWith("Bearer ")){
        token=authorization.substring(7);

        /// On récupére le Jwwt de la base grace à la bdd
        jwtEntity=this.jwtService.tokenByValue(token);

        isTokenExpired=jwtService.isTokenExpired(token);
        email=jwtService.readEmailOnToken(token);
    }

    /// Verifie si une personne est authentifié
    if(!isTokenExpired&& jwtEntity.getUser().getEmail().equals(email)&& SecurityContextHolder.getContext().getAuthentication()==null){
        UserDetails userDetails=usersService.loadUserByUsername(email);
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities()); ///Récupération du token d'authentification
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    filterChain.doFilter(request,response); ///Enchaine le filtre suivant
    }
    catch(Exception e){
        exceptionResolver.resolveException(request,response,null,e);
    }
    }


}
