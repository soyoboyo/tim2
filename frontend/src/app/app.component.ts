import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { NavbarService } from './shared/services/navbar-service/navbar.service';
import { TranslateService } from '@ngx-translate/core';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	@ViewChild('sidenav', { static: true }) public sidenav: MatSidenav;

	constructor(private sidenavService: NavbarService,
				private translateService: TranslateService) {
		translateService.setDefaultLang('en_GB');

		const savedLanguage = localStorage.getItem('selectedLanguage');
		const localeRegexp = new RegExp('[a-z]{2}_[A-Z]{2}');
		if (savedLanguage.match(localeRegexp)) {
			translateService.use(savedLanguage);
		} else {
			localStorage.setItem('selectedLanguage', 'en_GB');
		}
	}

	ngOnInit(): void {
		this.sidenavService.setSidenav(this.sidenav);
	}
}
