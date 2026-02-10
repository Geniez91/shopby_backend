package org.shopby_backend.status.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.status.StatusAlreadyExistsException;
import org.shopby_backend.exception.status.StatusGetException;
import org.shopby_backend.exception.status.StatusNotFoundException;
import org.shopby_backend.exception.status.StatusUpdateException;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.shopby_backend.tools.Tools;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class StatusService {
    private StatusRepository statusRepository;

    public StatusOutputDto addNewStatus(StatusInputDto statusInputDto) {
        long start = System.nanoTime();

        if (statusRepository.findByLibelle(statusInputDto.libelle()).isPresent()) {
            StatusAlreadyExistsException exception = new StatusAlreadyExistsException("Le status existe deja avec le libelle " + statusInputDto.libelle());
            log.warn("Le status existe deja avec le libelle {}", statusInputDto.libelle(), exception);
        }

        StatusEntity newStatus = StatusEntity.builder()
                .libelle(statusInputDto.libelle())
                .build();
        StatusEntity savedEntity = statusRepository.save(newStatus);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été ajouté,durationMs = {}", savedEntity.getLibelle(), durationMs);
        return new StatusOutputDto(savedEntity.getIdStatus(), savedEntity.getLibelle());
    }

    public StatusOutputDto updateStatus(Long idStatus,StatusInputDto statusInputDto) {
        long start = System.nanoTime();

        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusNotFoundException exception = new StatusNotFoundException("Aucun status n'existe avec l'id " + idStatus);
            log.warn("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });

        status.setLibelle(statusInputDto.libelle());
        StatusEntity savedStatus=statusRepository.save(status);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été mise à jour,durationMs = {}", savedStatus.getLibelle(), durationMs);
        return new StatusOutputDto(savedStatus.getIdStatus(),savedStatus.getLibelle());
    }

    public void deleteStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusNotFoundException exception = new StatusNotFoundException("Aucun status avec l'id " + idStatus);
            log.warn("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });

        statusRepository.delete(status);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été supprimé,durationMs = {}", status.getIdStatus(), durationMs);
    }

    public StatusOutputDto getStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusNotFoundException exception = new StatusNotFoundException("Aucun status avec l'id " + idStatus);
            log.warn("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} est bien présent, durationMs = {}", status.getIdStatus(), durationMs);
        return new StatusOutputDto(status.getIdStatus(),status.getLibelle());
    }

    public List<StatusOutputDto> getAllStatus() {
      long start = System.nanoTime();
      List<StatusOutputDto> listStatusDto= statusRepository.findAll().stream().map(statusEntity -> new StatusOutputDto(statusEntity.getIdStatus(),statusEntity.getLibelle())
        ).toList();
      long durationMs = Tools.getDurationMs(start);
      log.info("Il ya plus de {} status dans la base de données, durationMs = {}", listStatusDto.size(), durationMs);
      return listStatusDto;
    }

}
