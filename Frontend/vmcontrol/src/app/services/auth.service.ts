import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface RegisterRequest {
  nome: string;
  email: string;
  senha: string;
}

export interface RegisterResponse {
  id?: string;
  message?: string;
}

export interface LoginRequest {
  email: string;
  senha: string;
}

export interface LoginResponse {
  token: string;
  email: string;
  nome: string;
  role: string;
  id: number;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/v1/auth'; 

  constructor(private http: HttpClient) {}

  registrar(nome: string, email: string, senha: string): Observable<RegisterResponse> {
    const body: RegisterRequest = {
      nome,
      email,
      senha
    };
    return this.http.post<RegisterResponse>(`${this.apiUrl}/registrar`, body);
  }

  login(email: string, senha: string): Observable<LoginResponse> {
    const body: LoginRequest = {
      email,
      senha
    };
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, body);
  }
}
