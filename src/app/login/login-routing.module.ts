import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { LoginComponent } from './login.component'

import { AdminLoginModule } from 'app/login/admin/admin-login.module'
import { AdminLoginComponent } from 'app/login/admin/admin-login.component'

const eoxLoginRoutes: Routes = [{
    path: ':app/login',
    component: LoginComponent,
    children: [{
        path: '',
        component: AdminLoginComponent
    }]
}];

@NgModule({
    imports: [RouterModule.forChild(eoxLoginRoutes)],
    exports: [RouterModule]
})
export class LoginRoutingModule { }