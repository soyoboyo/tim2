import {Component, OnInit, ViewChild} from '@angular/core';
import { MatSidenav } from '@angular/material';
import { NavbarService } from './shared/services/navbar-service/navbar.service';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	@ViewChild('sidenav', { static: true }) public sidenav: MatSidenav;

	constructor(private sidenavService: NavbarService) {
	}

	ngOnInit(): void {
		this.sidenavService.setSidenav(this.sidenav);
	}

}
