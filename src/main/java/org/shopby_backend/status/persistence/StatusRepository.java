package org.shopby_backend.status.persistence;

import org.shopby_backend.status.model.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusEntity, Long>, JpaSpecificationExecutor<StatusEntity> {
    Optional<StatusEntity> findByLibelle(String libelle);
}
