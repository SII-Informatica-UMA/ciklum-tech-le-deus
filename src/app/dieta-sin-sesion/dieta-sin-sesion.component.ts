//ya venia importado
import { Component } from '@angular/core';

//lo he importado yo
import { CommonModule, TitleCasePipe } from '@angular/common';
import { UsuariosService } from '../services/usuarios.service'; //ya venia implementado

@Component({
  selector: 'app-dieta-sin-sesion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dieta-sin-sesion.component.html',
  styleUrl: './dieta-sin-sesion.component.css'
})
export class DietaSinSesionComponent {

  //lo he añadido yo
  constructor(private usuarioService: UsuariosService){

  }

  //lo he añadido yo
  get sesionIniciada() {
    return this.usuarioService.getUsuarioSesion();
  }

}
