import { Component, OnInit, ViewChild } from '@angular/core';
import { MatSidenav } from '@angular/material/sidenav';
import { NavbarService } from './shared/services/navbar-service/navbar.service';
import { TranslateService } from "@ngx-translate/core";

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

		let savedLanguage = localStorage.getItem('selectedLanguage');
		if (savedLanguage != null) {
			translateService.use(savedLanguage);
		}
	}

	ngOnInit(): void {
		this.sidenavService.setSidenav(this.sidenav);
	}

}
