package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.exception.users.ValidationAccountException;
import org.shopby_backend.users.dto.UserActivationDto;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UsersDto;
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
import java.util.Optional;

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
                .enabled(false)
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
                savedUser.getEmail());
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
}
