import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginModule } from 'app/login/login.module'
import { LoginComponent } from 'app/login/login.component'

const eoxRoutes: Routes = [{
    path: '',
    component: LoginComponent
}];

@NgModule({
    imports: [RouterModule.forRoot(eoxRoutes), LoginModule],
    exports: [RouterModule]
})
export class AppRoutingModule { }