import { Component, OnInit } from '@angular/core';

import { AuthenticationService } from 'app/shared/authentication/authentication.service';
import { AdminAuthenticationService } from 'app/shared/authentication/admin/admin-authentication.service';

@Component({
    moduleId: module.id,
    selector: 'eox-diva-start-page',
    templateUrl: 'diva-start-page.component.html',
    styleUrls: ['../themes/ebase/theme.css', 'diva-start-page.component.css'],
    providers: [ {provide: AuthenticationService, useClass: AdminAuthenticationService} ]
})
export class DivaStartPageComponent implements OnInit {
    loginText: string;

    constructor(authenticationService: AuthenticationService) { 
        
    }

    ngOnInit() { 
        this.loginText = 'Roman Smushkevich'
    }
}