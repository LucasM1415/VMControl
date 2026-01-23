import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: 'login.component.html',
  styleUrl: 'login.component.css'
})
export class LoginComponent implements OnInit {
  showLoginForm = true;
  showRegisterForm = false;

  loginForm = {
    email: '',
    password: '',
    rememberMe: false
  };

  registerForm = {
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    terms: false
  };

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    // Initialize feather icons only in the browser
    if (isPlatformBrowser(this.platformId) && typeof (window as any).feather !== 'undefined') {
      (window as any).feather.replace();
    }
  }

  toggleForms(): void {
    this.showLoginForm = !this.showLoginForm;
    this.showRegisterForm = !this.showRegisterForm;
  }

  onLoginSubmit(): void {
    console.log('Login form submitted:', this.loginForm);
    // Add your login logic here
    // e.g., call your authentication service
  }

  onRegisterSubmit(): void {
    if (this.registerForm.password !== this.registerForm.confirmPassword) {
      alert('As senhas não coincidem!');
      return;
    }
    console.log('Register form submitted:', this.registerForm);
    // Add your registration logic here
    // e.g., call your authentication service
  }

  onForgotPassword(): void {
    console.log('Forgot password clicked');
    // Add your forgot password logic here
  }
}
