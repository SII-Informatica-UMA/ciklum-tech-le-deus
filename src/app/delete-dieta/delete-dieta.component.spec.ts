import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteDietaComponent } from './delete-dieta.component';

describe('DeleteDietaComponent', () => {
  let component: DeleteDietaComponent;
  let fixture: ComponentFixture<DeleteDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeleteDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
