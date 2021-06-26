import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminComponent } from './admin.component';
import { UsuarioComponent } from './usuario/usuario.component';
import { ProductoComponent } from './producto/producto.component';
import { SucursalComponent } from './sucursal/sucursal.component';
import { SharedModule } from '../../shared/shared.module';
import { MaterialModule } from '../../shared/material.module';
import { AdminRoutingModule } from './admin.routes';
import { ProductoCreateUpdateComponent } from './producto/producto-create-update/producto-create-update.component';
import { SucursalCreateUpdateComponent } from './sucursal/sucursal-create-update/sucursal-create-update.component';
import { UsuarioCreateUpdateComponent } from './usuario/usuario-create-update/usuario-create-update.component';



@NgModule({
  declarations: [
    AdminComponent, 
    UsuarioComponent, 
    ProductoComponent, 
    SucursalComponent, 
    ProductoCreateUpdateComponent, 
    SucursalCreateUpdateComponent, 
    UsuarioCreateUpdateComponent],
  imports: [
    CommonModule,
    SharedModule,
    MaterialModule,
    AdminRoutingModule
  ]
})
export class AdminModule { }
