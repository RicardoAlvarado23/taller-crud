import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AdminComponent } from './admin.component';
import { UsuarioComponent } from './usuario/usuario.component';
import { ProductoComponent } from './producto/producto.component';
import { SucursalComponent } from './sucursal/sucursal.component';
import { AuthGuard } from '../../core/guards/auth.guard';

const routes: Routes = [
    {
        path: '', component: AdminComponent, canActivate: [AuthGuard],
        children: [
            {path: 'usuarios', component: UsuarioComponent,  canActivate: [AuthGuard]},
            {path: 'productos', component: ProductoComponent, canActivate: [AuthGuard]},
            {path: 'sucursales', component: SucursalComponent, canActivate: [AuthGuard]},
            {
                path: '**',
                redirectTo: 'usuarios'
            }
        ]
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdminRoutingModule { }