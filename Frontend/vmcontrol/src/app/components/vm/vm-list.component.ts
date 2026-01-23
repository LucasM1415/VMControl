import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { VmService, VM } from '../../services/vm.service';
import { Subject } from 'rxjs';
import { takeUntil, filter } from 'rxjs/operators';

@Component({
  selector: 'app-vm-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './vm-list.component.html'
})
export class VmListComponent implements OnInit, OnDestroy {
  vms: VM[] = [];
  filteredVms: VM[] = [];
  searchTerm: string = '';
  loading = false;
  error: string | null = null;
  expandedId: number | null = null;
  private destroy$ = new Subject<void>();

  constructor(private route: ActivatedRoute, private router: Router, private vmService: VmService) {}

  ngOnInit(): void {
    // Carrega os dados já resolvidos pela rota
    this.route.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
      this.vms = (data['vms'] || []).sort((a: VM, b: VM) => a.id - b.id);
      this.filteredVms = [...this.vms];
    });

    // Refaz a busca sempre que navegarmos para /dashboard/vm
    this.router.events
      .pipe(
        filter((e): e is NavigationEnd => e instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe((e) => {
        if (e.urlAfterRedirects.includes('/dashboard/vm')) {
          this.fetchVms();
        }
      });
  }

  private fetchVms(): void {
    this.loading = true;
    this.error = null;
    this.vmService.listarTodas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: VM[]) => {
          this.vms = data.sort((a, b) => a.id - b.id);
          this.filteredVms = [...this.vms];
          this.filterVms();
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao listar VMs', err);
          this.error = err?.error?.message || 'Falha ao carregar VMs';
          this.loading = false;
        }
      });
  }

  filterVms(): void {
    const term = this.searchTerm.toLowerCase().trim();
    if (!term) {
      this.filteredVms = [...this.vms];
      return;
    }
    
    // Se o termo é um número, busca por ID na API
    if (/^\d+$/.test(term)) {
      const vmId = parseInt(term, 10);
      this.vmService.buscarPorId(vmId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: (vm: VM) => {
            this.filteredVms = [vm];
          },
          error: (err) => {
            console.error('Erro ao buscar VM por ID', err);
            this.filteredVms = [];
          }
        });
    } else {
      this.filteredVms = this.vms.filter(vm => 
        vm.id.toString().includes(term)
      );
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  toggle(vmId: number): void {
    this.expandedId = this.expandedId === vmId ? null : vmId;
  }

  statusColor(status: string): string {
    switch (status) {
      case 'RUNNING':
        return 'bg-green-100 text-green-700';
      case 'STOPPED':
        return 'bg-gray-200 text-gray-700';
      default:
        return 'bg-amber-100 text-amber-700';
    }
  }
}
