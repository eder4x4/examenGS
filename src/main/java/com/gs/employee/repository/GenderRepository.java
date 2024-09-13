package com.gs.employee.repository;

import com.gs.employee.model.entity.Gender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenderRepository extends JpaRepository<Gender, Long> {
}