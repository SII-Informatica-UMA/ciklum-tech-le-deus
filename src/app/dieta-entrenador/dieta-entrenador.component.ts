import { Component } from '@angular/core';
import { UsuariosService } from '../services/usuarios.service';
import { DietaService } from '../services/dieta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { Usuario, UsuarioImpl } from '../entities/usuario';
import { Rol } from '../entities/login';
import { FormularioDietaComponent } from '../formulario-dieta/formulario-dieta.component';
import { FormularioUsuarioComponent } from '../formulario-usuario/formulario-usuario.component';
import { Dieta } from '../entities/dieta';
import { DetalleDietaComponent } from '../detalle-dieta/detalle-dieta.component';
@Component({
  selector: 'app-dieta-entrenador',
  standalone: true,
  imports: [CommonModule, DetalleDietaComponent],
  templateUrl: './dieta-entrenador.component.html',
  styleUrl: './dieta-entrenador.component.css'
})
export class DietaEntrenadorComponent {
  dietas: Dieta [] = [];
  dietaElegida?: Dieta;
  constructor(private dietaService: DietaService, private usuarioService: UsuariosService, private modalService: NgbModal){
      this.ngOnInit();
  }

  /*
   ngOnInit(): void {
    this.dietaService.getDietas().subscribe(dietas => {
      this.dietas = dietas;
    });
  }
  */

  ngOnInit(): void {
    this.usuarioService.getUsuarioSesionObservable().subscribe(usuarioSesion => {
      if (usuarioSesion) {
        const usuarioId = usuarioSesion.id;
        this.dietaService.getDietasPorCreador(usuarioId).subscribe(dietas => {
          this.dietas = dietas;
        });
      } else {
        console.error('No se ha encontrado un usuario autenticado.');
      }
    });
  }
  


  elegirDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  aniadirDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.contacto = { nombre: '', descripcion: '', observaciones:'', objetivo:'', duracionDias: 0, 
    alimentos: [], recomendaciones:'', id: 0, usuarioId:0, creadorId:0};
    ref.result.then((dieta: Dieta) => {
      this.dietaService.crearDieta(dieta, dieta.usuarioId);
      this.ngOnInit();
    }, (reason) => {}) 
  }

  eliminarDieta(dieta: Dieta): void {
    this.dietaService.eliminarDieta(dieta, dieta.usuarioId);
    this.ngOnInit();
    this.dietaElegida = undefined;
  }
  editarDieta(dieta: Dieta): void {
    this.dietaService.editarDieta(dieta, dieta.usuarioId);
    this.ngOnInit();
    this.dietaElegida = undefined;
  }
}
