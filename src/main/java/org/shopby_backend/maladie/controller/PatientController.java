package org.shopby_backend.maladie.controller;

import lombok.AllArgsConstructor;
import org.shopby_backend.maladie.dto.PatientDto;
import org.shopby_backend.maladie.model.Patient;
import org.shopby_backend.maladie.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/patient")
@AllArgsConstructor
@RestController
public class PatientController {
    private PatientService patientService;

    @GetMapping
    public List<PatientDto> getPatientsPriorarty(List<Patient> patient){
        return patientService.getPrioritaryPatients(patient);
    }
}
