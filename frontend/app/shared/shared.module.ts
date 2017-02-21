import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core'
import { TranslateHttpLoader } from '@ngx-translate/http-loader'
import { HttpModule, Http } from '@angular/http';

@NgModule({
    imports: [TranslateModule.forRoot({
        loader: {
            provide: TranslateLoader,
            useFactory: (http: Http) => new TranslateHttpLoader(http, '/app/shared/', '.json'),
            deps: [Http]
        }
    })],
    exports: [CommonModule, HttpModule, TranslateModule]
})
export class SharedModule { }