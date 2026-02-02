package org.shopby_backend.status.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.status.dto.StatusInputDto;
import org.shopby_backend.status.dto.StatusOutputDto;
import org.shopby_backend.status.service.StatusService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
public class StatusController {
    private StatusService statusService;

    @PostMapping("/status")
    public StatusOutputDto createStatus(@RequestBody StatusInputDto statusInputDto) {
        return statusService.addNewStatus(statusInputDto);
    }

    @PatchMapping("/status/{idStatus}")
    public StatusOutputDto updateStatus(@PathVariable Long idStatus, @RequestBody StatusInputDto statusInputDto) {
        return statusService.updateStatus(idStatus,statusInputDto);
    }

    @DeleteMapping("/status/{idStatus}")
    public StatusOutputDto updateStatus(@PathVariable Long idStatus) {
        return statusService.deleteStatus(idStatus);
    }

    @GetMapping("/status/{idStatus}")
    public StatusOutputDto getStatusById(@PathVariable Long idStatus) {
        return statusService.getStatus(idStatus);
    }

    @GetMapping("/status/")
    public List<StatusOutputDto> getAllStatus() {
        return statusService.getAllStatus();
    }
}
