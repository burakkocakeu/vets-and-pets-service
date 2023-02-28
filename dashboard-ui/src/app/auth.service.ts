import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import jwtDecode from "jwt-decode";
import jwt_decode from "jwt-decode";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  userRole: string = '';
  userId: string = '';

  constructor(private http: HttpClient) { }

  getUserId(): string {
    const token = localStorage.getItem('access_token');
    if (token) {
      const decodedToken: any = jwt_decode(token);
      this.userId = decodedToken.userId
    }
    return this.userId;
  }

  getRole(): string {
    const token = localStorage.getItem('access_token');
    if (token) {
      const decodedToken: any = jwt_decode(token);
      this.userRole = decodedToken.ROLES[0]
    }
    return this.userRole;
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('access_token');
    if (token) {
      const decodedToken = jwtDecode(token) as { exp: number };
      if (decodedToken.exp * 1000 > Date.now()) {
        // Token is not expired
        return true;
      }
      return false;
    }
    return false;
  }

  login(username: string, password: string) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>('http://localhost:8500/api/v1/auth/authenticate', { username, password }, { headers });
  }

  register(body: any) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>('http://localhost:8500/api/v1/auth/register', body, { headers });
  }

  logout() {
    const token = localStorage.getItem('access_token');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` });
    return this.http.get<any>('http://localhost:8500/api/v1/auth/logout', { headers });
  }

}
