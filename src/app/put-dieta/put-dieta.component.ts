import { Component } from '@angular/core';
import { Asignacion } from '../dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-put-dieta',
  templateUrl: './put-dieta.component.html',
  styleUrls: ['./put-dieta.component.css']
})
export class PutDietaComponent {
  accion: "PUT" | undefined;
  idCliente?: Number;
  asignaciones: Asignacion[] = [];
  errorMessage?: Error;

  constructor(public modal: NgbActiveModal) { }

  asignarCorrector(): void {
    if (this.idCorrector && this.idExamen) {
      let asignacion: Asignacion = { idCorrector: this.idCorrector, idExamen: this.idExamen };
      this.asignaciones.push(asignacion);
    } else {
      this.errorMessage = new Error("Campo/s vacío/s");
      alert(this.errorMessage);
    }
  }

  guardarAsignaciones(): void {
    this.modal.close(this.asignaciones);
  }

}


