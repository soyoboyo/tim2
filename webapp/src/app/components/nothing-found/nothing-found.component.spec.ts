import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NothingFoundComponent } from './nothing-found.component';

describe('NothingFoundComponent', () => {
  let component: NothingFoundComponent;
  let fixture: ComponentFixture<NothingFoundComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NothingFoundComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NothingFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
