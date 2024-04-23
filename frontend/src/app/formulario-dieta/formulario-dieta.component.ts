import { Component } from '@angular/core';
import { Dieta } from '../entities/dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { UsuariosService } from '../services/usuarios.service';
import { Usuario } from '../entities/usuario';
@Component({
  selector: 'app-formulario-dieta',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './formulario-dieta.component.html',
  styleUrl: './formulario-dieta.component.css'
})
export class FormularioDietaComponent {
  constructor(public modal: NgbActiveModal, private ususarioService: UsuariosService) {}
  
  
  accion?: "Añadir" | "Editar";
  dieta : Dieta = { nombre: '', descripcion: '', observaciones:'', objetivo:'', duracionDias: 0, 
    alimentos: [], recomendaciones:'', id: 0, usuarioId:0, creadorId:this.ususarioService.getUsuarioSesion()?.id }
  
  
    ngOnInit(): void {
    }

  saveDieta(): void {
    this.modal.close(this.dieta);
  }

}
