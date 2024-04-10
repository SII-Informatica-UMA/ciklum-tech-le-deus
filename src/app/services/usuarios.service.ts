import { Injectable } from "@angular/core";
import { Login, UsuarioSesion, Rol, RolCentro } from "../entities/login";
import { Observable, of, forkJoin, concatMap, lastValueFrom } from "rxjs";
import {map} from 'rxjs/operators';
import * as jose from 'jose';

import { Usuario } from "../entities/usuario";
import { BackendFakeService } from "./backend.fake.service";
import { BackendService } from "./backend.service";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  _rolCentro?: RolCentro;

  constructor(private backend: BackendFakeService) {}

  doLogin(login: Login): Observable<UsuarioSesion> {
    let jwtObs = this.backend.login(login.email, login.password);
    
    let usuarioObs = jwtObs.pipe(concatMap(jwt => this.backend.getUsuario(this.getUsuarioIdFromJwt(jwt))));
    
    let join = forkJoin({ jwt: jwtObs, usuario: usuarioObs });
  
    let usuarioSesion = join.pipe(map(obj => {
      return {
        id: obj.usuario.id,
        nombre: obj.usuario.nombre,
        apellido1: obj.usuario.apellido1,
        apellido2: obj.usuario.apellido2,
        email: obj.usuario.email,
        roles: obj.usuario.administrador ? [{rol: Rol.ADMINISTRADOR}] : [],
        jwt: obj.jwt,
        dietas: obj.usuario.dietas
      };
    }));
  
    usuarioSesion = usuarioSesion.pipe(concatMap(usuarioSesion => this.completarConRoles(usuarioSesion)));
  
    usuarioSesion = usuarioSesion.pipe(map(usuarioSesion => {
      localStorage.setItem('usuario', JSON.stringify(usuarioSesion));
      if (usuarioSesion.roles.length > 0) {
        this.rolCentro = usuarioSesion.roles[0];
      } else {
        this.rolCentro = undefined;
      }
      return usuarioSesion;
    }));
  
    return usuarioSesion;
  }
  
  
  private completarConRoles(usuarioSesion: UsuarioSesion): Observable<UsuarioSesion> {
    
    if (usuarioSesion.id === 2 || usuarioSesion.id === 3) {

      usuarioSesion.roles.push({ rol: Rol.ENTRENADOR });
    } else if (usuarioSesion.id === 4 || usuarioSesion.id === 5) {
      
      usuarioSesion.roles.push({ rol: Rol.CLIENTE });
    }
   
    return of(usuarioSesion);
  }
  
  getUsuariosConRolCliente(): Observable<Usuario[]> {
    const usuariosCliente: Usuario[] = [
      {
        id: 4,
        nombre: 'Jaime',
        apellido1: 'Jimenez',
        apellido2: 'Santos',
        email: 'jaime@uma.es',
        administrador: false,
        password: '1234',
        dietas: []
      },
      {
        id: 5,
        nombre: 'Reda',
        apellido1: 'Hassoun',
        apellido2: 'Toujgani',
        email: 'reda@uma.es',
        administrador: false,
        password: '1234',
        dietas: []
      }
    ];
  
    return of(usuariosCliente);
  }
  

  private getUsuarioIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: "+JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  get rolCentro(): RolCentro | undefined {
    return this._rolCentro;
  }

  set rolCentro(r: RolCentro | undefined) {
    this._rolCentro = r;
  }

  getUsuarioSesion(): UsuarioSesion | undefined {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : undefined;
  }
  //*
  getUsuarioSesionObservable(): Observable<UsuarioSesion | undefined> {
    const usuario = localStorage.getItem('usuario');
    return of(usuario ? JSON.parse(usuario) : undefined);
  }

  doLogout() {
    localStorage.removeItem('usuario');
  }

  doForgottenPassword(email: string): Observable<void> {
    return this.backend.forgottenPassword(email);
  }

  doCambiarContrasenia(password: string, token: string): Promise<void> {
    return lastValueFrom(this.backend.resetPassword(token, password),{defaultValue:undefined});
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.backend.getUsuarios();
  }

  editarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.putUsuario(usuario);
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.backend.deleteUsuario(id);
  }

  aniadirUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.postUsuario(usuario);
  }



}