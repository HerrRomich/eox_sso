import { Component } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { TranslateService } from '@ngx-translate/core'

@Component({
    selector: 'my-app',
    template: `
    <h1>{{'EOX.TITLE' | translate}}</h1>
  `
})
export class AppComponent {

    constructor(private titleService: Title, private translateService: TranslateService) {
        translateService.use('de');
        translateService.get('EOX.TITLE').subscribe((result: string) => {
            titleService.setTitle(result);
        });
    }
}
