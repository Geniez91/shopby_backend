package org.shopby_backend.status.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.shopby_backend.status.dto.StatusFilter;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.service.StatusService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/status")
public class StatusController {
    private StatusService statusService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StatusOutputDto createStatus(@Valid @RequestBody StatusInputDto statusInputDto) {
        return statusService.addNewStatus(statusInputDto);
    }

    @PatchMapping("/{idStatus}")
    @ResponseStatus(HttpStatus.OK)
    public StatusOutputDto updateStatus(@PathVariable Long idStatus, @Valid @RequestBody StatusInputDto statusInputDto) {
        return statusService.updateStatus(idStatus,statusInputDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{idStatus}")
    public void deleteStatus(@PathVariable Long idStatus) {
         statusService.deleteStatus(idStatus);
    }

    @GetMapping("/{idStatus}")
    @ResponseStatus(HttpStatus.OK)
    public StatusOutputDto getStatusById(@PathVariable Long idStatus) {
        return statusService.getStatus(idStatus);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<StatusOutputDto> getAllStatus(StatusFilter filter, Pageable pageable) {
        return statusService.getAllStatus(filter,pageable);
    }
}
