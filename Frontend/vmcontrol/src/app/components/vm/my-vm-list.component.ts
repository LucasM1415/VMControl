import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, NavigationEnd } from '@angular/router';
import { VmService, VM } from '../../services/vm.service';
import { Subject } from 'rxjs';
import { takeUntil, filter } from 'rxjs/operators';

@Component({
  selector: 'app-my-vm-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-vm-list.component.html'
})
export class MyVmListComponent implements OnInit, OnDestroy {
  vms: VM[] = [];
  filteredVms: VM[] = [];
  searchTerm: string = '';
  loading = false;
  error: string | null = null;
  expandedId: number | null = null;
  showModal = false;
  editingVm: VM | null = null;
  showCreateModal = false;
  newVm: any = { nome: '', cpu: 1, memoriaRam: 1, tamanhoDisco: 1 };
  notificationMessage: string | null = null;
  private destroy$ = new Subject<void>();

  constructor(private route: ActivatedRoute, private router: Router, private vmService: VmService) {}

  ngOnInit(): void {
    // Carrega os dados já resolvidos pela rota
    this.route.data.pipe(takeUntil(this.destroy$)).subscribe((data) => {
      this.vms = (data['vms'] || []).sort((a: VM, b: VM) => a.id - b.id);
      this.filteredVms = [...this.vms];
    });

    // Refaz a busca sempre que navegarmos para /dashboard/minhas-vms
    this.router.events
      .pipe(
        filter((e): e is NavigationEnd => e instanceof NavigationEnd),
        takeUntil(this.destroy$)
      )
      .subscribe((e) => {
        if (e.urlAfterRedirects.includes('/dashboard/minhas-vms')) {
          this.fetchVms();
        }
      });
  }

  private fetchVms(): void {
    this.loading = true;
    this.error = null;
    this.vmService.listarMinhas()
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (data: VM[]) => {
          this.vms = data.sort((a, b) => a.id - b.id);
          this.filteredVms = [...this.vms];
          this.filterVms();
          this.loading = false;
        },
        error: (err) => {
          console.error('Erro ao listar minhas VMs', err);
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
      this.vmService.buscarMinhaPorId(vmId)
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

  toggle(vmId: number, event?: Event): void {
    if (event) {
      event.stopPropagation();
    }
    this.expandedId = this.expandedId === vmId ? null : vmId;
  }

  openEditModal(vm: VM, event: Event): void {
    event.stopPropagation();
    this.editingVm = { ...vm };
    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.editingVm = null;
  }

  openCreateModal(): void {
    this.newVm = { nome: '', cpu: 1, memoriaRam: 1, tamanhoDisco: 1 };
    this.showCreateModal = true;
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
  }

  createVm(): void {
    const createData = {
      nome: this.newVm.nome,
      cpu: this.newVm.cpu,
      memoriaRam: this.newVm.memoriaRam,
      tamanhoDisco: this.newVm.tamanhoDisco
    };

    this.vmService.criarMinhaVm(createData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (vm: VM) => {
          console.log('VM criada com sucesso:', vm);
          this.notificationMessage = 'VM criada com sucesso';
          this.closeCreateModal();
          setTimeout(() => {
            window.location.reload();
          }, 100);
        },
        error: (err) => {
          console.error('Erro ao criar VM', err);
          this.error = err?.error?.message || 'Falha ao criar VM';
          this.closeCreateModal();
        }
      });
  }

  isValidNewVM(): boolean {
    return !!(
      this.newVm.nome &&
      this.newVm.nome.length >= 6 &&
      this.newVm.cpu > 0 &&
      this.newVm.memoriaRam > 0 &&
      this.newVm.tamanhoDisco > 0
    );
  }

  saveVm(): void {
    if (!this.editingVm) return;
    
    const updateData = {
      nome: this.editingVm.nome,
      cpu: this.editingVm.cpu,
      memoriaRam: this.editingVm.memoriaRam,
      tamanhoDisco: this.editingVm.tamanhoDisco
    };

    this.vmService.atualizarMinhaVm(this.editingVm.id, updateData)
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (vm: VM) => {
          console.log('VM atualizada com sucesso:', vm);
          this.expandedId = null; // Fechar a seção de detalhes
          this.fetchVms(); // Recarregar a lista após atualizar
        },
        error: (err) => {
          console.error('Erro ao atualizar VM', err);
          this.error = err?.error?.message || 'Falha ao atualizar VM';
        }
      });
    
    this.closeModal(); // Fechar o modal imediatamente
  }

  isValidVM(): boolean {
    if (!this.editingVm) return false;
    
    return !!(
      this.editingVm.nome && 
      this.editingVm.nome.length >= 6 &&
      this.editingVm.cpu > 0 &&
      this.editingVm.memoriaRam > 0 &&
      this.editingVm.tamanhoDisco > 0
    );
  }

  deleteVm(vmId: number, event: Event): void {
    event.stopPropagation();
    if (confirm('Tem certeza que deseja excluir esta VM?')) {
      this.vmService.deletarMinhaVm(vmId)
        .pipe(takeUntil(this.destroy$))
        .subscribe({
          next: () => {
            console.log('VM excluída:', vmId);
            this.notificationMessage = 'VM excluída com sucesso';
            setTimeout(() => {
              window.location.reload();
            }, 100);
          },
          error: (err) => {
            console.error('Erro ao excluir VM', err);
            this.error = err?.error?.message || 'Falha ao excluir VM';
          }
        });
    }
  }

  startVm(vmId: number, event: Event): void {
    event.stopPropagation();
    this.expandedId = null;
    this.vmService.alterarStatusMinhaVm(vmId, 'STARTED')
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (vm: VM) => {
          console.log('VM iniciada:', vm);
          this.notificationMessage = 'Iniciando máquina virtual';
          setTimeout(() => {
            window.location.reload();
          }, 100);
        },
        error: (err) => {
          console.error('Erro ao iniciar VM', err);
          this.error = err?.error?.message || 'Falha ao iniciar VM';
        }
      });
  }

  pauseVm(vmId: number, event: Event): void {
    event.stopPropagation();
    this.expandedId = null;
    this.vmService.alterarStatusMinhaVm(vmId, 'SUSPENDED')
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (vm: VM) => {
          console.log('VM pausada:', vm);
          this.notificationMessage = 'Pausando máquina virtual';
          setTimeout(() => {
            window.location.reload();
          }, 100);
        },
        error: (err) => {
          console.error('Erro ao pausar VM', err);
          this.error = err?.error?.message || 'Falha ao pausar VM';
        }
      });
  }

  stopVm(vmId: number, event: Event): void {
    event.stopPropagation();
    this.expandedId = null;
    this.vmService.alterarStatusMinhaVm(vmId, 'STOPPED')
      .pipe(takeUntil(this.destroy$))
      .subscribe({
        next: (vm: VM) => {
          console.log('VM parada:', vm);
          this.notificationMessage = 'Parando máquina virtual';
          setTimeout(() => {
            window.location.reload();
          }, 100);
        },
        error: (err) => {
          console.error('Erro ao parar VM', err);
          this.error = err?.error?.message || 'Falha ao parar VM';
        }
      });
  }

  statusColor(status: string): string {
    switch (status) {
      case 'RUNNING':
      case 'STARTED':
        return 'bg-green-100 text-green-700';
      case 'STOPPED':
        return 'bg-gray-200 text-gray-700';
      default:
        return 'bg-amber-100 text-amber-700';
    }
  }
}
