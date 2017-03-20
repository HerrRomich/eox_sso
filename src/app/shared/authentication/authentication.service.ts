import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Authentication } from './authentication';

@Injectable()
export abstract class AuthenticationService {

    public abstract get isLoggedIn() : boolean;
    public redirectUrl: string;

    constructor() {

    }

    abstract login(authentication: Authentication): void;

    abstract logout(): void;

}