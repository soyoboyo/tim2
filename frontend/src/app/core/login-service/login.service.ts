import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { API_URL } from '../../app.config';
import { UserPrincipal } from "../../shared/types/UserPrincipal";

@Injectable({
	providedIn: 'root'
})
export class LoginService {
	isLoggedIn = false;
	constructor(private http: HttpClient,
				private router: Router) {
		this.loggedIn.next(false);
		this.getPrincipalFromAPI();
	}

	private loggedIn: BehaviorSubject<any> = new BehaviorSubject([]);
	private role: BehaviorSubject<any> = new BehaviorSubject([]);
	private username: BehaviorSubject<any> = new BehaviorSubject([]);



	getIsLoggedOk() {
		return this.loggedIn.asObservable();
	}

	getRole() {
		return this.role.asObservable();
	}

	getUsername() {
		return this.username.asObservable();
	}

	async getPrincipalFromAPI() {
		let isStatusOk = true;
		const response: any = await this.http.get<UserPrincipal>(API_URL + '/user/get', { withCredentials: true }).toPromise()
		.catch((e: HttpErrorResponse) => {
			isStatusOk = false;
			this.clearData();
			this.router.navigateByUrl('login');
		});
		const principal: UserPrincipal = response;
		if (isStatusOk && principal != null) {
			this.isLoggedIn = true;
			this.loggedIn.next(true);
			this.username.next(principal.username);
			this.role.next(principal.role);
			if (principal.role === 'ROLE_DEVELOPER') {
				this.router.navigateByUrl('developer');
			} else if (principal.role === 'ROLE_TRANSLATOR') {
				this.router.navigateByUrl('translator');
			}
		} else {
			this.clearData();
		}
	}

	async loginUser(username: string, password: string) {
		let body = new HttpParams();
		body = body.set('username', username);
		body = body.set('password', password);
		let isStatusOk = true;
		await this.http.post<any>(API_URL + '/login', body, { withCredentials: true }).toPromise()
		.catch((e: HttpErrorResponse) => {
			isStatusOk = false;
		});
		if (isStatusOk === true) {
			this.loggedIn.next(true);
			this.getPrincipalFromAPI();
			this.isLoggedIn = true;
		} else {
			this.clearData();
		}
		return isStatusOk;
	}

	async logoutUser() {
		let isStatusOk = true;
		await this.http.post<any>(API_URL + '/logout', null, { withCredentials: true }).toPromise()
		.catch((e: HttpErrorResponse) => {
			isStatusOk = false;
		});
		if (isStatusOk) {
			this.isLoggedIn = false;
			this.clearData();
			this.router.navigateByUrl('login');
		}
	}

	private clearData() {
		this.isLoggedIn = false;
		this.loggedIn.next(false);
		this.role.next('');
		this.username.next('Not logged in');
		sessionStorage.setItem('possibleSignIn', 'false');
	}
}
