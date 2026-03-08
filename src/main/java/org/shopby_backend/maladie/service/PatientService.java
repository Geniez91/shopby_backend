package org.shopby_backend.maladie.service;

import org.shopby_backend.maladie.dto.PatientDto;
import org.shopby_backend.maladie.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class PatientService {
    public List<PatientDto> getPrioritaryPatients(List<Patient> patientList){
        return patientList.stream()
                .filter(patient -> Objects.equals(patient.pathologie(), "Maladie d'Addison")|| patient.age()>60)
                .map(patient -> new PatientDto(patient.name(),patient.age(),patient.pathologie())).toList();
    }
}
