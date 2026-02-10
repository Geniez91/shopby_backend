package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.users.*;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.typeArticle.service.TypeArticleService;
import org.shopby_backend.users.dto.*;
import org.shopby_backend.users.model.RoleEntity;
import org.shopby_backend.users.model.TypeRoleEnum;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class UsersService implements UserDetailsService {
    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ValidationService validationService;

    public UsersDto addUser(UserInputDto userInputDto) {
        long start = System.nanoTime();
        if(usersRepository.findByEmail(userInputDto.email()).isPresent()){
            String message = "Vos identifiants existe deja";
            UsersAlreadyExistsException exception = new UsersAlreadyExistsException(message);
            log.warn(message,exception);
            throw exception;
        }

        if(!userInputDto.email().contains("@")){
            String message = "Votre email est invalide";
            UsersCreateException exception = new UsersCreateException(message);
            log.error(message,exception);
            throw exception;
        }

        final RoleEntity roleUser=new RoleEntity();
        roleUser.setLibelle(TypeRoleEnum.USER);

        UsersEntity user = UsersEntity.builder()
                .nom(userInputDto.nom())
                .prenom(userInputDto.prenom())
                .password(bCryptPasswordEncoder.encode(userInputDto.password()))
                .email(userInputDto.email())
                .role(roleUser)
                .country(userInputDto.country())
                .enabled(false)
                .billingAddress(null)
                .deliveryAddress(null)
                .build();
        UsersEntity savedUser = usersRepository.save(user);

        if(roleUser.getLibelle().equals(TypeRoleEnum.USER)){
            validationService.save(savedUser);
        }

        long durationMs = Tools.getDurationMs(start);
        log.info("L'utilisateur {} a bien été ajouté, durationMs = {}",savedUser.getId(),durationMs);
        return new UsersDto(
                savedUser.getId(),
                savedUser.getNom(),
                savedUser.getPrenom(),
                savedUser.getPassword(),
                savedUser.getEmail(),
                savedUser.getCountry());
    }

    public String activationUser(String code) {
        long start = System.nanoTime();
        ValidationEntity validation =this.validationService.readCode(code);

        if(Instant.now().isAfter(validation.getExpirationDate())){
            ValidationAccountException exception = new ValidationAccountException("Le code d'utilisateur a expiré le "+validation.getExpirationDate());
            log.warn("Le code d'utilisateur a expiré le {}",validation.getExpirationDate(),exception);
            throw exception;
        }

        UsersEntity userExist=this.usersRepository.findById(validation.getUser().getId()).orElseThrow(()->
        {
            ValidationAccountException exception = new ValidationAccountException("L'utilisateur n'existe pas avec l'id user "+validation.getUser().getId());
            log.warn("L'utilisateur n'existe pas avec l'id user {}",validation.getUser().getId(),exception);
            return exception;
        });

        userExist.setEnabled(true);
        this.usersRepository.save(userExist);
        long durationMs = Tools.getDurationMs(start);
        log.info("L'utilisateur {} a bien été activé, durationMs = {}",userExist.getId(),durationMs);
        return "L'utilisateur a bien été activé";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        long start = System.nanoTime();

        UserDetails userDetails=usersRepository.findByEmail(email).orElseThrow(()->{
            UsernameNotFoundException exception = new UsernameNotFoundException("L'utilisateur n'existe pas avec l'email "+email);
            log.warn("L'utilisateur n'existe pas avec l'email {}",email,exception);
            return exception;
        });

        long durationMs = Tools.getDurationMs(start);
        log.info("L'utilisateur {} a bien été trouvé,durationMs = {}",userDetails.getUsername(),durationMs);
        return userDetails;
    }

    public void resetPassword(UserResetDto userResetDto){
        long start = System.nanoTime();
        UsersEntity user = (UsersEntity) this.loadUserByUsername(userResetDto.email());
        this.validationService.save(user);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le mot de passe a bien été reset avec l'email {},durationMs = {}",userResetDto.email(),durationMs);
    }

    public void newPassword(UserNewPasswordDto userNewPasswordDto){
        long start = System.nanoTime();
        /// On recherche l'utilisateur par son adresse mail
        UsersEntity user=usersRepository.findByEmail(userNewPasswordDto.email()).orElseThrow(()->{
            NewPasswordException exception = new NewPasswordException("L'email ne correspond a aucun utilisateur avec l'email "+userNewPasswordDto.email());
            log.warn("L'email ne correspond a aucun utilisateur avec l'email {}",userNewPasswordDto.email(),exception);
            return exception;
        });

        /// On regarde si le code donnait correspond
        final ValidationEntity validation=this.validationService.readCode(userNewPasswordDto.code());

        if(validation.getUser().getEmail().equals(user.getEmail())){
            String passwordCrypted=bCryptPasswordEncoder.encode(userNewPasswordDto.password());
            user.setPassword(passwordCrypted);
            this.usersRepository.save(user);
            long durationMs = Tools.getDurationMs(start);
            log.info("Le mot de passe de l'utilisateur a bien été changer avec l'email {}, durationMs = {}",userNewPasswordDto.email(),durationMs);
        }
    }

    public List<UsersOutput> findAllUsers(){
        long start = System.nanoTime();
        List<UsersOutput> listUsers = this.usersRepository.findAll().stream().map(user->new UsersOutput(
                user.getId(),
                user.getPrenom(),
                user.getNom(),
                user.getPassword(),
                user.getEmail()
        )).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre d'utilisateur dans la base de données est de {},durationMs = {}",listUsers.size(),durationMs);
        return listUsers;
    }

    public void updateUserRole(UserUpdateRoleDto userInputDto){
        long start = System.nanoTime();
        UsersEntity user=this.usersRepository.findByEmail(userInputDto.email()).orElseThrow(()->{
            UsersNotFoundException exception = new UsersNotFoundException("Aucun users trouvés avec l'adresse mail "+userInputDto.email());
            log.warn("Aucun users trouvés avec l'adresse mail {}",userInputDto.email(),exception);
            return exception;
        });

        final RoleEntity roleAdmin=new RoleEntity();
        roleAdmin.setLibelle(userInputDto.role());
        user.setRole(roleAdmin);
        usersRepository.save(user);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le role de l'utilisateur {} a bien été mis à jour, durationMs={}",userInputDto.email(),durationMs);
    }

    public UserOutputInfoUpdateDto updateUserInfo(Long idUser, UserInfoUpdateDto userInfoUpdate){
        long start = System.nanoTime();
        UsersEntity user = usersRepository.findById(idUser).orElseThrow(() ->
        {
            UsersNotFoundException exception = new UsersNotFoundException("L'utilisateur n'existe pas avec l'id "+idUser);
            log.warn("L'utilisateur n'existe pas avec l'id {}",idUser,exception);
            return exception;
        });

        if(userInfoUpdate.prenom()!=null&& !userInfoUpdate.prenom().isBlank()){
            user.setPrenom(userInfoUpdate.prenom());
        }

        if(userInfoUpdate.nom()!=null&& !userInfoUpdate.nom().isBlank()){
            user.setNom(userInfoUpdate.nom());
        }

        if(userInfoUpdate.email()!=null&& !userInfoUpdate.email().isBlank()){
            user.setEmail(userInfoUpdate.email());
        }

        if(userInfoUpdate.country()!=null&& !userInfoUpdate.country().isBlank()){
            user.setCountry(userInfoUpdate.country());
        }

        if(userInfoUpdate.password()!=null&& !userInfoUpdate.password().isBlank()){
            bCryptPasswordEncoder.encode(userInfoUpdate.password());
        }

        if(userInfoUpdate.billingAddress()!=null&& !userInfoUpdate.billingAddress().isBlank()){
            user.setBillingAddress(userInfoUpdate.billingAddress());
        }

        if(userInfoUpdate.deliveryAddress() !=null&& !userInfoUpdate.deliveryAddress().isBlank()){
            user.setDeliveryAddress(userInfoUpdate.deliveryAddress());
        }
        UsersEntity savedUsers= usersRepository.save(user);
        long durationMs=Tools.getDurationMs(start);
        log.info("L'utilisateur {} a bien été mise à jour, durationMs = {}",savedUsers.getId(),durationMs);
        return UserOutputInfoUpdateDto.builder()
                .id(savedUsers.getId())
                .nom(savedUsers.getNom())
                .prenom(savedUsers.getPrenom())
                .password(savedUsers.getPassword())
                .email(savedUsers.getEmail())
                .country(savedUsers.getCountry())
                .deliveryAddress(savedUsers.getDeliveryAddress())
                .billingAddress(savedUsers.getBillingAddress())
                .build();
    }
}
