import { Component, OnInit } from '@angular/core'
import { Router, Route } from '@angular/router'

import { AdminLoginComponent } from 'app/login/admin/admin-login.component'

@Component({
    moduleId: module.id,
    selector: 'eox-login',
    templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

    constructor(private router: Router) {

    }

    ngOnInit() {
        let routes = this.router.config;
        let loginRouteIndex = routes.findIndex((route: Route) => route.path === "login");
        if (loginRouteIndex !== -1) {
            routes[loginRouteIndex].children = [{
                path: '',
                component: AdminLoginComponent
            }]
        }

        this.router.resetConfig(routes);
    }
}