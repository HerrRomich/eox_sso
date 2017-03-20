import { Injectable } from '@angular/core';
import {
    CanActivate, CanActivateChild, Router,
    ActivatedRouteSnapshot, RouterStateSnapshot,
    UrlTree, UrlSegmentGroup, 
    PRIMARY_OUTLET
} from '@angular/router';

import { AdminAuthenticationService } from './admin-authentication.service';

@Injectable()
export class AdminAuthenticationGuard implements CanActivate, CanActivateChild {

    constructor(private authService: AdminAuthenticationService, private router: Router) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string = state.url;
        let stateTree: UrlTree = this.router.parseUrl(state.url);
        let primarySegmentGroup: UrlSegmentGroup  = stateTree.root.children[PRIMARY_OUTLET];
        let appPath = primarySegmentGroup.segments[0].path;
        return this.checkLogin(url, appPath);
    }

    canActivateChild(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string = state.url;

        let appPath = '';//route.url[0].path;

        return this.checkLogin(url, appPath);
    }


    checkLogin(url: string, appPath: string): boolean {
        if (this.authService.isLoggedIn) { return true; }

        // Store the attempted URL for redirecting
        this.authService.redirectUrl = url;

        // Navigate to the login page with extras
        this.router.navigate(['/' + appPath + '/login']);
        return false;
    }
}
