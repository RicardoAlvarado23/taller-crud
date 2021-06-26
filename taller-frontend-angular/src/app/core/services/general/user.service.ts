import { Injectable } from '@angular/core';
import { Usuario } from '../../models/usuario.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor() { }

  guardarStorage(usuario: Usuario) {
    sessionStorage.setItem('usuario', JSON.stringify(usuario));
  }

  obtenerUsuarioStorage(): Usuario {
    const usuario: Usuario = JSON.parse(sessionStorage.getItem('usuario'));
    return usuario;
  }

}
