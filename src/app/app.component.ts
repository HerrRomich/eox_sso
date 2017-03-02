import { Component, OnInit } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { TranslateService } from '@ngx-translate/core'

@Component({
    moduleId: module.id,
    selector: 'my-app',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

    constructor(private titleService: Title, private translateService: TranslateService) {
    }

    ngOnInit() {
        this.translateService.use('de');
        this.translateService.get('EOX.TITLE').subscribe((result: string) => {
            this.titleService.setTitle(result);
        });
    }
}
