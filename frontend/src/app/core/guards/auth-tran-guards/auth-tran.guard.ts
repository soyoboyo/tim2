import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateChild, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../../login-service/login.service';

@Injectable({
	providedIn: 'root'
})
export class AuthTranGuard implements CanActivate, CanActivateChild {

	isLoggedOk = false;
	role = '';

	constructor(private loginService: LoginService,
				private router: Router) {
		this.loginService.getIsLoggedOk().subscribe(isLoggedIn => {
			this.isLoggedOk = isLoggedIn;
		});
		this.loginService.getRole().subscribe(role => {
			this.role = role;
		});
	}

	canActivate(
		next: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		return this.checkLogin(state.url);
	}

	canActivateChild(
		childRoute: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
		return undefined;
	}

	checkLogin(url: string): boolean {
		if (this.isLoggedOk && this.role === 'ROLE_TRANSLATOR') {
			return true;
		}

		// Store the attempted URL for redirecting
		// this.loginService.redirectUrl = url;

		// Navigate to the login page with extras
		this.router.navigate(['/forbidden']);
		return false;
	}
}
