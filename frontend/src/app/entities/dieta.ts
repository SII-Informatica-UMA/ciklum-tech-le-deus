export interface Dieta{
    nombre: string,
    descripcion: string,
    observaciones: string,
    objetivo: string,
    duracionDias: number,
    alimentos: string[],
    recomendaciones: string,
    id: number ;
    usuarioId: number;
    creadorId: number | undefined ;
  }