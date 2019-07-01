import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevHistoryMessagesComponent } from './dev-history-messages.component';

describe('DevHistoryMessagesComponent', () => {
	let component: DevHistoryMessagesComponent;
	let fixture: ComponentFixture<DevHistoryMessagesComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [DevHistoryMessagesComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(DevHistoryMessagesComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
