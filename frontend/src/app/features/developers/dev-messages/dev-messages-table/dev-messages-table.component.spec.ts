import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevMessagesTableComponent } from './dev-messages-table.component';

describe('DevMessagesTableComponent', () => {
  let component: DevMessagesTableComponent;
  let fixture: ComponentFixture<DevMessagesTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevMessagesTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevMessagesTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
