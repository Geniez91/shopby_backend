package org.shopby_backend.status.service;
import lombok.AllArgsConstructor;
import org.shopby_backend.exception.status.StatusCreateException;
import org.shopby_backend.exception.status.StatusUpdateException;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class StatusService {
    private StatusRepository statusRepository;

    public StatusOutputDto addNewStatus(StatusInputDto statusInputDto) {
        if (statusInputDto == null || statusInputDto.libelle() == null || statusInputDto.libelle().isBlank()) {
            throw new StatusCreateException("Le libelle du status ne peut pas être vide");
        }

        if (statusRepository.findByLibelle(statusInputDto.libelle()).isPresent()) {
            throw new StatusCreateException("Le status existe deja");
        }
        StatusEntity newStatus = StatusEntity.builder()
                .libelle(statusInputDto.libelle())
                .build();
        StatusEntity savedEntity = statusRepository.save(newStatus);
        return new StatusOutputDto(savedEntity.getIdStatus(), savedEntity.getLibelle());
    }

    public StatusOutputDto updateStatus(Long idStatus,StatusInputDto statusInputDto) {
        if(statusInputDto.libelle().isBlank()){
            throw new StatusCreateException("Le libelle du status ne peut pas être vide");
        }
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()-> new StatusUpdateException("Le status n'existe pas"));
        status.setLibelle(statusInputDto.libelle());
        StatusEntity savedStatus=statusRepository.save(status);
        return new StatusOutputDto(savedStatus.getIdStatus(),savedStatus.getLibelle());
    }

    public StatusOutputDto deleteStatus(Long idStatus) {
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()-> new StatusUpdateException("Le status n'existe pas"));
        statusRepository.delete(status);
        return new StatusOutputDto(status.getIdStatus(),status.getLibelle());
    }

    public StatusOutputDto getStatus(Long idStatus) {
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()-> new StatusUpdateException("Le status n'existe pas"));
        return new StatusOutputDto(status.getIdStatus(),status.getLibelle());
    }

    public List<StatusOutputDto> getAllStatus() {
      return  statusRepository.findAll().stream().map(statusEntity -> {
            return new StatusOutputDto(statusEntity.getIdStatus(),statusEntity.getLibelle());
        }
        ).toList();
    }

}
