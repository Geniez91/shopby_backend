package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.users.*;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.shopby_backend.users.dto.*;
import org.shopby_backend.users.mapper.UsersMapper;
import org.shopby_backend.users.model.RoleEntity;
import org.shopby_backend.users.model.TypeRoleEnum;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.model.ValidationEntity;
import org.shopby_backend.users.persistence.UsersRepository;
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
    private final UsersRepository usersRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ValidationService validationService;
    private final UsersMapper usersMapper;

    public UsersDto addUser(UserInputDto userInputDto) {
        long start = System.nanoTime();
        if (usersRepository.findByEmail(userInputDto.email()).isPresent()) {
            UsersAlreadyExistsException exception = new UsersAlreadyExistsException(userInputDto.email());
            log.warn(LogMessages.USERS_ALREADY_EXISTS, userInputDto.email(), exception);
            throw exception;
        }

        if (!userInputDto.email().contains("@")) {
            String message = "Votre email est invalide";
            UsersCreateException exception = new UsersCreateException(message);
            log.error(message, exception);
            throw exception;
        }

        final RoleEntity roleUser = new RoleEntity();
        roleUser.setLibelle(TypeRoleEnum.USER);

        UsersEntity user = usersMapper.toEntity(userInputDto, bCryptPasswordEncoder, roleUser);
        UsersEntity savedUser = usersRepository.save(user);

        if (roleUser.getLibelle().equals(TypeRoleEnum.USER)) {
            validationService.save(savedUser);
        }

        long durationMs = Tools.getDurationMs(start);
        log.info("L'utilisateur {} a bien été ajouté, durationMs = {}", savedUser.getId(), durationMs);
        return usersMapper.toDto(savedUser);
    }

    public String activationUser(String code) {
        long start = System.nanoTime();
        ValidationEntity validation =this.validationService.readCode(code);

        if(Instant.now().isAfter(validation.getExpirationDate())){
            ValidationAccountException exception = new ValidationAccountException(validation.getExpirationDate());
            log.warn(LogMessages.VALIDATION_EXPIRED,validation.getExpirationDate(),exception);
            throw exception;
        }

        UsersEntity userExist=this.findUsersOrThrow(validation.getUser().getId());
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
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_EMAIL,email,exception);
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
        UsersEntity user=this.findUsersByEmailOrThrow(userNewPasswordDto.email());
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

    public List<UsersDto> findAllUsers(){
        long start = System.nanoTime();
        List<UsersDto> listUsers = this.usersRepository.findAll().stream().map(user->usersMapper.toDto(user)).toList();
        long durationMs = Tools.getDurationMs(start);
        log.info("Le nombre d'utilisateur dans la base de données est de {},durationMs = {}",listUsers.size(),durationMs);
        return listUsers;
    }

    public void updateUserRole(UserUpdateRoleDto userInputDto){
        long start = System.nanoTime();
        UsersEntity user=this.findUsersByEmailOrThrow(userInputDto.email());
        final RoleEntity roleAdmin=new RoleEntity();
        roleAdmin.setLibelle(userInputDto.role());
        user.setRole(roleAdmin);
        usersRepository.save(user);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le role de l'utilisateur {} a bien été mis à jour, durationMs={}",userInputDto.email(),durationMs);
    }

    public UsersDto updateUserInfo(Long idUser, UserInfoUpdateDto userInfoUpdate){
        long start = System.nanoTime();
        UsersEntity user = this.findUsersOrThrow(idUser);

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
        return usersMapper.toDto(savedUsers);
    }
    public UsersEntity findUsersOrThrow(Long idUser){
       return usersRepository.findById(idUser).orElseThrow(() -> {
            UsersNotFoundException exception = UsersNotFoundException.byUserId(idUser);
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_ID, idUser, exception);
            return exception;
        });
    }

    public UsersEntity findUsersByEmailOrThrow(String email){
        return usersRepository.findByEmail(email).orElseThrow(()->{
            NewPasswordException exception = new NewPasswordException(email);
            log.warn(LogMessages.USERS_NOT_FOUND_BY_USER_EMAIL,email,exception);
            return exception;
        });
    }


}
