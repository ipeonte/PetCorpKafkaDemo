package com.example.demo.petoffice.rest.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.petcorp.shared.SharedConstants;
import com.example.demo.petoffice.rest.dto.ClientDto;
import com.example.demo.petoffice.rest.error.PetOfficeExeption;
import com.example.demo.petoffice.rest.service.ClientService;

/**
 * ClientStore Controller
 * 
 * @author Igor Peonte <igor.144@gmail.com>
 *
 */

@RestController
@RequestMapping(SharedConstants.BASE_URL)
public class ClientController {

  @Autowired
  private ClientService clientService;

  /*********************************/
  /** Common Section **/
  /**
   * @throws PetOfficeExeption
   *******************************/

  @GetMapping("/clients")
  public List<ClientDto> getClients(@RequestParam(required = false) String st)
      throws PetOfficeExeption {
    return clientService.getAllClients(st != null);
  }

  @GetMapping("/clients/{id}/pets")
  public List<Long> getClientPets(@PathVariable Long id, @RequestParam(required = false) String st)
      throws PetOfficeExeption {
    return clientService.getClientPets(id, st != null);
  }

  @DeleteMapping("/clients/clear_st")
  public void clearSynthTestData() throws PetOfficeExeption {
    clientService.clearSynthTestData();
  }
}
