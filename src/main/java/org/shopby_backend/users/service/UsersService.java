package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.exception.users.UsersUpdateException;
import org.shopby_backend.exception.users.ValidationAccountException;
import org.shopby_backend.users.dto.*;
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

@AllArgsConstructor
@Service
public class UsersService implements UserDetailsService {
    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ValidationService validationService;

    public UsersDto addUser(UserInputDto userInputDto) {
        if(usersRepository.findByEmail(userInputDto.email())!=null){
            throw new UsersCreateException("Vos identifiants existe deja");
        }

        if(!userInputDto.email().contains("@")){
            throw new UsersCreateException("Votre email est invalide");
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

        return new UsersDto(
                savedUser.getId(),
                savedUser.getNom(),
                savedUser.getPrenom(),
                savedUser.getPassword(),
                savedUser.getEmail(),
                savedUser.getCountry());
    }

    public String activationUser(String code) {
        ValidationEntity validation =this.validationService.readCode(code);

        if(Instant.now().isAfter(validation.getExpirationDate())){
            throw new ValidationAccountException("Le code d'activitation a expiré");
        }

        UsersEntity userExist=this.usersRepository.findById(validation.getUser().getId())
                .orElseThrow(()->new ValidationAccountException("L'utilisateur n'existe pas"));

        userExist.setEnabled(true);
        this.usersRepository.save(userExist);
        return "L'utilisateur a bien été activé";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserDetails userDetails=usersRepository.findByEmail(email);
        if(userDetails==null){
            throw new UsernameNotFoundException("Email non valide");
        }
        return userDetails;
    }

    public void resetPassword(UserResetDto userResetDto){
        UsersEntity user= (UsersEntity) this.loadUserByUsername(userResetDto.email());
        this.validationService.save(user);
    }

    public void newPassword(UserNewPasswordDto userNewPasswordDto){
        /// On recherche l'utilisateur par son adresse mail
        UsersEntity user=usersRepository.findByEmail(userNewPasswordDto.email());

        /// On regarde si le code donnait correspond
        final ValidationEntity validation=this.validationService.readCode(userNewPasswordDto.code());

        if(validation.getUser().getEmail().equals(user.getEmail())){
            String passwordCrypted=bCryptPasswordEncoder.encode(userNewPasswordDto.password());
            user.setPassword(passwordCrypted);
            this.usersRepository.save(user);
        }
    }

    public List<UsersOutput> findAllUsers(){
        return this.usersRepository.findAll().stream().map(user->new UsersOutput(
                user.getId(),
                user.getPrenom(),
                user.getNom(),
                user.getPassword(),
                user.getEmail()
        )).toList();
    }

    public void updateUserRole(UserUpdateRoleDto userInputDto){
        UsersEntity user=this.usersRepository.findByEmail(userInputDto.email());
        final RoleEntity roleAdmin=new RoleEntity();
        roleAdmin.setLibelle(userInputDto.role());
        user.setRole(roleAdmin);
        usersRepository.save(user);
    }

    public UserOutputInfoUpdateDto updateUserInfo(Long idUser, UserInfoUpdateDto userInfoUpdate){
        UsersEntity user = usersRepository.findById(idUser)
                .orElseThrow(() -> new UsersUpdateException("L'utilisateur n'existe pas"));

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
