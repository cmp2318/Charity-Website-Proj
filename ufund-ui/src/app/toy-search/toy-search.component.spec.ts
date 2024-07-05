import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ToySearchComponent } from './toy-search.component';

describe('ToySearchComponent', () => {
  let component: ToySearchComponent;
  let fixture: ComponentFixture<ToySearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ToySearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ToySearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
