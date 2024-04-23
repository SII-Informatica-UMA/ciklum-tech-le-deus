import { APP_BASE_HREF as BaseHref } from '@angular/common';
import { CommonEngine as Engine } from '@angular/ssr';
import express from 'express';
import { fileURLToPath } from 'node:url';
import { dirname, join, resolve } from 'node:path';
import bootstrap from './src/main.server';

export function aplicacion(): express.Express {
  const servidor = express();
  const carpetaDistribucionServidor = dirname(fileURLToPath(import.meta.url));
  const carpetaDistribucionNavegador = resolve(carpetaDistribucionServidor, '../navegador');
  const indexHtml = join(carpetaDistribucionServidor, 'index.server.html');

  const motorComun = new Engine();

  servidor.set('view engine', 'html');
  servidor.set('views', carpetaDistribucionNavegador);

  
  servidor.get('*.*', express.static(carpetaDistribucionNavegador, {
    maxAge: '1y'
  }));

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

function start(): void {
  const puerto = process.env['SSR_PORT'] || 4000;

  // Iniciar el servidor Node
  const servidor = aplicacion();
  servidor.listen(puerto, () => {
    console.log(`Servidor Node Express escuchando en http://localhost:${puerto}`);
  });
}

start();
