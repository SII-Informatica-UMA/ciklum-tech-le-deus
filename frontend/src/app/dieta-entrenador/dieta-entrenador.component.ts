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
import { AppComponent } from '../app.component';
@Component({
  selector: 'app-dieta-entrenador',
  standalone: true,
  imports: [CommonModule, DetalleDietaComponent,AppComponent],
  templateUrl: './dieta-entrenador.component.html',
  styleUrl: './dieta-entrenador.component.css'
})
export class DietaEntrenadorComponent {
  dietas: Dieta [] = [];
  dietaElegida?: Dieta;
  
  //Comprobamos si es entrenador
  isTrainer(): boolean {
    const usuario = this.usuarioService.getUsuarioSesion();
    if (usuario) {
        return usuario.roles.some(rol => rol.rol === Rol.ENTRENADOR);
    }
    return false; 
  }


// Comprobamos si el usuario actual es un cliente
  isCliente(): boolean {
    const usuario = this.usuarioService.getUsuarioSesion();
    if(usuario){
      return usuario.roles.some(rol => rol.rol === Rol.CLIENTE);
    }
    return false; 
  }

  constructor(private dietaService: DietaService, private usuarioService: UsuariosService, private modalService: NgbModal){
      this.ngOnInit();
  }

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
  


  chooseDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  addDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.contacto = { nombre: '', descripcion: '', observaciones:'', objetivo:'', duracionDias: 0, 
    alimentos: [], recomendaciones:'', id: 0, usuarioId:0, creadorId:0};
    ref.result.then((dieta: Dieta) => {
      this.dietaService.crearDieta(dieta, dieta.usuarioId);
      this.ngOnInit();
    }, (reason) => {}) 
  }

  deleteDieta(dieta: Dieta): void {
    this.dietaService.eliminarDieta(dieta, dieta.usuarioId);
    this.ngOnInit();

    this.dietaElegida  = undefined;
  }

  editDieta(dieta: Dieta): void {
    this.dietaService.editarDieta(dieta, dieta.usuarioId);
    this.ngOnInit();
    
    this.dietaElegida = undefined;
  }
}
