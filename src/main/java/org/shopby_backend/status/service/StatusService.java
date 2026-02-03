package org.shopby_backend.status.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.brand.service.BrandService;
import org.shopby_backend.exception.status.StatusCreateException;
import org.shopby_backend.exception.status.StatusGetException;
import org.shopby_backend.exception.status.StatusUpdateException;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class StatusService {
    private StatusRepository statusRepository;
    private static final Logger logger = LoggerFactory.getLogger(StatusService.class);

    public StatusOutputDto addNewStatus(StatusInputDto statusInputDto) {
        long start = System.nanoTime();
        if (statusInputDto == null || statusInputDto.libelle() == null || statusInputDto.libelle().isBlank()) {
            String message = "Le libelle du status ne peut pas être vide";
            StatusCreateException exception = new StatusCreateException(message);
            logger.error(message, exception);
            throw exception;
        }

        if (statusRepository.findByLibelle(statusInputDto.libelle()).isPresent()) {
            StatusCreateException exception = new StatusCreateException("Le status existe deja avec le libelle " + statusInputDto.libelle());
            logger.error("Le status existe deja avec le libelle {}", statusInputDto.libelle(), exception);
        }

        StatusEntity newStatus = StatusEntity.builder()
                .libelle(statusInputDto.libelle())
                .build();
        StatusEntity savedEntity = statusRepository.save(newStatus);
        long durationMs = (System.nanoTime() - start) / 1000000;
        logger.info("Le status {} a bien été ajouté,durationMs = {}", savedEntity.getLibelle(), durationMs);
        return new StatusOutputDto(savedEntity.getIdStatus(), savedEntity.getLibelle());
    }

    public StatusOutputDto updateStatus(Long idStatus,StatusInputDto statusInputDto) {
        long start = System.nanoTime();
        if(statusInputDto.libelle().isBlank()){
            String message = "Le libelle du status ne peut pas être vide";
            StatusUpdateException exception = new StatusUpdateException(message);
            logger.error(message, exception);
            throw exception;
        }

        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusUpdateException exception = new StatusUpdateException("Aucun status n'existe avec l'id " + idStatus);
            logger.error("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });

        status.setLibelle(statusInputDto.libelle());
        StatusEntity savedStatus=statusRepository.save(status);
        long durationMs = (System.nanoTime() - start) / 1000000;
        logger.info("Le status {} a bien été mise à jour,durationMs = {}", savedStatus.getLibelle(), durationMs);
        return new StatusOutputDto(savedStatus.getIdStatus(),savedStatus.getLibelle());
    }

    public StatusOutputDto deleteStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusUpdateException exception = new StatusUpdateException("Aucun status avec l'id " + idStatus);
            logger.error("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });

        statusRepository.delete(status);
        long durationMs = (System.nanoTime() - start) / 1000000;
        logger.info("Le status {} a bien été supprimé,durationMs = {}", status.getIdStatus(), durationMs);
        return new StatusOutputDto(status.getIdStatus(),status.getLibelle());
    }

    public StatusOutputDto getStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusGetException exception = new StatusGetException("Aucun status avec l'id " + idStatus);
            logger.error("Aucun status avec l'id {}", idStatus, exception);
            return exception;
        });
        long durationMs = (System.nanoTime() - start) / 1000000;
        logger.info("Le status {} est bien présent, durationMs = {}", status.getIdStatus(), durationMs);
        return new StatusOutputDto(status.getIdStatus(),status.getLibelle());
    }

    public List<StatusOutputDto> getAllStatus() {
      long start = System.nanoTime();
      List<StatusOutputDto> listStatusDto= statusRepository.findAll().stream().map(statusEntity -> new StatusOutputDto(statusEntity.getIdStatus(),statusEntity.getLibelle())
        ).toList();
      long durationMs = (System.nanoTime() - start) / 1000000;
      logger.info("Il ya plus de {} status dans la base de données, durationMs = {}", listStatusDto.size(), durationMs);
      return listStatusDto;
    }

}
