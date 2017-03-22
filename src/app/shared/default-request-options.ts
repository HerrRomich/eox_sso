import { Injectable } from '@angular/core';
import { BaseRequestOptions, RequestOptions, RequestOptionsArgs } from '@angular/http';
import { CookieService } from 'angular2-cookie/core';

@Injectable()
export class DefaultRequestOptions extends BaseRequestOptions {

    private static JSESSIONID_NAME: string = 'JSESSIONID';
    private static AUTHORIZATION: string = 'Authorization';

    constructor(private cookieService: CookieService) {
        super();
    }

    merge(options?: RequestOptionsArgs): RequestOptions {
        let newOptions = super.merge(options);

        let jSessionId = this.cookieService.get(DefaultRequestOptions.JSESSIONID_NAME);
        newOptions.headers.set(DefaultRequestOptions.JSESSIONID_NAME, jSessionId);

        let authorization = localStorage.getItem(DefaultRequestOptions.AUTHORIZATION);
        newOptions.headers.set(DefaultRequestOptions.AUTHORIZATION, authorization);

        return newOptions;
    }

}