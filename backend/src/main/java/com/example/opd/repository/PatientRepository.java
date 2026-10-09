package com.example.opd.repository;

import com.example.opd.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Query("select p from Patient p where lower(p.name) like lower(concat('%', :q, '%')) or p.phone like concat('%', :q, '%') order by p.id desc")
    List<Patient> search(@Param("q") String q);
}
