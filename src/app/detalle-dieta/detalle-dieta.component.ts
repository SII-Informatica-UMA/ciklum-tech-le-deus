import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Dieta } from '../entities/dieta';
import { UsuariosService } from '../services/usuarios.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioDietaComponent } from '../formulario-dieta/formulario-dieta.component';
@Component({
  selector: 'app-detalle-dieta',
  standalone: true,
  imports: [],
  templateUrl: './detalle-dieta.component.html',
  styleUrl: './detalle-dieta.component.css'
})
export class DetalleDietaComponent {
  @Input() dieta?: Dieta;
  @Output() dietaEditado = new EventEmitter<Dieta>();
  @Output() dietaEliminado = new EventEmitter<Dieta>();
  constructor(private usuarioService: UsuariosService, private modalService: NgbModal) {
  }
  editarDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.dieta = {...this.dieta};
    ref.result.then((dieta: Dieta) => {
      this.dietaEditado.emit(dieta);
    });
  }
  eliminarDieta(): void {
    this.dietaEliminado.emit(this.dieta);
  }
}
