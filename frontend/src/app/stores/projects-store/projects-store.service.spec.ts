import { TestBed } from '@angular/core/testing';

import { ProjectsStoreService } from './projects-store.service';

describe('ProjectsStoreService', () => {
	beforeEach(() => TestBed.configureTestingModule({}));

	it('should be created', () => {
		const service: ProjectsStoreService = TestBed.get(ProjectsStoreService);
		expect(service).toBeTruthy();
	});
});
