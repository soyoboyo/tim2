import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevMessagesComponent } from './dev-messages.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AppModule } from '../../../app.module';

describe('DevMessagesComponent', () => {
	let component: DevMessagesComponent;
	let fixture: ComponentFixture<DevMessagesComponent>;

	beforeEach(async(() => {
		TestBed.configureTestingModule({
			declarations: [DevMessagesComponent],
			imports: [HttpClientTestingModule, AppModule]
		})
		.compileComponents();
	}));

	beforeEach(() => {
		fixture = TestBed.createComponent(DevMessagesComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
