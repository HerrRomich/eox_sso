import { AuthenticationService } from '../authentication.service';
import { Authentication } from '../authentication';
import { AdminAuthentication } from './admin-authentication';

export class AdminAuthenticationService extends AuthenticationService {

    private _isLoggedIn: boolean;
    get isLoggedIn() {
        return this._isLoggedIn;
    }

    login(authentication :Authentication): void {
        let adminAuthentication: AdminAuthentication = <AdminAuthentication>authentication;

        this._isLoggedIn = true;
    }

    logout(): void {
        this._isLoggedIn = false;
    }
    
}