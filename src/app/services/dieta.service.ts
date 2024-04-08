import { Injectable } from '@angular/core';
import { Dieta } from '../entities/dieta';
import { UsuarioSesion } from '../entities/login';
import { BackendFakeService } from "./backend.fake.service";
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class DietaService {

  constructor(private backend: BackendFakeService) { }

  getDietas(): Observable<Dieta[]>{
    return this.backend.getDietas();
  }
  eliminarDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    return this.backend.eliminarDieta(dieta, usuarioId);
  }

  editarDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    return this.backend.editarDieta(dieta, usuarioId);
  }

  crearDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    return this.backend.crearDieta(dieta, usuarioId);
  }

  getDietasPorUsuario(usuarioId: number): Observable<Dieta[]> {
    return this.backend.getDietasPorUsuario(usuarioId);
  }
  getDietasPorCreador(usuarioId: number): Observable<Dieta[]> {
    return this.backend.getDietasPorCreador(usuarioId);
  }
}
