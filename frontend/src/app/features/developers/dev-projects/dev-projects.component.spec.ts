import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevProjectsComponent } from './dev-projects.component';

describe('DevProjectsComponent', () => {
	let component: DevProjectsComponent;
	let fixture: ComponentFixture<DevProjectsComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [DevProjectsComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(DevProjectsComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
