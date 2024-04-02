import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GetDietaComponent } from './get-dieta.component';

describe('GetDietaComponent', () => {
  let component: GetDietaComponent;
  let fixture: ComponentFixture<GetDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GetDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GetDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
