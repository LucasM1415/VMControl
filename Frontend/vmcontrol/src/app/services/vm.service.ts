import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';

export interface UsuarioVM {
  id: number;
  nome: string;
  email: string;
}

export interface VM {
  id: number;
  nome: string;
  status: 'RUNNING' | 'STOPPED' | string;
  cpu: number;
  cpuUso: number;
  memoriaRam: number;
  memoriaUso: number;
  tamanhoDisco: number;
  discoUso: number;
  dataCriacao: string;
  ultimaAtualizacao: string;
  usuario: UsuarioVM;
}

@Injectable({ providedIn: 'root' })
export class VmService {
  private baseUrl = 'http://localhost:8080/api/v1/vms';

  constructor(private http: HttpClient, @Inject(PLATFORM_ID) private platformId: Object) {}

  listarTodas(): Observable<VM[]> {
    // Evita chamada no SSR (sem token/localStorage), retorna vazio e deixa o cliente carregar
    if (!isPlatformBrowser(this.platformId)) {
      return of([]);
    }
    return this.http.get<VM[]>(`${this.baseUrl}/todas`);
  }
}
