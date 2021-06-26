import { Injectable } from "@angular/core";
import { ApiService } from "../general/api.service";
import { HttpParams } from '@angular/common/http';
import { Sucursal } from '../../models/sucursal.model';

@Injectable({
    providedIn: 'root'
})
export class RsSuucursalService {
  
    constructor(private api: ApiService) { }
    
    
    listar() {
        return this.api.get('sucursal');
    }

    obtenerPorCodigo(codigo: string) {
        const params = new HttpParams().append('id', codigo);
        return this.api.get(`sucursal/${codigo}`, params);
    }

    guardar(sucursal: Sucursal) {
        return this.api.post('sucursal', sucursal);
    }

    actualizar(sucursal: Sucursal, codigo: string) {
        return this.api.put('sucursal', sucursal, codigo);
    }

    eliminar(codigo: string) {
        return this.api.delete('sucursal', codigo);
    }
  
}
