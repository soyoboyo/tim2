import { TestBed } from '@angular/core/testing';

import { MatPaginatorIntlService } from './mat-paginator-intl.service';

describe('MatPaginatorIntlService', () => {
	let service: MatPaginatorIntlService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(MatPaginatorIntlService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
