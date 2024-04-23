import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleDietaClienteComponent } from './detalle-dieta-cliente.component';

describe('DetalleDietaClienteComponent', () => {
  let component: DetalleDietaClienteComponent;
  let fixture: ComponentFixture<DetalleDietaClienteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleDietaClienteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetalleDietaClienteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
