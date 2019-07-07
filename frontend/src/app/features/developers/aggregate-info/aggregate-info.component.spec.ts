import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AggregateInfoComponent } from './aggregate-info.component';

describe('AggregateInfoComponent', () => {
  let component: AggregateInfoComponent;
  let fixture: ComponentFixture<AggregateInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AggregateInfoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AggregateInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
