import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TraMessagesTableComponent } from './tra-messages-table.component';

describe('TraMessagesTableComponent', () => {
	let component: TraMessagesTableComponent;
	let fixture: ComponentFixture<TraMessagesTableComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [TraMessagesTableComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(TraMessagesTableComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
