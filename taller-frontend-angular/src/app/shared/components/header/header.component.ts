import { Component, OnInit } from '@angular/core';
import { UserService } from '../../../core/services/general/user.service';
import { Usuario } from '../../../core/models/usuario.model';
import { Router } from '@angular/router';
import { UtilService } from '../../../core/services/general/util.service';
declare var $: any;
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  usuario: Usuario;
  constructor(private userService: UserService,
              private router: Router,
              private utilService: UtilService
    ) { }

  ngOnInit(): void {
    this.usuario = this.userService.obtenerUsuarioStorage();
    $('.dropdown-toggle').dropdown();
  }

  logout() {
    this.utilService.clearAllStorage();
    this.router.navigateByUrl('/auth/login');
  }

}
