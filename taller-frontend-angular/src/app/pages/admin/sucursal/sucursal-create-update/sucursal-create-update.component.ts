import { Component, Inject, OnInit } from '@angular/core';
import { fadeInUp400ms } from '../../../../shared/animations/fade-in-up.animation';
import { fadeInRight400ms } from '../../../../shared/animations/fade-in-right.animation';
import { Sucursal } from '../../../../core/models/sucursal.model';
import { RsSuucursalService } from '../../../../core/services/rest/rs-sucursal.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-sucursal-create-update',
  templateUrl: './sucursal-create-update.component.html',
  styleUrls: ['./sucursal-create-update.component.scss'],
  animations: [
    fadeInUp400ms, fadeInRight400ms
  ]
})
export class SucursalCreateUpdateComponent implements OnInit {

  form: FormGroup;
  mode: 'create' | 'update' = 'create';
  isLoading = false;
  sucursal: Sucursal;

  constructor(@Inject(MAT_DIALOG_DATA) public defaults: Sucursal,
              private fb: FormBuilder,
              private rsSucursal: RsSuucursalService,
              private dialogRef: MatDialogRef<SucursalCreateUpdateComponent>,
  ) { 
      this.sucursal = new Sucursal();
      this.form = this.fb.group({
          codigoSucursal: [{value: this.defaults.codigoSucursal || '', disabled: true}],
          nombre: [this.defaults.nombre || '', Validators.required]
      }); 
      if (this.defaults.codigoSucursal) {
          this.mode = 'update';
      } 
      
  }

  ngOnInit(): void {
      if (Object.keys(this.defaults).length === 0) {
          this.defaults = null;
      }
  }

  save() {
      if (this.isCreateMode()) {
          this.createSucursal();
      } else if (this.isUpdateMode()) {
          this.updateSucursal();
      }
  }

  createSucursal() {
      const servicio: Sucursal = this.form.value;
      this.dialogRef.close(servicio);
  }
  
  updateSucursal() {
      const sucursal: Sucursal = this.defaults;
      sucursal.nombre = this.form.get('nombre').value;
      this.dialogRef.close(sucursal);
  }

  isCreateMode() {
      return this.mode === 'create';
  }

  isUpdateMode() {
  return this.mode === 'update';
  }

  limpiar() {
      this.form.reset();
      this.defaults = new Sucursal();
      this.mode = 'create';
  }
}
