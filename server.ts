import { APP_BASE_HREF as BaseHref } from '@angular/common';
import { CommonEngine as Engine } from '@angular/ssr';
import express from 'express';
import { fileURLToPath } from 'node:url';
import { dirname, join, resolve } from 'node:path';
import bootstrap from './src/main.server';

// Se exporta la aplicación Express para que pueda ser utilizada por funciones sin servidor.
export function aplicacion(): express.Express {
  const servidor = express();
  const carpetaDistribucionServidor = dirname(fileURLToPath(import.meta.url));
  const carpetaDistribucionNavegador = resolve(carpetaDistribucionServidor, '../navegador');
  const indexHtml = join(carpetaDistribucionServidor, 'index.server.html');

  const motorComun = new Engine();

  servidor.set('view engine', 'html');
  servidor.set('views', carpetaDistribucionNavegador);

  // Ejemplo de puntos finales de API Rest de Express
  // server.get('/api/**', (req, res) => { });
  // Servir archivos estáticos desde /navegador
  servidor.get('*.*', express.static(carpetaDistribucionNavegador, {
    maxAge: '1y'
  }));

  // Todas las rutas regulares utilizan el motor Angular
  servidor.get('*', (req, res, next) => {
    const { protocol, originalUrl, baseUrl, headers } = req;

    motorComun
      .render({
        bootstrap,
        documentFilePath: indexHtml,
        url: `${protocol}://${headers.host}${originalUrl}`,
        publicPath: carpetaDistribucionNavegador,
        providers: [{ provide: BaseHref, useValue: baseUrl }],
      })
      .then((html) => res.send(html))
      .catch((err) => next(err));
  });

  return servidor;
}

function iniciar(): void {
  const puerto = process.env['SSR_PORT'] || 4000;

  // Iniciar el servidor Node
  const servidor = aplicacion();
  servidor.listen(puerto, () => {
    console.log(`Servidor Node Express escuchando en http://localhost:${puerto}`);
  });
}

iniciar();
