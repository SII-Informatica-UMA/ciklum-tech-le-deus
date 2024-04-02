import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PutDietaIdComponent } from './put-dieta-id.component';

describe('PutDietaIdComponent', () => {
  let component: PutDietaIdComponent;
  let fixture: ComponentFixture<PutDietaIdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PutDietaIdComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PutDietaIdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
