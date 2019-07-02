import { Component, OnInit } from '@angular/core';
import { NavbarService } from '../../shared/services/navbar-service/navbar.service';
import { LoginService } from '../../core/login-service/login.service';
import { Router } from '@angular/router';

@Component({
	selector: 'app-navbar',
	templateUrl: './navbar.component.html',
	styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
	isAuth = false;

	constructor(private navbarService: NavbarService,
				private loginService: LoginService,
				private router: Router) {
	}

	isLoggedIn = false;
	username = 'Not logged in';
	role = '';

	ngOnInit() {
		this.loginService.getIsLoggedOk().subscribe(isLoggedIn => {
			this.isLoggedIn = isLoggedIn;
		});
		this.loginService.getRole().subscribe(role => {
			this.role = role;
		});
		this.loginService.getUsername().subscribe(username => {
			this.username = username;
		});

	}

	gotToHomepage() {
		if (this.role === 'ROLE_DEVELOPER') {
			this.router.navigateByUrl('developer');
		} else if (this.role === 'ROLE_TRANSLATOR') {
			this.router.navigateByUrl('translator');
		} else {
			this.router.navigateByUrl('homepage');
		}
	}

	signInButtonClicked() {
		this.router.navigateByUrl('login');
	}

	logoutButtonClicked() {
		this.loginService.logoutUser();
	}
}
