import { Injectable } from "@angular/core";
import { ApiService } from "../general/api.service";
import { Producto } from '../../models/producto.model';
import { HttpParams } from "@angular/common/http";

@Injectable({
    providedIn: 'root'
  })
  export class RsProductoService {
  
    constructor(private api: ApiService) { }
  
    
    listar() {
      return this.api.get('producto');
    }

    obtenerPorCodigo(codigo: string) {
        const params = new HttpParams().append('id', codigo);
        return this.api.get(`producto/${codigo}`, params);
    }

    guardar(producto: Producto) {
        return this.api.post('producto', producto);
    }

    actualizar(producto: Producto, codigo: string) {
        return this.api.put('producto', producto, codigo);
    }

    eliminar(codigo: string) {
        return this.api.delete('producto', codigo);
    }
  
  }
  