import { NgModule } from '@angular/core';
import { BrowserModule, Title } from '@angular/platform-browser';
import { Http } from '@angular/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core'
import { TranslateHttpLoader } from '@ngx-translate/http-loader'

import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';

@NgModule({
  imports: [BrowserModule, SharedModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [Title]
})
export class AppModule {
}
