package org.shopby_backend.status.persistence;

import org.shopby_backend.status.model.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface StatusRepository extends JpaRepository<StatusEntity, Long> {
    Optional<StatusEntity> findByLibelle(String libelle);
}
