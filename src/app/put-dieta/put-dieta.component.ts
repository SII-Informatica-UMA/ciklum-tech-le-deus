// put-dieta.component.ts
/*
import { Component } from '@angular/core';
import { ClienteService } from './cliente.service';
import { Dieta } from './dieta.model';

@Component({
  selector: 'app-put-dieta',
  templateUrl: './put-dieta.component.html',
  styleUrls: ['./put-dieta.component.css']
})
export class PutDietaComponent {
  clientes?: Cliente[]; // Array para almacenar clientes del entrenador
  selectedCliente: any; // Cliente seleccionado por el entrenador
  dieta: Dieta = new Dieta(); // Modelo para los detalles de la dieta

  constructor(private clienteService: ClienteService) {}

  ngOnInit() {
    // Obtener lista de clientes del entrenador
    this.clienteService.getClientesDelEntrenador().subscribe(
      (clientes) => {
        this.clientes = clientes;
      },
      (error) => {
        console.error('Error al obtener clientes:', error);
      }
    );
  }

  onSubmit() {
    // Verificar que se haya seleccionado un cliente
    if (!this.selectedCliente) {
      console.error('Debes seleccionar un cliente');
      return;
    }

    // Asociar la dieta al cliente seleccionado
    this.clienteService.asociarDietaACliente(this.selectedCliente.id, this.dieta).subscribe(
      (response) => {
        console.log('Dieta asociada correctamente');
        // Reiniciar formulario o realizar otra acción necesaria
      },
      (error) => {
        console.error('Error al asociar dieta:', error);
      }
    );
  }
}
*/