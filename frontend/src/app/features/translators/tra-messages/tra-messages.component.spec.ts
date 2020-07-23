import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TraMessagesComponent } from './tra-messages.component';

describe('TraMessagesComponent', () => {
	let component: TraMessagesComponent;
	let fixture: ComponentFixture<TraMessagesComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [TraMessagesComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(TraMessagesComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
