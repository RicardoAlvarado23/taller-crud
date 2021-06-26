import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router, CanLoad } from '@angular/router';
import { Observable } from 'rxjs';
import { Usuario } from '../models/usuario.model';
import { UserService } from '../services/general/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanLoad, CanActivate {

  constructor(private usuarioServicio: UserService,
              private router: Router
    ) {}
    canActivate(): boolean   {
      return this.validarUsuario();
    }

  canLoad(): boolean   {
    return this.validarUsuario();
  }

  validarUsuario() {
    const usuario: Usuario = this.usuarioServicio.obtenerUsuarioStorage();
    if (usuario) {
      return true;
    } else {
      this.router.navigateByUrl('/auth/login');
      return false;
    }
  }

  
  
}
