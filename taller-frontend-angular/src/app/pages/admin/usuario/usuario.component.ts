import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { NgxSpinnerService } from 'ngx-spinner';
import { TableColumn } from 'src/app/core/models/table-column.model';
import { Usuario } from '../../../core/models/usuario.model';
import { RsUsuarioService } from '../../../core/services/rest/rs-usuario.service';
import { UtilService } from '../../../core/services/general/util.service';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { fadeInRight400ms } from '../../../shared/animations/fade-in-right.animation';
import { fadeInUp400ms } from '../../../shared/animations/fade-in-up.animation';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Producto } from 'src/app/core/models/producto.model';
import Swal from 'sweetalert2';
import { ProductoCreateUpdateComponent } from '../producto/producto-create-update/producto-create-update.component';
import { UsuarioCreateUpdateComponent } from './usuario-create-update/usuario-create-update.component';

@UntilDestroy()
@Component({
  selector: 'app-usuario',
  templateUrl: './usuario.component.html',
  styleUrls: ['./usuario.component.scss'],
  animations: [
    fadeInRight400ms, fadeInUp400ms,
  ]
})
export class UsuarioComponent implements OnInit, AfterViewInit {
  usuarios: Usuario[] = [];

  columns: TableColumn<Usuario>[] = [
    { label: 'Codigo', property: 'codigoUsuario', type: 'text', visible: true },
    { label: 'Nombre', property: 'nombre', type: 'text', visible: true },
    { label: 'Usuario', property: 'user', type: 'text', visible: true },
    { label: 'Sucursal', property: 'sucursal.nombre', type: 'text', visible: true , transform: 'titlecase'},
    { label: 'Acciones', property: 'actions', type: 'button', visible: true }
  ];
  pageSize = 10;
  pageIndex = 0;
  resultsLength = 0;
  pageSizeOptions: number[] = [5, 10, 20, 50];
  dataSource: MatTableDataSource<Usuario> | null;
  selection = new SelectionModel<Usuario>(true, []);
  searchCtrl = new FormControl();
  keyword = '';
  dataEnvia: Usuario = new Usuario();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private spinner: NgxSpinnerService,
    private rsUsuario: RsUsuarioService,
    private util: UtilService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) { }

  get visibleColumns() {
    return this.columns.filter(column => column.visible).map(column => column.property);
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    
    this.usuarios = [];
    this.dataSource.data = [];
    this.obtenerUsuarios();

    this.searchCtrl.valueChanges.pipe(
      untilDestroyed(this)
    ).subscribe(value => this.onFilterChange(value));
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
  
  onFilterChange(value: string) {
      if (!this.dataSource) {
          return;
      }
      value = value.trim();
      value = value.toLowerCase();
      this.dataSource.filter = value;
  }

  obtenerUsuarios() {
    this.spinner.show();
    this.rsUsuario.listar().subscribe((data) => {
      this.spinner.hide();
      if (data.success) {
        this.usuarios = data.cursor;
        this.dataSource.data = this.usuarios;
      } else {
        this.util.mostrarMensajeError(data.message);
      }
    }, error => {
      this.spinner.hide();
      const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
      this.util.mostrarMensajeError(mensajeError);
    })
  }

  crear() {
    this.dialog.open(UsuarioCreateUpdateComponent, {
      disableClose: true,
      data: this.dataEnvia || null,
      minWidth: '40vw'
    })
    .afterClosed().subscribe((usuario: Usuario) => {
      if (usuario) {
        this.spinner.show();
        this.rsUsuario.guardar(usuario)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Usuario registrado correctamente');
                this.obtenerUsuarios();
              } else {
                this.util.mostrarMensajeError(data.message);
              }
            }, error => {
              this.spinner.hide();
              const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
              this.util.mostrarMensajeError(mensajeError);
            })
      }
    });
  }

  actualizar(codigoUsuario: string) {
    this.spinner.show();
    this.rsUsuario.obtenerPorCodigo(codigoUsuario)
        .subscribe((data) => {
          this.spinner.hide();
          if (data.success) {
            const usuario = data.usuario;
            this.dialog.open(UsuarioCreateUpdateComponent, {
              data: usuario,
              disableClose: true,
              minWidth: '40vw'
            }).afterClosed().subscribe(usuarioActualizado => {
                if (usuarioActualizado) {
                  this.actualizarServico(usuarioActualizado, codigoUsuario);
                }
            });
          } else {
            this.util.mostrarMensajeError(data.message);
          }
        }, error => {
          this.spinner.hide();
          const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
          this.util.mostrarMensajeError(mensajeError);
        })
  
  }

  actualizarServico(usuarioActualizado: Usuario, codigoUsuario: string) {
    this.spinner.show();
    this.rsUsuario.actualizar(usuarioActualizado, codigoUsuario)
        .subscribe((data) => {
          this.spinner.show();
          if (data.success) {
            this.openSnackBar('Usuario actualizado correctamente');
            this.obtenerUsuarios();
          } else {
            this.util.mostrarMensajeError(data.message);
          }
        }, error => {
          this.spinner.hide();
          const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
          this.util.mostrarMensajeError(mensajeError);
        })
  }

  eliminar(codigoUsuario: string) {
    Swal.fire({
      title: '¿Estás seguro de eliminar el usuario?',
      showCancelButton: true,
      confirmButtonText: `Confirmar`,
      cancelButtonText: `Cancelar`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.spinner.show();
        this.rsUsuario.eliminar(codigoUsuario)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Usuario eliminado correctamente');
                this.obtenerUsuarios();
              }else {
                this.util.mostrarMensajeError(data.message);
              }
            }, error => {
              this.spinner.hide();
              const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
              this.util.mostrarMensajeError(mensajeError);
            })
      } 
    })
  }

  openSnackBar(message: string) {
    this._snackBar.open(message, 'Aceptar', {
      duration: 5000
    });
  }
   
  trackByProperty<T>(index: number, column: TableColumn<T>) {
    return column.property;
  }


}
