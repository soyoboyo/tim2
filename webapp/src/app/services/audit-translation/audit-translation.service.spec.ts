import { TestBed } from '@angular/core/testing';

import { AuditTranslationService } from './audit-translation.service';

describe('AuditTranslationService', () => {
	beforeEach(() => TestBed.configureTestingModule({}));

	it('should be created', () => {
		const service: AuditTranslationService = TestBed.get(AuditTranslationService);
		expect(service).toBeTruthy();
	});
});
