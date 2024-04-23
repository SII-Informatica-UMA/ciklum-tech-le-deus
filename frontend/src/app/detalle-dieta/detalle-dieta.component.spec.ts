import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetalleDietaComponent } from './detalle-dieta.component';

describe('DetalleDietaComponent', () => {
  let component: DetalleDietaComponent;
  let fixture: ComponentFixture<DetalleDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetalleDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetalleDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
