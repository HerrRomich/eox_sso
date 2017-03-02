import { NgModule } from '@angular/core'
import { BrowserModule, Title } from '@angular/platform-browser'
import { MdSidenavModule } from '@angular/material'
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core'
import { HttpModule, Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Observable } from 'rxjs/Observable';
import 'hammerjs';


import { AppComponent } from './app.component'
import { SharedModule } from './shared/shared.module'
import { AppRoutingModule } from 'app/app-routing.module'
import { RoutesLoaderService } from 'app/routes-loader.service'

@NgModule({
  imports: [BrowserModule, SharedModule, AppRoutingModule, MdSidenavModule.forRoot(), TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (http: Http) => new TranslateHttpLoader(http, '/app/shared/', '.json'),
            deps: [Http]
        }
    })],
  declarations: [AppComponent],
  bootstrap: [AppComponent],
  providers: [Title, RoutesLoaderService]
})
export class AppModule {
}

export class TranslateHttpLoader implements TranslateLoader {
    constructor(private http: Http, private prefix: string, private suffix: string) { }

    /**
     * Gets the translations from the server
     * @param lang
     * @returns {any}
     */
    public getTranslation(lang: string): any {
        return this.http.get(`${this.prefix}${lang}${this.suffix}`)
            .map((res: Response) => res.json()).catch(res => Observable.of({}));
    }
}