import { TestBed } from '@angular/core/testing';

import { AuditMessageService } from './audit-message.service';

describe('AuditMessageService', () => {
	beforeEach(() => TestBed.configureTestingModule({}));

	it('should be created', () => {
		const service: AuditMessageService = TestBed.get(AuditMessageService);
		expect(service).toBeTruthy();
	});
});
