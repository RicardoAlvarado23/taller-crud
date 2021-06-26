import { Component, Inject, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { fadeInUp400ms, fadeInRight400ms } from 'src/app/shared/animations';
import { Producto } from '../../../../core/models/producto.model';
import { RsProductoService } from '../../../../core/services/rest/rs-producto.service';

@Component({
  selector: 'app-producto-create-update',
  templateUrl: './producto-create-update.component.html',
  styleUrls: ['./producto-create-update.component.scss'],
  animations: [
    fadeInUp400ms, fadeInRight400ms
  ]
})
export class ProductoCreateUpdateComponent implements OnInit {

  form: FormGroup;
  mode: 'create' | 'update' = 'create';
  isLoading = false;
  producto: Producto;

  constructor(@Inject(MAT_DIALOG_DATA) public defaults: Producto,
              private fb: FormBuilder,
              private rsProducto: RsProductoService,
              private dialogRef: MatDialogRef<ProductoCreateUpdateComponent>,
  ) { 
      this.producto = new Producto();
      this.form = this.fb.group({
          codigoProducto: [{value: this.defaults.codigoProducto || '', disabled: true}],
          nombre: [this.defaults.nombre || '', Validators.required],
          precio: [this.defaults.precio || '', [Validators.required, Validators.min(1)]]
      }); 
      if (this.defaults.codigoProducto) {
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
          this.createProducto();
      } else if (this.isUpdateMode()) {
          this.updateProducto();
      }
  }

  createProducto() {
      const producto: Producto = this.form.value;
      this.dialogRef.close(producto);
  }
  
  updateProducto() {
      const producto: Producto = this.defaults;
      producto.nombre = this.form.get('nombre').value;
      producto.precio = this.form.get('precio').value;
      this.dialogRef.close(producto);
  }

  isCreateMode() {
      return this.mode === 'create';
  }

  isUpdateMode() {
  return this.mode === 'update';
  }

  limpiar() {
      this.form.reset();
      this.defaults = new Producto();
      this.mode = 'create';
  }
}
