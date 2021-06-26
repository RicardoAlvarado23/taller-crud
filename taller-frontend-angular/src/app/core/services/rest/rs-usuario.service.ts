import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Usuario } from '../../models/usuario.model';
import { ApiService } from '../general/api.service';

@Injectable({
  providedIn: 'root'
})
export class RsUsuarioService {

  constructor(private api: ApiService) { }

  
  listar() {
    return this.api.get('usuario');
  }

  obtenerPorCodigo(codigo: string) {
    const params = new HttpParams().append('id', codigo);
    return this.api.get(`usuario/${codigo}`, params);
  }
  
  guardar(usuario: Usuario) {
      return this.api.post('usuario', usuario);
  }
  
  actualizar(usuario: Usuario, codigo: string) {
      return this.api.put('usuario', usuario, codigo);
  }
  
  eliminar(codigo: string) {
      return this.api.delete('usuario', codigo);
  }

}
