import { NgModule } from '@angular/core';

import { AdminLoginComponent } from './admin-login.component';
import { AdminAuthenticationGuard } from './admin-authentication-guard.service';
import { AdminAuthenticationService } from './admin-authentication.service';
import { ComponentsModule } from 'app/components/components.module';
import { SharedModule } from '../../shared.module';

@NgModule({
    declarations: [AdminLoginComponent],
    imports: [ComponentsModule, SharedModule],
    providers: [AdminAuthenticationService, AdminAuthenticationGuard],
    exports: [AdminLoginComponent]
})
export class AdminAuthenticationModule { }