import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';
import { OAUTH_URL } from '../app.config';
import { Router } from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

	constructor(private cookieService: CookieService,
				private router: Router) {
	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const token = this.cookieService.get('oauth_token');

		if (!token) {
			this.router.navigateByUrl('login');
		}

		if (req.url === OAUTH_URL) {
			return next.handle(req);
		}

		const req1 = req.clone({
			headers: req.headers.set('Authorization', `Bearer ${token}`)
		});

		return next.handle(req1);
	}
}
