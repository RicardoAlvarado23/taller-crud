import { Component, Inject, OnInit } from '@angular/core';
import { Usuario } from '../../../../core/models/usuario.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RsSuucursalService } from 'src/app/core/services/rest/rs-sucursal.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { fadeInUp400ms, fadeInRight400ms } from 'src/app/shared/animations';
import { Sucursal } from '../../../../core/models/sucursal.model';
import { NgxSpinnerService } from 'ngx-spinner';

@Component({
  selector: 'app-usuario-create-update',
  templateUrl: './usuario-create-update.component.html',
  styleUrls: ['./usuario-create-update.component.scss'],
  animations: [
    fadeInUp400ms, fadeInRight400ms
  ]
})
export class UsuarioCreateUpdateComponent implements OnInit {

  form: FormGroup;
  mode: 'create' | 'update' = 'create';
  isLoading = false;
  usuario: Usuario;
  sucursales: Sucursal[] = [];

  constructor(@Inject(MAT_DIALOG_DATA) public defaults: Usuario,
              private fb: FormBuilder,
              private rsSucursal: RsSuucursalService,
              private spinner: NgxSpinnerService,
              private dialogRef: MatDialogRef<UsuarioCreateUpdateComponent>,
  ) { 
      this.usuario = new Usuario();
      this.form = this.fb.group({
          codigoUsuario: [{value: this.defaults.codigoUsuario || '', disabled: true}],
          nombre: [this.defaults.nombre || '', [Validators.required, Validators.maxLength(50)]],
          user: [this.defaults.user || '', [Validators.required, Validators.maxLength(50)]],
          password: [this.defaults.password || '', [Validators.required, Validators.maxLength(50)]],
          sucursal: [this.defaults.sucursal || '', [Validators.required]],
      }); 
      if (this.defaults.codigoUsuario) {
          this.mode = 'update';
      } 
      
  }

  ngOnInit(): void {
      if (Object.keys(this.defaults).length === 0) {
          this.defaults = null;
      }
      this.obtenerSucursales();
  }

  obtenerSucursales() {
    this.spinner.show();
    this.rsSucursal.listar() 
        .subscribe((data) => {
          this.spinner.hide();
          if (data.success) {
            this.sucursales = data.cursor;
            this.setUpdateParametros();
          }
        });
  }

  save() {
      if (this.isCreateMode()) {
          this.createUsuario();
      } else if (this.isUpdateMode()) {
          this.updateUsuario();
      }
  }

  createUsuario() {
      const usuario: Usuario = this.form.value;
      this.dialogRef.close(usuario);
  }
  
  updateUsuario() {
      const usuario: Usuario = this.defaults;
      usuario.nombre = this.form.get('nombre').value;
      usuario.user = this.form.get('user').value;
      usuario.sucursal = this.form.get('sucursal').value;
      if (this.defaults.password != this.form.get('password').value) {
        usuario.password = this.form.get('password').value;
      }
      this.dialogRef.close(usuario);
  }

  isCreateMode() {
      return this.mode === 'create';
  }

  isUpdateMode() {
  return this.mode === 'update';
  }

  setUpdateParametros() {
    if (this.isUpdateMode()) {
        const sucursal = this.sucursales.find((t) => t.codigoSucursal == this.defaults.sucursal.codigoSucursal);
        this.form.get('sucursal').setValue(sucursal);
    }
}

  limpiar() {
      this.form.reset();
      this.defaults = new Usuario();
      this.mode = 'create';
  }
}
