import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevHistoryTranslationsComponent } from './dev-history-translations.component';

describe('DevHistoryTranslationsComponent', () => {
	let component: DevHistoryTranslationsComponent;
	let fixture: ComponentFixture<DevHistoryTranslationsComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [DevHistoryTranslationsComponent]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(DevHistoryTranslationsComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
