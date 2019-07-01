import { Component, Input, OnInit } from '@angular/core';

@Component({
	selector: 'app-nothing-found',
	templateUrl: './nothing-found.component.html',
	styleUrls: ['./nothing-found.component.scss']
})
export class NothingFoundComponent implements OnInit {

	@Input() nothingFound: boolean;
	@Input() message: string;

	constructor() {
	}

	ngOnInit() {
	}

}
