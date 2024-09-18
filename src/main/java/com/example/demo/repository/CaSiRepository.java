package com.example.demo.repository;

import com.example.demo.entity.CaSi;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaSiRepository extends JpaRepository<CaSi ,Integer> {
    CaSi getById(Integer id);
}
