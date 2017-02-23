import { NgModule } from '@angular/core'
import { BrowserModule, Title } from '@angular/platform-browser'

import { AppComponent } from './app.component'
import { SharedModule } from './shared/shared.module'
import { AppRoutingModule } from 'app/app-routing.module'
import { LoginModule } from 'app/login/login.module'

@NgModule({
  imports: [BrowserModule, SharedModule, AppRoutingModule, LoginModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [Title]
})
export class AppModule {
}
