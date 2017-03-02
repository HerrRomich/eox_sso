import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ComponentsModule } from 'app/components/components.module'
import { LoginComponent } from './login.component';

@NgModule({
    imports: [SharedModule, ComponentsModule],
    declarations: [LoginComponent]
})
export class LoginModule {
}