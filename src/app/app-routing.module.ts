import { NgModule } from '@angular/core';
import { RouterModule, Router, Routes } from '@angular/router';
import { Http, Response } from '@angular/http';

import { RoutesLoaderService } from 'app/routes-loader.service';
import { LoginComponent } from 'app/components/login.component';
import { AdminLoginComponent } from 'app/shared/authentication/admin/admin-login.component';

const initialRoutes: Routes = [{
    path: '**',
    canActivate: [RoutesLoaderService],
    children: []
}]

@NgModule({
    imports: [RouterModule.forRoot(initialRoutes, { enableTracing: true })],
    exports: [RouterModule]
})
export class AppRoutingModule { }

