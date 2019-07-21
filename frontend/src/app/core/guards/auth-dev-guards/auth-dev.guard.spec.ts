import { TestBed, async, inject } from '@angular/core/testing';

import { AuthDevGuard } from './auth-dev.guard';

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
