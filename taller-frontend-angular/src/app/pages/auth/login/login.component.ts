import { ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { fadeInRight400ms } from '../../../shared/animations/fade-in-right.animation';
import { RsAuthService } from '../../../core/services/rest/rs-auth.service';
import { UtilService } from '../../../core/services/general/util.service';
import { UserService } from '../../../core/services/general/user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  styleUrls: ['./login.component.scss'],
  animations: [
    fadeInRight400ms,
  ]
})
export class LoginComponent implements OnInit {

  form: FormGroup;
  inputType = 'password';
  visible = false;
  verPrueba = false;
  labelVerUsuarioPrueba = "Ver";

  constructor(private fb: FormBuilder,
              private cd: ChangeDetectorRef,
              private spinner: NgxSpinnerService,
              private rsAuth: RsAuthService,
              private userService: UserService,
              private router: Router,
              private util: UtilService
    ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      user: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(20)]]
    });
  }

  toggleVisibility() {
    if (this.visible) {
      this.inputType = 'password';
      this.visible = false;
      this.cd.markForCheck();
    } else {
      this.inputType = 'text';
      this.visible = true;
      this.cd.markForCheck();
    }
  }

  toggerPruebaUser() {
    if (this.verPrueba) {
      this.labelVerUsuarioPrueba = 'Ver';
      this.verPrueba = false;
      this.cd.markForCheck();
    } else {
      this.labelVerUsuarioPrueba = 'Ocultar';
      this.verPrueba = true;
      this.cd.markForCheck();
    }
  }

  login() {
    this.spinner.show();
    const { user, password } = this.form.value;
    this.rsAuth.login(user, password)
        .subscribe((data) => {
          this.spinner.hide();
          if (data.success) {
            this.userService.guardarStorage(data.usuario);
            this.router.navigateByUrl('/admin');
          } else {
            this.util.mostrarMensajeError(data.message);
          }
        }, error => {
          this.spinner.hide();
          const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
          this.util.mostrarMensajeError(mensajeError);
        })
  }


}
