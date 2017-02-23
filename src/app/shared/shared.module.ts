import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateLoader, TranslateService } from '@ngx-translate/core'
import { HttpModule, Http, Response } from '@angular/http';
import { RouterModule } from '@angular/router';

import "rxjs/add/operator/map";
import "rxjs/add/operator/catch";
import { Observable } from "rxjs/Observable"

@NgModule({
    imports: [TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (http: Http) => new TranslateHttpLoader(http, '/app/shared/', '.json'),
            deps: [Http]
        },

    }), RouterModule],
    exports: [CommonModule, HttpModule, TranslateModule, RouterModule]
})
export class SharedModule { }

export class TranslateHttpLoader implements TranslateLoader {
    constructor(private http: Http, private prefix: string = "/assets/i18n/", private suffix: string = ".json") { }

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