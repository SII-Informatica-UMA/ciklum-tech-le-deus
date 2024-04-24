import { TestBed } from '@angular/core/testing';

import { DietaService } from './dieta.service';
import { Dieta } from '../entities/dieta';

describe('DietaService', () => {
  let service: DietaService;
  let dietas : Dieta[];

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DietaService);
    service.getDietas().subscribe(dietass => {
      dietas = dietass;
    })
  });

  it('debe  contener el atributo nombre', () => {
    expect(dietas[0].nombre).toBeDefined();
  });

  it('debe contener el atributo id', () => {
    expect(dietas[0].id).toBeDefined();
  });
  
  it('debe contener el atributo creadorId', () => {
    expect(dietas[0].creadorId).toBeDefined();
  });
});
