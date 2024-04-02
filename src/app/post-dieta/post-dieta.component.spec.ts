import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PostDietaComponent } from './post-dieta.component';

describe('PostDietaComponent', () => {
  let component: PostDietaComponent;
  let fixture: ComponentFixture<PostDietaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PostDietaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(PostDietaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
