package org.example.blps_lab1.adapters.db.saga;

import org.example.blps_lab1.core.domain.saga.FailureRecord;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FailureRecordRepository extends JpaRepository<FailureRecord, Long> {
}
