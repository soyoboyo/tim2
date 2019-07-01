import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, CanActivateChild, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login/login.service';

@Injectable({
	providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanActivateChild {

	constructor(private loginService: LoginService,
				private router: Router) {
	}

	canActivate(
		next: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		console.log('AuthGuard#canActivate called');
		return this.checkLogin(state.url);
	}

	canActivateChild(
		childRoute: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		console.log('AuthGuard#canActivateChild called');
		return undefined;
	}

	checkLogin(url: string): boolean {
		if (this.loginService.isLoggedIn) { return true; }

		// Store the attempted URL for redirecting
		// this.loginService.redirectUrl = url;

		// Navigate to the login page with extras
		this.router.navigate(['/forbidden']);
		return false;
	}
}
