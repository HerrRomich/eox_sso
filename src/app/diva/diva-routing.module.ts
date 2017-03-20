import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { DivaStartPageComponent } from './diva-start-page.component';
import { AdminAuthenticationGuard } from 'app/shared/authentication/admin/admin-authentication-guard.service'
import { AdminAuthenticationModule } from 'app/shared/authentication/admin/admin-authentication.module';
import { AdminLoginComponent } from 'app/shared/authentication/admin/admin-login.component';

const divaRoutes: Routes = [{
    path: '',
    component: DivaStartPageComponent,
    children: [{
        path: "",
        canActivate: [AdminAuthenticationGuard],
    },
    {
        path: 'login',
        component: AdminLoginComponent,
    }]
}];

@NgModule({
    imports: [RouterModule.forChild(divaRoutes), AdminAuthenticationModule]
})
export class DivaRoutingModule {

}