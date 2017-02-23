import { NgModule } from '@angular/core'
import { SharedModule } from 'app/shared/shared.module'
import { LoginComponent } from './login.component'

@NgModule({
    imports: [SharedModule],
    declarations: [LoginComponent],
    entryComponents: [LoginComponent]
})
export class LoginModule {

}