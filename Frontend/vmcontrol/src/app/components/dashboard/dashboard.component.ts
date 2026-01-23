import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { StorageService, UserData } from '../../services/storage.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './dashboard.component.html'
})
export class DashboardComponent implements OnInit {
  user: UserData | null = null;

  stats = [
    { label: 'VMs ativas', value: 12, trend: '+8%', color: 'from-[#5B1ADB] to-[#310E75]' },
    { label: 'Uso de CPU', value: '43%', trend: '-3%', color: 'from-[#0EA5E9] to-[#0369A1]' },
    { label: 'Alertas', value: 2, trend: '+1', color: 'from-[#F59E0B] to-[#D97706]' }
  ];

  constructor(private storage: StorageService) {}

  ngOnInit(): void {
    this.user = this.storage.getUser();
  }

  get displayName(): string {
    return this.user?.nome || 'Admin';
  }

  logout(): void {
    this.storage.clearUser();
    // redirecione para login
    window.location.href = '/login';
  }
}
