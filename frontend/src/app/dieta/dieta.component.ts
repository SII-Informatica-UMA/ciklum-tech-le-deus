import { UsuarioSesion } from "./../entities/login";
import { Component } from "@angular/core";
import { UsuariosService } from "../services/usuarios.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CommonModule } from "@angular/common";
import { Usuario, UsuarioImpl } from "../entities/usuario";
import { Rol } from "../entities/login";
import { FormularioUsuarioComponent } from "../formulario-usuario/formulario-usuario.component";
import { RouterLink } from "@angular/router";
import { Router } from "@angular/router";

@Component({
  selector: "app-dieta",
  standalone: true,
  imports: [RouterLink],
  templateUrl: "./dieta.component.html",
  styleUrl: "./dieta.component.css",
})
export class DietaComponent {}
