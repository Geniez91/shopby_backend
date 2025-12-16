package org.shopby_backend.users.service;

import lombok.AllArgsConstructor;
import org.shopby_backend.exception.users.UsersCreateException;
import org.shopby_backend.users.dto.UserInputDto;
import org.shopby_backend.users.dto.UsersDto;
import org.shopby_backend.users.model.UsersEntity;
import org.shopby_backend.users.persistence.UsersRepository;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UsersService {
    private UsersRepository usersRepository;

    public UsersDto addUser(UserInputDto userInputDto) {
        if(usersRepository.findByEmail(userInputDto.email())!=null){
            throw new UsersCreateException("Vos identifiants existe deja");
        }

        if(!userInputDto.email().contains("@")){
            throw new UsersCreateException("Votre email est invalide");
        }

        UsersEntity user = UsersEntity.builder()
                .nom(userInputDto.nom())
                .prenom(userInputDto.prenom())
                .password(userInputDto.password())
                .email(userInputDto.email())
                .build();
        UsersEntity savedUser = usersRepository.save(user);
        return new UsersDto(
                savedUser.getId(),
                savedUser.getNom(),
                savedUser.getPrenom(),
                savedUser.getPassword(),
                savedUser.getEmail());
    }

}
