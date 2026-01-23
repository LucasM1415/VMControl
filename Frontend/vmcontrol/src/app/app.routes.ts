import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { VmListComponent } from './components/vm/vm-list.component';
import { OverviewComponent } from './components/dashboard/overview.component';
import { vmListResolver } from './resolvers/vm-list.resolver';
import { dashboardResolver } from './resolvers/dashboard.resolver';

export const routes: Routes = [
  { path: '', component: LoginComponent },
  { path: 'login', component: LoginComponent },
  { path: 'dashboard', component: DashboardComponent, resolve: { vms: dashboardResolver }, runGuardsAndResolvers: 'always', children: [
    { path: '', component: OverviewComponent },
    { path: 'vm', component: VmListComponent, resolve: { vms: vmListResolver }, runGuardsAndResolvers: 'always' }
  ]},
  { path: 'vm', redirectTo: 'dashboard/vm' }
];
