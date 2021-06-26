import { SelectionModel } from '@angular/cdk/collections';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { NgxSpinnerService } from 'ngx-spinner';
import { TableColumn } from 'src/app/core/models/table-column.model';
import { UtilService } from 'src/app/core/services/general/util.service';
import { fadeInRight400ms, fadeInUp400ms } from 'src/app/shared/animations';
import Swal from 'sweetalert2';
import { Sucursal } from '../../../core/models/sucursal.model';
import { RsSuucursalService } from '../../../core/services/rest/rs-sucursal.service';
import { SucursalCreateUpdateComponent } from './sucursal-create-update/sucursal-create-update.component';

@UntilDestroy()
@Component({
  selector: 'app-sucursal',
  templateUrl: './sucursal.component.html',
  styleUrls: ['./sucursal.component.scss'],
  animations: [
    fadeInRight400ms, fadeInUp400ms,
  ]
})
export class SucursalComponent implements OnInit, AfterViewInit {

  sucursales: Sucursal[] = [];

  columns: TableColumn<Sucursal>[] = [
    { label: 'Codigo', property: 'codigoSucursal', type: 'text', visible: true },
    { label: 'Nombre', property: 'nombre', type: 'text', visible: true },
    { label: 'Acciones', property: 'actions', type: 'button', visible: true }
  ];
  pageSize = 10;
  pageIndex = 0;
  resultsLength = 0;
  pageSizeOptions: number[] = [5, 10, 20, 50];
  dataSource: MatTableDataSource<Sucursal> | null;
  selection = new SelectionModel<Sucursal>(true, []);
  searchCtrl = new FormControl();
  keyword = '';
  dataEnvia: Sucursal = new Sucursal();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private spinner: NgxSpinnerService,
    private rsSucursal: RsSuucursalService,
    private util: UtilService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) { }

  get visibleColumns() {
    return this.columns.filter(column => column.visible).map(column => column.property);
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    
    this.sucursales = [];
    this.dataSource.data = [];
    this.obtenerSucursales();

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

  obtenerSucursales() {
    this.spinner.show();
    this.rsSucursal.listar().subscribe((data) => {
      this.spinner.hide();
      if (data.success) {
        this.sucursales = data.cursor;
        this.dataSource.data = this.sucursales;
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
    this.dialog.open(SucursalCreateUpdateComponent, {
      disableClose: true,
      data: this.dataEnvia || null,
      minWidth: '40vw'
    })
    .afterClosed().subscribe((sucursal: Sucursal) => {
      if (sucursal) {
        this.spinner.show();
        this.rsSucursal.guardar(sucursal)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Sucursal registrada correctamente');
                this.obtenerSucursales();
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

  actualizar(codigoSucursal: string) {
    this.spinner.show();
    this.rsSucursal.obtenerPorCodigo(codigoSucursal)
        .subscribe((data) => {
          this.spinner.hide();
          if (data.success) {
            const sucursal = data.sucursal;
            this.dialog.open(SucursalCreateUpdateComponent, {
              data: sucursal,
              disableClose: true,
              minWidth: '40vw'
            }).afterClosed().subscribe(sucursalActualizado => {
                if (sucursalActualizado) {
                  this.actualizarServico(sucursalActualizado, codigoSucursal);
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

  actualizarServico(sucursalActulizada: Sucursal, codigoSucursal: string) {
    this.spinner.show();
    this.rsSucursal.actualizar(sucursalActulizada, codigoSucursal)
        .subscribe((data) => {
          this.spinner.show();
          if (data.success) {
            this.openSnackBar('Sucursal actualizada correctamente');
            this.obtenerSucursales();
          } else {
            this.util.mostrarMensajeError(data.message);
          }
        }, error => {
          this.spinner.hide();
          const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
          this.util.mostrarMensajeError(mensajeError);
        })
  }

  eliminar(codigoSucursal: string) {
    Swal.fire({
      title: '¿Estás seguro de eliminar la sucursal?',
      showCancelButton: true,
      confirmButtonText: `Confirmar`,
      cancelButtonText: `Cancelar`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.spinner.show();
        this.rsSucursal.eliminar(codigoSucursal)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Sucursal eliminada correctamente');
                this.obtenerSucursales();
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
