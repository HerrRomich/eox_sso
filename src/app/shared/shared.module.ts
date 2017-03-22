import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core'
import { HttpModule, RequestOptions } from '@angular/http';
import { RouterModule } from '@angular/router';
import { FlexLayoutModule } from '@angular/flex-layout';
import { MaterialModule } from '@angular/material';
import { FormsModule } from '@angular/forms';
import { CookieService } from 'angular2-cookie/services';

import { DefaultRequestOptions } from './default-request-options';

@NgModule({
    imports: [TranslateModule, RouterModule, FlexLayoutModule, MaterialModule, FormsModule],
    exports: [CommonModule, HttpModule, TranslateModule, RouterModule, MaterialModule, FlexLayoutModule, FormsModule],
    providers: [CookieService, { provide: RequestOptions, useClass: DefaultRequestOptions }]
})
export class SharedModule { }