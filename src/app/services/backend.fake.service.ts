import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { SECRET_JWT } from "../config/config";
import { from } from "rxjs";
import * as jose from 'jose';
import { FRONTEND_URI } from "../config/config";

//lo añado yo
import { Dieta } from "../entities/dieta";

//-----------------------------------------------------------------------------------------
//lo añado yo - REVISAR

const dietasC: Dieta [] = [

  {
    nombre: 'Antonio',
    descripcion: 'Poco calórica',
    observaciones: 'No superar limite de calorias',
    objetivo: 'Bajar peso',
    duracionDias: 30,
    alimentos: ['verduras', 'arroz'],
    recomendaciones: 'beber mucha agua',
    id: 1,
    usuarioId: 2,
    creadorId: 2
  },
  {
    nombre: 'Adam',
    descripcion: 'comer',
    observaciones: 'no comer frito',
    objetivo: 'ganar peso',
    duracionDias: 21,
    alimentos: ['pavo','pescado', 'pollo'],
    recomendaciones: 'comer sano',
    id: 1,
    usuarioId: 5,
    creadorId: 2
  },
]

//------------------------------------------------------------------------------------------
// Este servicio imita al backend pero utiliza localStorage para almacenar los datos

const usuariosC: Usuario [] = [
  {
    id: 1,
    nombre: 'Admin',
    apellido1: 'Admin',
    apellido2: 'Admin',
    email: 'admin@uma.es',
    administrador: true,
    password: 'admin', 
    dietas: [] //LO AÑADO YO
  },
  {
    id: 2,
    nombre: 'Antonio',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'antonio@uma.es',
    administrador: false,
    password: '5678',
    dietas: [] //LO AÑADO YO
  },
  {
    id: 3,
    nombre: 'Adam',
    apellido1: 'Ahmed',
    apellido2: 'Mohamed',
    email: 'adam@uma.es',
    administrador: false,
    password: '1234',
    dietas:[]
  },
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

@Injectable({
  providedIn: 'root'
})
export class BackendFakeService {
  private usuarios: Usuario [];
  private forgottenPasswordTokens;

  //lo añado yo
  private dietas: Dieta[]= [
    {
      nombre: 'Adam',
      descripcion: 'volumen',
      observaciones: '6 dias a la semana',
      objetivo: 'ganar peso',
      duracionDias: 25,
      alimentos: ['arroz', 'pollo'],
      recomendaciones: 'beber mucha agua',
      id: 1,
      usuarioId: 4,
      creadorId: 2
    },
    {
      nombre: 'Jaime',
      descripcion: 'baja en caloria',
      observaciones: '100 g proteinas',
      objetivo: '2 kilos en un mes',
      duracionDias: 30,
      alimentos: ['salchicha', 'pollo'],
      recomendaciones: 'correr',
      id: 1,
      usuarioId: 4,
      creadorId: 2
    },
  ];

  constructor() {
    let _usuarios = localStorage.getItem('usuarios');
    if (_usuarios) {
      this.usuarios = JSON.parse(_usuarios);
    } else {
      this.usuarios = [...usuariosC];
    }

    let _forgottenPasswordTokens = localStorage.getItem('forgottenPasswordTokens');
    if (_forgottenPasswordTokens) {
      this.forgottenPasswordTokens = new Map(JSON.parse(_forgottenPasswordTokens));
    } else {
      this.forgottenPasswordTokens = new Map();
    }

    //lo añado yo
    let storedDietas = localStorage.getItem('dietas');
    if(storedDietas){
      this.dietas = storedDietas ? JSON.parse(storedDietas) : [];
    } else {
      this.dietas = [...dietasC];
    }

  }

  //LO AÑADO YO
  eliminarDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    let i = this.dietas.findIndex(d => d.id === dieta.id);
    if (i < 0) {
      return new Observable<Dieta>(observer => {
        observer.error('Dieta no encontrada');
      });
    }
    this.dietas.splice(i, 1);
  
    let usuario = this.usuarios.find(u => u.id == usuarioId);
    if (usuario) {
      let indexDietaUsuario = usuario.dietas.findIndex(d => d.id == dieta.id);
      if (indexDietaUsuario !== -1) {
        usuario.dietas.splice(indexDietaUsuario, 1);
      }
    }
    this.guardarUsuariosEnLocalStorage();
    this.guardarDietasEnLocalStorage();
    return of(dieta);
  }

  editarDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    let i = this.dietas.findIndex(d => d.id == dieta.id);
    if (i < 0) {
      return new Observable<Dieta>(observer => {
        observer.error('Dieta no encontrada');
      });
    }
    this.dietas[i] = dieta;
  
    // Encuentra el usuario
    let usuario = this.usuarios.find(u => u.id == usuarioId);
    if (usuario) {
      // Encuentra la dieta en la lista de dietas del usuario
      let indexDietaUsuario = usuario.dietas.findIndex(d => d.id == dieta.id);
      if (indexDietaUsuario !== -1) {
        // Actualiza la dieta en la lista de dietas del usuario
        usuario.dietas[indexDietaUsuario] = dieta;
      }
    }
  
    this.guardarDietasEnLocalStorage();
    this.guardarUsuariosEnLocalStorage();
    return of(dieta);
  }
  getDietas(): Observable<Dieta[]> {
    return of(this.dietas);
  }
  //*
  getDietasPorUsuario(usuarioId: number): Observable<Dieta[]> {
    
    const dietasUsuario = this.dietas.filter(dieta => dieta.usuarioId === usuarioId);
    return of(dietasUsuario);
  }
  //*
  getDietasPorCreador(usuarioId: number): Observable<Dieta[]> {
    const dietasCreador = this.dietas.filter(dieta => dieta.creadorId === usuarioId);
    return of(dietasCreador);
  }
  
  crearDieta(dieta: Dieta, usuarioId: number): Observable<Dieta> {
    const usuario = this.usuarios.find(u => u.id == usuarioId);
    
    if (!usuario) {
      return new Observable<Dieta>(observer => {
        observer.error('Usuario no encontrado');
      });
    }

    dieta.id = this.dietas.length + 1; // Asignar un ID único a la nueva dieta
    dieta.usuarioId = usuarioId; // Asignar el ID del usuario a la dieta

    this.dietas.push(dieta); // Agregar la nueva dieta al arreglo de dietas
    usuario.dietas.push(dieta);
    this.guardarDietasEnLocalStorage(); // Guardar las dietas en el almacenamiento local
    return of(dieta); // Devolver la nueva dieta creada
  }
  private guardarDietasEnLocalStorage(): void {
    localStorage.setItem('dietas', JSON.stringify(this.dietas));
  }

  //--------------------------------------------------------------------------------------
  getUsuarios(): Observable<Usuario[]> {
    return of(this.usuarios);
  }

  postUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.email == usuario.email);
    if (!usuario.email) {
      return new Observable<Usuario>(observer => {
        observer.error('El email es obligatorio');
      });
    }
    if (u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario ya existe');
      });
    }
    // Si no trae contraseña generamos una aleatoria
    if (usuario.password.length == 0) {
      usuario.password = this.generarCadena();
    }

    usuario.id = this.usuarios.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    this.usuarios.push(usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(usuario);
  }

  private guardarUsuariosEnLocalStorage() {
    localStorage.setItem('usuarios', JSON.stringify(this.usuarios));
  }

  private guardarForgottenPasswordTokensEnLocalStorage() {
    localStorage.setItem('forgottenPasswordTokens', JSON.stringify(Array.from(this.forgottenPasswordTokens.entries())));
  }

  putUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == usuario.id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    // Si la contraseña está en blanco mantenemos la que ya tiene
    if (usuario.password.length == 0) {
      usuario.password = u.password;
    }

    Object.assign(u, usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(u);
  }

  deleteUsuario(id: number): Observable<void> {
    let i = this.usuarios.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('El usuario no existe');
      });
    }
    this.usuarios.splice(i, 1);
    this.guardarUsuariosEnLocalStorage();
    return of();
  }

  getUsuario(id: number): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    return of(u);
  }

  login(email: string, password: string): Observable<string> {
    let u = this.usuarios.find(u => u.email == email && u.password == password);
    if (!u) {
      return new Observable<string>(observer => {
        observer.error({status: 401, statusText: 'Usuario o contraseña incorrectos'});
      });
    }
    return from(this.generateJwt(u));
  }

  forgottenPassword(email: string): Observable<void> {
    const token = this.generarCadena()
    console.log('Para resetar la contraseña acceda a: '+FRONTEND_URI+'/reset-password?token='+token);
    this.forgottenPasswordTokens.set(token, email);
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  resetPassword(token: string, password: string): Observable<void> {
    if (!this.forgottenPasswordTokens.get(token)) {
      return new Observable<void>(observer => {
        observer.error('Token incorrecto');
      });
    }
    let email = this.forgottenPasswordTokens.get(token);
    console.log("Email for token: ", email)
    let u = this.usuarios.find(u => u.email == email);
    if (!u) {
      return new Observable<void>(observer => {
        observer.error('Usuario no existe');
      });
    }
    u.password = password;
    this.forgottenPasswordTokens.delete(token);

    this.guardarUsuariosEnLocalStorage();
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  private generateJwt(usuario: Usuario): Promise<string> {
    const secret = new TextEncoder().encode(SECRET_JWT);
    return new jose.SignJWT({ sub: ""+usuario.id, email: usuario.email })
      .setProtectedHeader({ alg: 'HS256' })
      .sign(secret);
  }

  private generarCadena(): string {
    return Math.random().toString(36).substring(2);
  }

}