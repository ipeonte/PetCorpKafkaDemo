package com.example.demo.petstore.rest.jpa.repo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

  @Query("SELECT p FROM Pet p WHERE p.adopted = :adopted "
      + "and (p.stf is not null and p.stf = :stf or p.stf is null and not :stf) "
      + "order by name asc")
  List<Pet> findAllByAdoptedOrderByNameAsc(String adopted, boolean stf);

  @Query("SELECT p FROM Pet p WHERE p.vaccinated = :vaccinated and p.adopted = :adopted "
      + "and (p.stf is not null and p.stf = :stf or p.stf is null and not :stf) "
      + "order by name asc")
  List<Pet> findVaccinatedAdoptedPets(String vaccinated, String adopted, boolean stf);

  @Query("SELECT p FROM Pet p WHERE p.id = :id and p.vaccinated = :vaccinated and p.adopted = :adopted "
      + "and (p.stf is not null and p.stf = :stf or p.stf is null and not :stf) "
      + "order by name asc")
  Pet findVaccinatedAdoptedPetById(Long id, String vaccinated, String adopted, boolean stf);

  @Query("SELECT p FROM Pet p WHERE p.id = :id and (p.stf is not null and p.stf = :stf "
      + "or p.stf is null and not :stf) order by name asc")
  Optional<Pet> findPetById(Long id, boolean stf);

  @Modifying
  @Query("delete from Pet p where p.stf = true")
  void deleteStData();
}
