import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DietaSinSesionComponent } from './dieta-sin-sesion.component';

describe('DietaSinSesionComponent', () => {
  let component: DietaSinSesionComponent;
  let fixture: ComponentFixture<DietaSinSesionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DietaSinSesionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DietaSinSesionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
