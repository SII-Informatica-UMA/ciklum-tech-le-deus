export interface DietaNueva {
    nombre: string;
    descripcion: string;
    observaciones: string;
    objetivo: string;
    duracionDias:    Number;
    alimentos: string[];
    recomendaciones  : string;
    idDieta: Number;
    usuarioId: number; 
    creadorId: number | undefined ;
}
