import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PutDietaComponent } from './put-dieta.component';

describe('PutDietaComponent', () => {
  let component: PutDietaComponent;
  let fixture: ComponentFixture<PutDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PutDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PutDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
