import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  headers;

  constructor(private http: HttpClient) {
    const token = localStorage.getItem('access_token');
    this.headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` });
  }

  getAllUsers() {
    const headers = this.headers;
    return this.http.get<any>('http://localhost:8500/backoffice/user?all=true', { headers });
  }

  getUsers(event: any) {
    const headers = this.headers;
    return this.http.get<any>('http://localhost:8500/backoffice/user?search=' + event.target.value, { headers });
  }

  updateUser(body: any) {
    const headers = this.headers;
    return this.http.put<any>('http://localhost:8500/backoffice/user', body, { headers });
  }

  deleteUser(userId: any) {
    const headers = this.headers;
    return this.http.delete<any>('http://localhost:8500/backoffice/user/' + userId, { headers });
  }

  getUserStatistics() {
    const headers = this.headers;
    return this.http.get<any>('http://localhost:8500/backoffice/user/user-statistics?latestUserNumber=3', { headers });
  }

}
