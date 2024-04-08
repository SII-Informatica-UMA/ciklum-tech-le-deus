
//ya venia importado
import { Component } from '@angular/core';

//lo he importado yo
import { NgbCarouselModule } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [NgbCarouselModule], //lo he importado yo, lo demas ya venia
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent {

}
