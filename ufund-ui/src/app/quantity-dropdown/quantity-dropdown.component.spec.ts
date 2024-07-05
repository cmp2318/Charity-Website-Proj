import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuantityDropdownComponent } from './quantity-dropdown.component';

describe('QuantityDropdownComponent', () => {
  let component: QuantityDropdownComponent;
  let fixture: ComponentFixture<QuantityDropdownComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ QuantityDropdownComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuantityDropdownComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
