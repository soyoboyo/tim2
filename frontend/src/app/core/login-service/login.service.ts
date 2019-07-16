import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {BehaviorSubject, interval, Subscription} from 'rxjs';
import { API_URL, OAUTH_URL, APP_CREDENTIAL } from '../../app.config';
import { UserPrincipal } from '../../shared/types/UserPrincipal';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
	providedIn: 'root'
})
export class LoginService {
	isLoggedIn = false;

  subscription: Subscription;

  httpOptionsForLogin = {
    headers: new HttpHeaders({
      'Authorization': 'Basic ' + btoa(APP_CREDENTIAL)
    })
  };

	constructor(private http: HttpClient,
              private router: Router,
              private cookieService: CookieService) {
    const source = interval(1000 * 60 * 5);
	  this.subscription = source.subscribe(() => this.refreshToken());

		this.loggedIn.next(false);
		this.getPrincipalFromAPI();
	}

  ngOnDestroy() {
    this.subscription.unsubscribe();
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

	async refreshToken() {
	  const oauthToken = this.cookieService.get('refresh_token');
    this.loginUser(null, null, oauthToken);
  }

	async getPrincipalFromAPI() {
		let isStatusOk = true;
		const response: any = await this.http.get<UserPrincipal>(API_URL + '/user/get', {withCredentials: true}).toPromise()
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

	async loginUser(username: string, password: string, token: string) {
    let isStatusOk = true;
	  let body = new HttpParams();
    let principal;
    if (token === null) {
      body = body.set('username', username);
      body = body.set('password', password);
      body = body.set('grant_type', 'password');
      principal = await this.http.post<any>(OAUTH_URL, body, this.httpOptionsForLogin).toPromise()
        .catch((e: HttpErrorResponse) => {
          isStatusOk = false;
        });
    } else {
      body = body.set('grant_type', 'refresh_token');
      principal = await this.http.post<any>(OAUTH_URL, body).toPromise()
        .catch((e: HttpErrorResponse) => {
          isStatusOk = false;
        });
    }
		if (isStatusOk === true) {
		  const oauthToken = principal.access_token;
		  const refreshToken = principal.refresh_token;
		  const expiresIn = principal.expires_in;
		  this.cookieService.set('oauth_token', oauthToken);
		  this.cookieService.set('refresh_token', refreshToken);
		  sessionStorage.setItem('token_expires_in', expiresIn);
		  this.loggedIn.next(true);
		  this.getPrincipalFromAPI();
		  this.isLoggedIn = true;
		} else {
			this.clearData();
		}
		return isStatusOk;
	}

	async logoutUser() {
	  this.cookieService.delete('oauth_token');
    this.cookieService.delete('refresh_token');
    sessionStorage.clear();
    this.isLoggedIn = false;
    this.clearData();
	  this.router.navigateByUrl('login');
	}

	private clearData() {
		this.isLoggedIn = false;
		this.loggedIn.next(false);
		this.role.next('');
		this.username.next('Not logged in');
		sessionStorage.setItem('possibleSignIn', 'false');
		this.logoutUser();
	}
}
