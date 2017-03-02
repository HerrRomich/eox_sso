import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { ActivatedRouteSnapshot, RouterStateSnapshot, Router, Routes, CanActivate } from '@angular/router';
import 'rxjs/add/operator/map';

@Injectable()
export class RoutesLoaderService implements CanActivate {

  private isLoaded: boolean = false;

  constructor(private http: Http, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Promise<boolean> {
    return new Promise(resolve => {
      if (this.isLoaded) {
        // The routes have already been added. If we've hit this again, the route definitely doesn't exist.
        resolve(true);
        return;
      }

      this.http.get('app/app-routes.json').map((resp: Response) => resp.json()).subscribe((data: Routes) => {
        this.router.resetConfig(data);
        this.isLoaded = true;
        resolve(false);
        // Retry the original navigation request
        this.router.navigateByUrl(state.url);
      })
    });
  }

  resetRoutes() {
    this.isLoaded = false;
  }
}