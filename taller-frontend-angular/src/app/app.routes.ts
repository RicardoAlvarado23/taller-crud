import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { AuthGuard } from './core/guards/auth.guard';

const routes: Routes = [
    {
        path: 'auth',
        loadChildren: () =>
            import('./pages/auth/auth.module').then(
                (m) => m.AuthModule
            ),
    },
    {
        path: 'admin',
        loadChildren: () =>
            import('./pages/admin/admin.module').then(
                (m) => m.AdminModule
            ),
        canLoad: [AuthGuard],
    },
    {
        path: '**',
        redirectTo: 'auth'
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes, { useHash: true })],
    exports: [RouterModule]
})
export class AppRoutingModule { }