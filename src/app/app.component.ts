// ya venia importado
import { Component } from '@angular/core';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterOutlet, RouterLink, Router } from '@angular/router';

//lo he importado yo
import { UsuariosService } from './services/usuarios.service';
import { NgbNavModule, NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap'; //lo importamos para crear pestañas de navegacion mas facilmente con el framework bootstrap
import { Rol } from './entities/login'; //entities ya venia creada con el login y usuario
import { InicioComponent } from './inicio/inicio.component'; //para poder usar la carpeta inicio
import { DietaSinSesionComponent } from './dieta-sin-sesion/dieta-sin-sesion.component'; //pagina para los usuarios que intenten entrar en dietas sin iniciar sesion

@Component({
  selector: 'app-root',
  standalone: true,

  //importamos las cosas que vayamos a usar en app.component.html
  imports: [RouterOutlet, CommonModule, NgbNavModule, NgbDropdownModule, InicioComponent, DietaSinSesionComponent,
    RouterLink, FormsModule, TitleCasePipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {

  //lo he añadido
  title = 'TechLeDeus';

  //ya venia asi
  _rolIndex: number = 0

  //inicializamos active a 1, ya que el 1 simboliza la pestaña inicio(mirar app.component.html) 
  active = 1;

  //añadimos boolean mostrarVentana para usarlo en app.component.html y mostrar una ventana de login cuando se pulse
  //mirar en app.componet.ts el metodo mostrarLogin(). Lo inicializamos a false.
  mostrarVentana: boolean = false;

  //ya venia asi
  constructor(private usuarioService: UsuariosService, private router: Router) {
    this.actualizarRol()
  }

  //ya venia asi
  get rolIndex() {
    return this._rolIndex;
  }

  //ya venia asi
  set rolIndex(i: number) {
    this._rolIndex = i;
    this.actualizarRol();
  }

  //ya venia asi
  actualizarRol() {
    let u = this.usuarioSesion;
    if (u) {
      this.usuarioService.rolCentro = u.roles[this.rolIndex];
    } else {
      this.usuarioService.rolCentro = undefined;
    }
  }

  //ya venia asi
  get rol() {
    return this.usuarioService.rolCentro;
  }

  //ya venia asi
  get usuarioSesion() {
    return this.usuarioService.getUsuarioSesion();
  }

  //añadimos mostrarLogin para usarlo en app.component.html
  mostrarLogin() {
    this.mostrarVentana = true;
  }

  //ya venia asi
  logout() {
    this.usuarioService.doLogout();
    this.actualizarRol();
    this.router.navigateByUrl('/login');
  }

  //añadimos este metodo para saber si un usuario es admin o no y usarlo en app.componet.html
  isAdmin() : boolean {
    return this.usuarioService._rolCentro?.rol == Rol.ADMINISTRADOR;
  }
}
