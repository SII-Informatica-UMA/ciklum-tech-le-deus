import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DietaEntrenadorComponent } from './dieta-entrenador.component';

describe('DietaEntrenadorComponent', () => {
  let component: DietaEntrenadorComponent;
  let fixture: ComponentFixture<DietaEntrenadorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DietaEntrenadorComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DietaEntrenadorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
