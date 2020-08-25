import { inject, TestBed } from '@angular/core/testing';

import { AuthTranGuard } from './auth-tran.guard';

describe('AuthTranGuard', () => {
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [AuthTranGuard]
		});
	});

	it('should ...', inject([AuthTranGuard], (guard: AuthTranGuard) => {
		expect(guard).toBeTruthy();
	}));
});
