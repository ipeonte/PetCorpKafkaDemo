package com.example.demo.petstore.rest.jpa.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.petstore.rest.jpa.model.Pet;

/**
 * Pets Repository
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */
@Repository
public interface PetsRepository extends JpaRepository<Pet, Long> {

  List<Pet> findAllByAdoptedOrderByNameAsc(String adopted);

  List<Pet> findByVaccinatedAndAdoptedOrderByNameAsc(String vaccinated, String adopted);

  Pet findByIdAndVaccinatedAndAdopted(Long id, String vaccinated, String adopted);
}
