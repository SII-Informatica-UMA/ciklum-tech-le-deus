import { Component } from '@angular/core';
import { UsuariosService } from '../services/usuarios.service';
import { DietaService } from '../services/dieta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { Dieta } from '../entities/dieta';
import { DetalleDietaClienteComponent } from '../detalle-dieta-cliente/detalle-dieta-cliente.component';

@Component({
  selector: 'app-dieta-usuario',
  standalone: true,
  imports: [DetalleDietaClienteComponent,CommonModule],
  templateUrl: './dieta-usuario.component.html',
  styleUrl: './dieta-usuario.component.css'
})
export class DietaUsuarioComponent {
  dietas: Dieta [] = [];
  dietaElegida?: Dieta;
  constructor(private dietaService: DietaService, private usuarioService: UsuariosService, private modalService: NgbModal){
      this.ngOnInit();
  }

  ngOnInit() : void {
    this.usuarioService.getUsuarioSesionObservable().subscribe(usuarioSesion => {
      if (usuarioSesion) {
        const usuarioId = usuarioSesion.id;
        this.dietaService.getDietasPorUsuario(usuarioId).subscribe(dietas => {
          
          this.dietas = dietas.concat(usuarioSesion.dietas);
           
        });
      } else {
        console.error('No se ha encontrado un usuario autenticado.');
      }
    });
  }

  chooseDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  get nombre(){
    return this.usuarioService.getUsuarioSesion()?.nombre;
  }

}
