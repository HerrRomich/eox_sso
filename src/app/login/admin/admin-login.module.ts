import { NgModule } from '@angular/core'

import { SharedModule } from 'app/shared/shared.module'
import { AdminLoginComponent } from './admin-login.component'

@NgModule({
    imports: [SharedModule],
    declarations: [AdminLoginComponent],
    exports: [AdminLoginComponent]
})
export class AdminLoginModule {
}