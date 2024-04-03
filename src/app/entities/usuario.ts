export interface Usuario {
  id: number;
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  password: string;
  administrador: boolean;
  entrenador:boolean;
  cliente:boolean;
}

export class UsuarioImpl implements Usuario {
  id: number;
  nombre: string;
  apellido1: string;
  apellido2: string;
  email: string;
  password: string;
  administrador: boolean;
  entrenador:boolean;
  cliente:boolean;

  constructor() {
    this.id = 0;
    this.nombre = '';
    this.apellido1 = '';
    this.apellido2 = '';
    this.email = '';
    this.password = '';
    this.administrador = false;
    this.entrenador = false;
    this.cliente = false;
  }
}
