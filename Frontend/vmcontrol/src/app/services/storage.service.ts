import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

export interface UserData {
  token: string;
  email: string;
  nome: string;
  role: string;
  id: number;
}

@Injectable({
  providedIn: 'root'
})
export class StorageService {
  private readonly USER_KEY = 'vmcontrol_user';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  saveUser(userData: UserData): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.setItem(this.USER_KEY, JSON.stringify(userData));
    }
  }

  getUser(): UserData | null {
    if (isPlatformBrowser(this.platformId)) {
      const data = localStorage.getItem(this.USER_KEY);
      return data ? JSON.parse(data) : null;
    }
    return null;
  }

  getToken(): string | null {
    const user = this.getUser();
    return user ? user.token : null;
  }

  clearUser(): void {
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem(this.USER_KEY);
    }
  }

  isLoggedIn(): boolean {
    return this.getToken() !== null;
  }
}
