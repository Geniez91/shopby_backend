package org.shopby_backend.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.jwt.model.JwtEntity;
import org.shopby_backend.jwt.persistence.JwtRepository;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.service.UsersService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {
    public static final String BEARER = "Bearer ";
    private UsersService usersService;
    private final String ENCRYPTION_KEY="0da5d76b511ce73326f7b33281e5d2bdc13afd2d82ec4ac12ff463fb1e60cb0f";
    private final JwtRepository jwtRepository;

    public Map<String,String> generateToken(String email){
        /// Verification que l'utilisateur est dans la bdd
        UsersEntity user= (UsersEntity) this.usersService.loadUserByUsername(email);

        /// On désactive tous les tokens de l'utilisateur
        this.disabledTokens(user);

        ///Génération du Jwt Token
        Map<String,String> jwtMap = new HashMap<>(this.generateJwtToken(user));

        JwtEntity jwt=JwtEntity
                .builder()
                .token(jwtMap.get(BEARER))
                .disabled(false)
                .expired(false)
                .user(user)
                .build();

        this.jwtRepository.save(jwt);

        return jwtMap;
    }

    public void disabledTokens(UsersEntity user){
        /// On met à jour les tokens en les mettant les tokens a disabled et expired quand on se connecte
        final List<JwtEntity> jwtEntityList=this.jwtRepository.findByUserValidToken(user.getEmail()).peek((jwtEntity ->  {
            jwtEntity.setDisabled(true);
            jwtEntity.setExpired(true);
        })).toList();
        this.jwtRepository.saveAll(jwtEntityList);
    }

    public Map<String,String>generateJwtToken(UsersEntity user){
       /// Définitionn de l'instant actuel et de son expiration
        final long currentTimeMillis=System.currentTimeMillis();
        final long expirationTimeMillis=currentTimeMillis*60*1000;

        /// Définition des claims (infos de l'utilsateur)
        final Map<String,Object>claims=Map.of(
                "nom",user.getNom(),
                "prenom",user.getPrenom(),
                "email",user.getEmail(),
                Claims.EXPIRATION,new Date(expirationTimeMillis),
                Claims.SUBJECT,user.getEmail()
        );

        /// Création du bearer
        final String bearer= Jwts.builder()
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(expirationTimeMillis))
                .setSubject(user.getEmail())
                .addClaims(claims)
                .signWith(getKey(), SignatureAlgorithm.HS256)///Algorithme de Signature
                .compact();
        return Map.of(BEARER,bearer);
    }

    private Key getKey(){
        /// Clé d'encryptions
        final byte[] decoders= Decoders.BASE64.decode(ENCRYPTION_KEY);
        return Keys.hmacShaKeyFor(decoders);
    }

    public JwtEntity tokenByValue(String token){
        /// permet de vérifier si le token est existant dans la bdd
        JwtEntity jwt=this.jwtRepository.findByTokenAndDisabledAndExpired(token,false,false);
        if(jwt==null){
            throw  new JwtException("Token not found");
        }
        return jwt;
    }


    public boolean isTokenExpired(String token){
        Date expirationDate=this.getClaim(token,Claims::getExpiration);
        return expirationDate.before(new Date());
    }

    public String readEmailOnToken(String token){
        return this.getClaim(token,Claims::getSubject);
    }

    public Claims getAllClaims(String token){
        return Jwts.parser().setSigningKey(this.getKey()).build().parseClaimsJws(token).getBody();
    }

    public <T> T getClaim(String token, Function<Claims, T> function){
        Claims claims=this.getAllClaims(token);
        return function.apply(claims);
    }

    public void logOut() {
        UsersEntity user= (UsersEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();/// récupération de la session

        /// Récupration du token a partir du User
        JwtEntity jwt=jwtRepository.findByUserValidToken(user.getEmail(),false,false);

        if(jwt==null){
            throw  new JwtException("Token not found");
        }
        jwt.setExpired(true);
        jwt.setDisabled(true);
        jwtRepository.save(jwt);
    }

    /// Suppresion tous les minutes des tokens expired ou désactivé
    @Scheduled(cron = "0 */1 * * * *")
    public void removeUselessJwtToken(){
        log.info("Removing useless jwt token", Instant.now());
        this.jwtRepository.deleteAllByExpiredAndDisabled(true,true);
    }
}
