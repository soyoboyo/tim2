import { inject, TestBed } from '@angular/core/testing';

import { AuthDevGuard } from './auth-tran.guard';

describe('AuthDevGuard', () => {
	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [AuthDevGuard]
		});
	});

	it('should ...', inject([AuthDevGuard], (guard: AuthDevGuard) => {
		expect(guard).toBeTruthy();
	}));
});
