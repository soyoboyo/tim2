import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevProjectFormComponent } from './dev-project-form.component';

describe('DevProjectFormComponent', () => {
	let component: DevProjectFormComponent;
	let fixture: ComponentFixture<DevProjectFormComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [DevProjectFormComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(DevProjectFormComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
