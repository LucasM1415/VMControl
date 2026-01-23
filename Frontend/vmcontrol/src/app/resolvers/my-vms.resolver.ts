import { inject, PLATFORM_ID } from '@angular/core';
import { ResolveFn } from '@angular/router';
import { VmService, VM } from '../services/vm.service';
import { isPlatformBrowser } from '@angular/common';
import { of } from 'rxjs';

export const myVmsResolver: ResolveFn<VM[]> = () => {
  const platformId = inject(PLATFORM_ID);
  if (!isPlatformBrowser(platformId)) {
    // Avoid backend call during SSR; fetch on client navigation
    return of([]);
  }
  const svc = inject(VmService);
  return svc.listarMinhas();
};
