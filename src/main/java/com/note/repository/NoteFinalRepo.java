package com.note.repository;

import com.note.model.NoteFinal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteFinalRepo extends JpaRepository<NoteFinal, Long> {

}