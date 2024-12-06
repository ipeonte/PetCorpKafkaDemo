import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Pet } from './Pet';
import { lastValueFrom } from 'rxjs';
import { Constants } from '../common/global-constants';

@Injectable({
  providedIn: 'root',
})

export class PetService {
  
  constructor(private http: HttpClient) { }

  async getPets(user_mask: number): Promise<Pet[]> {
    const url = user_mask == 0 ? Constants.PETS_URL : Constants.PETS_ADMIN_URL;
    console.log(url);
    return await lastValueFrom(this.http.get<Pet[]>(url, Constants.CORS_HEADER));
  }

  async find(pet_id: string, user_mask: number): Promise<Pet> {
    const url = user_mask == 0 ? Constants.PET_FIND_URL : Constants.PET_ADMIN_URL;
    console.log(url);
    return await lastValueFrom(this.http.get<Pet>(`${url}/${pet_id}`, Constants.CORS_HEADER));
  };

  async adoptPet(pet_id: number, client_id: string) {
    return await lastValueFrom(this.http.post(Constants.MGR_ADOPT_PET + `${pet_id}/${client_id}`, Constants.CORS_HEADER));
  }

  async addNewPet(new_pet: Pet): Promise<Pet> {
    return await lastValueFrom(this.http.post<Pet>(Constants.PET_ADMIN_URL, new_pet, Constants.CORS_HEADER));
  }
}
