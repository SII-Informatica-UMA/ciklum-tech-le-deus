import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioDietaComponent } from './formulario-dieta.component';

describe('FormularioDietaComponent', () => {
  let component: FormularioDietaComponent;
  let fixture: ComponentFixture<FormularioDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
