import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DevProjectsTableComponent } from './dev-projects-table.component';

describe('DevProjectsTableComponent', () => {
  let component: DevProjectsTableComponent;
  let fixture: ComponentFixture<DevProjectsTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DevProjectsTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DevProjectsTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
