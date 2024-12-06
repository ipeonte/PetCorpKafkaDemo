import { Component } from '@angular/core';
import { Pet } from './Pet';
import { Client } from '../client/Client';
import { PetService } from './pet.service';
import { ClientService } from '../client/client.service';
import { FormsModule } from '@angular/forms';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-pets',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './pets.component.html',
  styleUrl: './pets.component.css'
})

export class PetsComponent {

  // Initialize empty arrays
  pets: Pet[] = [];
  clients: Client[] = [];
  // Reverst client's map
  client_map = new Map<string, Client>();

  // Pet Id to find
  pet_id: string = "";
  user_mask = 3;

  // Number of selected pets
  selected_cnt: number = 0;

  // Selected client id
  client_id: string = "";

  newPetModal: bootstrap.Modal | undefined;

  // New Pet Model
  new_pet :Pet = new Pet(0,"","","");

  constructor(private petService: PetService, private clientService: ClientService) {}

  ngOnInit(): void {
    this.reset();
  }

  reset() {
    this.petService.getPets(this.user_mask).then((data) => {
      this.pets = data;

      // Sort by id
      this.sort(this.pets);
    });

    this.clientService.getClients().then((data) => {
      this.clients = data;

      // Recreate client's map
      this.client_map.clear;
      for(var id in data) {
        const client = data[id];
        this.client_map.set(String(client.id), client)
      }
    });

    this.pet_id = "";
  }
  
  find() {
    this.petService.find(this.pet_id, this.user_mask).then((data) => {
      this.pets = [data];

      // Sort by id
      this.sort(this.pets);
    });
  }

  sort(pets: Pet[]) {
    pets.sort((a,b) => {
      return a.id < b.id ? -1 : a.id == b.id ? 0 : 1;
    })
  }

  is_visible(mask: number) {
    return this.user_mask !== undefined 
      ? (this.user_mask & mask) == mask
      : false;
  };

  toggle_selection(pet: Pet) {
    if (pet.checked) {
      this.selected_cnt++;
    } else {
      this.selected_cnt--;
    }
  };

  adopt_pets() {
    console.log(this.client_id);
    const client = this.client_map.get(this.client_id);
    console.log(this.client_map);
    console.log(client);

    if (!client)
      return;

    if (!confirm("Please confirm adoption of " +
        this.selected_cnt + " pet" + (this.selected_cnt > 1 ? "s" : "") +
          " by " + client.firstName + " " + client.lastName))
      return;
    
    // Collect pet id's
    for (var id in this.pets) {
      var pet = this.pets[id];
      if (!pet.checked)
        continue;
      
      console.log(pet);

      (function(srv, pet) {
        srv.petService.adoptPet(pet.id, srv.client_id).then(() => {
          pet.checked = false;
          srv.selected_cnt--;
          srv.client_id = "";

          var idx = srv.pets.indexOf(pet);
          srv.pets.splice(idx, 1);
        });
      })(this, pet);
    }
  };

  openModal(el: Element) {
    this.newPetModal = new bootstrap.Modal(el,{}); 
    this.newPetModal?.show();
  }

  closeModal() {
    this.newPetModal?.toggle();
  }
  addNewPet() {
    // Close Modal
    this.petService.addNewPet(this.new_pet).then((pet) => {
      this.closeModal();

      // Clear new pet
      this.new_pet.clear();
      
      // Add new pet
      this.pets.push(pet);
    });
  }

  checkInputValue(value: string) {
    console.log(value);
    // suppose value is number;
    var patt = new RegExp(/^[0-9]+$/gm);
    var res = patt.test(value);
    console.log(res);
    if(res){
    //fetch data from db and fill the second form
    }
}
}
