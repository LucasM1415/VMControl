import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { isPlatformBrowser } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { StorageService } from '../../services/storage.service';
import { HttpClientModule } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
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

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private authService: AuthService,
    private storageService: StorageService,
    private router: Router
  ) {}

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
    if (!this.loginForm.email || !this.loginForm.password) {
      alert('Por favor, preencha todos os campos!');
      return;
    }

    this.authService.login(this.loginForm.email, this.loginForm.password).subscribe({
      next: (response) => {
        console.log('Login realizado com sucesso:', response);
        // Salva os dados do usuário no localStorage
        this.storageService.saveUser({
          token: response.token,
          email: response.email,
          nome: response.nome,
          role: response.role,
          id: response.id
        });
        alert(`Bem-vindo, ${response.nome}!`);
        // Limpa o formulário
        this.loginForm = {
          email: '',
          password: '',
          rememberMe: false
        };
        this.router.navigate(['/dashboard']);
      },
      error: (error) => {
        console.error('Erro ao fazer login:', error);
        const errorMessage = error.error?.message || 'Email ou senha incorretos!';
        alert(errorMessage);
      }
    });
  }

  onRegisterSubmit(): void {
    if (this.registerForm.password.length < 6) {
      alert('A senha deve ter no mínimo 6 caracteres.');
      return;
    }

    if (this.registerForm.password !== this.registerForm.confirmPassword) {
      alert('As senhas não coincidem!');
      return;
    }

    if (!this.registerForm.terms) {
      alert('Você precisa aceitar os Termos de Serviço!');
      return;
    }

    this.authService.registrar(
      this.registerForm.name,
      this.registerForm.email,
      this.registerForm.password
    ).subscribe({
      next: (response) => {
        console.log('Cadastro realizado com sucesso:', response);
        alert('Cadastro realizado com sucesso! Faça login para continuar.');
        // Limpa o formulário
        this.registerForm = {
          name: '',
          email: '',
          password: '',
          confirmPassword: '',
          terms: false
        };
        // Alterna para o formulário de login
        this.toggleForms();
      },
      error: (error) => {
        console.error('Erro ao cadastrar:', error);
        const errorMessage = error.error?.message || 'Erro ao realizar cadastro. Tente novamente.';
        alert(errorMessage);
      }
    });
  }

  onForgotPassword(): void {
    console.log('Forgot password clicked');
    // Add your forgot password logic here
  }
}
