import { Component, Input } from '@angular/core';
import { Dieta } from '../entities/dieta';

@Component({
  selector: 'app-detalle-dieta-cliente',
  standalone: true,
  imports: [],
  templateUrl: './detalle-dieta-cliente.component.html',
  styleUrl: './detalle-dieta-cliente.component.css'
})
export class DetalleDietaClienteComponent {
  @Input() dieta?: Dieta;

  constructor() {
  }
}
