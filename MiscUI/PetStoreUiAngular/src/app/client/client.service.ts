import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Client } from './Client';
import { lastValueFrom } from 'rxjs';
import { Constants } from '../common/global-constants';

@Injectable({
  providedIn: 'root'
})

export class ClientService {

  constructor(private http: HttpClient) { }

  async getClients(): Promise<Client[]> {
    
    // Initialize empty array with clients
    let clients: Client[] = [];
    const clients$ = lastValueFrom(this.http.get<Client[]>(Constants.CLIENTS_URL, Constants.CORS_HEADER));
    return await clients$;
  }
}
