import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class PetServiceService {

  headers;

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('access_token');
    this.headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` });
  }

  getPets(page: any = undefined, size: any = undefined) {
    const pageable = (page !== undefined && size !== undefined) ? '?page=' + page + '&size=' + size : '';
    const headers = this.headers;
    return this.http.get<any>(('http://localhost:8500/user/pets' + pageable), { headers });
  }

  createPet(body: any) {
    const headers = this.headers;
    return this.http.post<any>('http://localhost:8500/user/pets', body, { headers });
  }

  updatePet(index: number, body: any) {
    const headers = this.headers;
    return this.http.put<any>('http://localhost:8500/user/pets', body, { headers });
  }

  deletePet(pet: any) {
    const headers = this.headers;
    return this.http.delete<any>('http://localhost:8500/user/pets/' + pet.id, { headers });
  }

  getPetStatistics() {
    const headers = this.headers;
    return this.http.get<any>('http://localhost:8500/backoffice/user/pet-statistics', { headers });
  }

}
