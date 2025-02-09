package com.example.demo.petoffice.rest.jpa.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demo.petoffice.rest.jpa.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

  @Query("SELECT c FROM Client c WHERE c.id = :id and (c.stf is not null and c.stf = :stf "
      + "or c.stf is null and not :stf)")
  Optional<Client> findClientById(Long id, boolean stf);

  @Modifying
  @Query("delete from ClientPetRef cpr where cpr.client = (select c from Client c where c.stf = true)")
  void deleteStData();
}
