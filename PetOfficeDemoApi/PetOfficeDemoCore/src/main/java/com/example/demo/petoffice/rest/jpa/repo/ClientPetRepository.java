package com.example.demo.petoffice.rest.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.petoffice.rest.jpa.model.ClientPetRef;

@Repository
public interface ClientPetRepository extends JpaRepository<ClientPetRef, Long> {

}
