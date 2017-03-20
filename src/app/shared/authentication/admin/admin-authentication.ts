import { Authentication } from '../authentication';

export class AdminAuthentication implements Authentication {
    userName: string;
    password: string;
}