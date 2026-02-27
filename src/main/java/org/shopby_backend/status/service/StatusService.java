package org.shopby_backend.status.service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shopby_backend.exception.status.StatusAlreadyExistsException;
import org.shopby_backend.exception.status.StatusGetException;
import org.shopby_backend.exception.status.StatusNotFoundException;
import org.shopby_backend.exception.status.StatusUpdateException;
import org.shopby_backend.order.model.OrderStatus;
import org.shopby_backend.status.dto.StatusFilter;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.mapper.StatusMapper;
import org.shopby_backend.status.model.StatusEntity;
import org.shopby_backend.status.persistence.StatusRepository;
import org.shopby_backend.status.specification.StatusSpecification;
import org.shopby_backend.tools.LogMessages;
import org.shopby_backend.tools.Tools;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class StatusService {
    private final StatusRepository statusRepository;
    private final StatusMapper statusMapper;

    @Transactional
    public StatusOutputDto addNewStatus(StatusInputDto statusInputDto) {
        long start = System.nanoTime();

        if (statusRepository.findByLibelle(statusInputDto.libelle()).isPresent()) {
            StatusAlreadyExistsException exception = new StatusAlreadyExistsException(statusInputDto.libelle());
            log.warn(LogMessages.STATUS_ALREADY_EXISTS, statusInputDto.libelle(), exception);
        }

        StatusEntity newStatus = statusMapper.toEntity(statusInputDto);
        StatusEntity savedEntity = statusRepository.save(newStatus);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été ajouté,durationMs = {}", savedEntity.getLibelle(), durationMs);
        return statusMapper.toDto(savedEntity);
    }

    @Transactional
    public StatusOutputDto updateStatus(Long idStatus,StatusInputDto statusInputDto) {
        long start = System.nanoTime();

        StatusEntity status=this.findStatusByIdOrThrow(idStatus);

        status.setLibelle(statusInputDto.libelle());
        StatusEntity savedStatus=statusRepository.save(status);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été mise à jour,durationMs = {}", savedStatus.getLibelle(), durationMs);
        return statusMapper.toDto(savedStatus);
    }

    @Transactional
    public void deleteStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=this.findStatusByIdOrThrow(idStatus);

        statusRepository.delete(status);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} a bien été supprimé,durationMs = {}", status.getIdStatus(), durationMs);
    }

    public StatusOutputDto getStatus(Long idStatus) {
        long start = System.nanoTime();
        StatusEntity status=this.findStatusByIdOrThrow(idStatus);
        long durationMs = Tools.getDurationMs(start);
        log.info("Le status {} est bien présent, durationMs = {}", status.getIdStatus(), durationMs);
        return statusMapper.toDto(status);
    }

    public Page<StatusOutputDto> getAllStatus(StatusFilter statusFilter, Pageable pageable) {
      long start = System.nanoTime();
      Specification<StatusEntity> filter = StatusSpecification.witFilters(statusFilter);
      Page<StatusEntity> page= statusRepository.findAll(filter, pageable);
      long durationMs = Tools.getDurationMs(start);
      log.info("Il ya plus de {} status dans la base de données,page : {}, durationMs = {}", page.getNumberOfElements(),page.getNumber(), durationMs);
      return page.map(statusMapper::toDto);
    }

    public StatusEntity findStatusByLibelleOrThrow(String libelle) {
       return statusRepository.findByLibelle(libelle).orElseThrow(() ->
        {
            StatusNotFoundException exception =  StatusNotFoundException.byOrderStatus(OrderStatus.valueOf(libelle));
            log.warn(LogMessages.STATUS_NOT_FOUND_BY_ORDER_STATUS,libelle,exception);
            return exception;
        });
    }

    public StatusEntity findStatusByIdOrThrow(Long idStatus) {
        return statusRepository.findById(idStatus).orElseThrow(()->
        {
            StatusNotFoundException exception = StatusNotFoundException.byId(idStatus);
            log.warn(LogMessages.STATUS_NOT_FOUND_BY_ID, idStatus, exception);
            return exception;
        });
    }

}
