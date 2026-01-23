import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';
import { ActivatedRoute } from '@angular/router';
import { VM } from '../../services/vm.service';

@Component({
  selector: 'app-dashboard-overview',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './overview.component.html'
})
export class OverviewComponent implements OnInit {
  stats = [
    { label: 'VMs ativas', value: 0, trend: '+0%', color: 'from-[#5B1ADB] to-[#310E75]' },
    { label: 'Uso de CPU', value: '43%', trend: '-3%', color: 'from-[#0EA5E9] to-[#0369A1]' },
    { label: 'Alertas', value: 2, trend: '+1', color: 'from-[#F59E0B] to-[#D97706]' }
  ];

  // Gráfico de barras - Status das VMs
  barChartData: ChartConfiguration<'bar'>['data'] = {
    labels: ['STARTED', 'STOPPED', 'SUSPENDED'],
    datasets: [
      {
        label: 'Quantidade de VMs',
        data: [0, 0, 0],
        backgroundColor: ['#10B981', '#6B7280', '#F59E0B'],
        borderRadius: 4,
        borderSkipped: false
      }
    ]
  };

  barChartOptions: ChartConfiguration<'bar'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    resizeDelay: 150,
    indexAxis: 'x',
    plugins: {
      legend: {
        display: true
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        ticks: {
          stepSize: 1
        }
      }
    }
  };

  // Gráfico de pizza - Limite de VMs
  pieChartData: ChartConfiguration<'doughnut'>['data'] = {
    labels: ['VMs Usadas', 'VMs Disponíveis'],
    datasets: [
      {
        data: [0, 5],
        backgroundColor: ['#8B5CF6', '#E5E7EB'],
        borderColor: '#FFFFFF',
        borderWidth: 2
      }
    ]
  };

  pieChartOptions: ChartConfiguration<'doughnut'>['options'] = {
    responsive: true,
    maintainAspectRatio: false,
    resizeDelay: 150,
    plugins: {
      legend: {
        position: 'bottom'
      }
    }
  };

  totalVms = 0;
  maxVms = 5;

  constructor(private cdr: ChangeDetectorRef, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.loadVmsFromResolver();
  }

  private loadVmsFromResolver(): void {
    const vms = this.route.snapshot.parent?.data['vms'] || [];
    this.updateCharts(vms);
  }

  private updateCharts(vms: VM[]): void {
    this.totalVms = vms.length;

    // Contar VMs por status
    const statusCount = {
      'STARTED': 0,
      'STOPPED': 0,
      'SUSPENDED': 0
    };

    vms.forEach((vm) => {
      if (statusCount[vm.status as keyof typeof statusCount] !== undefined) {
        statusCount[vm.status as keyof typeof statusCount]++;
      }
    });

    // Atualizar dados do gráfico de barras
    this.barChartData = {
      ...this.barChartData,
      datasets: [
        {
          ...this.barChartData.datasets[0],
          data: [
            statusCount['STARTED'],
            statusCount['STOPPED'],
            statusCount['SUSPENDED']
          ]
        }
      ]
    };

    // Atualizar dados do gráfico de pizza
    const vmsDispo = Math.max(this.maxVms - this.totalVms, 0);
    this.pieChartData = {
      ...this.pieChartData,
      datasets: [
        {
          ...this.pieChartData.datasets[0],
          data: [this.totalVms, vmsDispo]
        }
      ]
    };

    // Atualizar card de VMs ativas
    this.stats = [
      { ...this.stats[0], value: this.totalVms },
      this.stats[1],
      this.stats[2]
    ];

    this.cdr.markForCheck();
  }
}
