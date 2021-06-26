import { Injectable } from '@angular/core';
import { ApiService } from '../general/api.service';

@Injectable({
  providedIn: 'root'
})
export class RsAuthService {

  constructor(private api: ApiService) { }

  login(usuario: string, clave: string) {
    return this.api.post('auth/login', { usuario, clave });
  }
}
