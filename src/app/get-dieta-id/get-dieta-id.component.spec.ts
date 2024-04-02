import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetDietaIdComponent } from './get-dieta-id.component';

describe('GetDietaIdComponent', () => {
  let component: GetDietaIdComponent;
  let fixture: ComponentFixture<GetDietaIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GetDietaIdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GetDietaIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
