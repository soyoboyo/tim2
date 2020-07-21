import { TestBed } from '@angular/core/testing';

import { TranslatorFormStateService } from './translator-form-state.service';

describe('TranslatorFormStateService', () => {
	let service: TranslatorFormStateService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = TestBed.inject(TranslatorFormStateService);
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});
});
