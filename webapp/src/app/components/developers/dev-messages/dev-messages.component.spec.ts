import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevMessagesComponent } from './dev-messages.component';

describe('DevMessagesComponent', () => {
    let component: DevMessagesComponent;
    let fixture: ComponentFixture<DevMessagesComponent>;

    beforeEach(async(() => {
        TestBed.configureTestingModule({
            declarations: [DevMessagesComponent]
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
