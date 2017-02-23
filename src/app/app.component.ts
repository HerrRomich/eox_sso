import { Component, OnInit } from '@angular/core'
import { Title } from '@angular/platform-browser'
import { TranslateService } from '@ngx-translate/core'
import { Router } from '@angular/router'
import { Http, Response } from '@angular/http'

@Component({
    moduleId: module.id,
    selector: 'my-app',
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {

    constructor(private titleService: Title, private translateService: TranslateService, private router: Router, private http: Http) {
        translateService.use('de');
        translateService.get('EOX.TITLE').subscribe((result: string) => {
            titleService.setTitle(result);
        });
    }

    ngOnInit() {
        //this.http.get('/services/app/eox-routes').map((res: Response) => res.json()).subscribe()
    }
}
