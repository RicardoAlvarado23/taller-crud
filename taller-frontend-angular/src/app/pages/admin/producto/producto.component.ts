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
import { Producto } from '../../../core/models/producto.model';
import { RsProductoService } from '../../../core/services/rest/rs-producto.service';
import { ProductoCreateUpdateComponent } from './producto-create-update/producto-create-update.component';

@UntilDestroy()
@Component({
  selector: 'app-producto',
  templateUrl: './producto.component.html',
  styleUrls: ['./producto.component.scss'],
  animations: [
    fadeInRight400ms, fadeInUp400ms,
  ]
})
export class ProductoComponent implements OnInit, AfterViewInit {

  productos: Producto[] = [];

  columns: TableColumn<Producto>[] = [
    { label: 'Codigo', property: 'codigoProducto', type: 'text', visible: true },
    { label: 'Nombre', property: 'nombre', type: 'text', visible: true },
    { label: 'Precio', property: 'precio', type: 'text', visible: true},
    { label: 'Acciones', property: 'actions', type: 'button', visible: true }
  ];
  pageSize = 10;
  pageIndex = 0;
  resultsLength = 0;
  pageSizeOptions: number[] = [5, 10, 20, 50];
  dataSource: MatTableDataSource<Producto> | null;
  selection = new SelectionModel<Producto>(true, []);
  searchCtrl = new FormControl();
  keyword = '';
  dataEnvia: Producto = new Producto();

  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort: MatSort;

  constructor(
    private spinner: NgxSpinnerService,
    private rsProducto: RsProductoService,
    private util: UtilService,
    private dialog: MatDialog,
    private _snackBar: MatSnackBar
  ) { }

  get visibleColumns() {
    return this.columns.filter(column => column.visible).map(column => column.property);
  }

  ngOnInit(): void {
    this.dataSource = new MatTableDataSource();
    
    this.productos = [];
    this.dataSource.data = [];
    this.obtenerProductos();

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

  obtenerProductos() {
    this.spinner.show();
    this.rsProducto.listar().subscribe((data) => {
      this.spinner.hide();
      if (data.success) {
        this.productos = data.cursor;
        this.dataSource.data = this.productos;
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
    this.dialog.open(ProductoCreateUpdateComponent, {
      disableClose: true,
      data: this.dataEnvia || null,
      minWidth: '40vw'
    })
    .afterClosed().subscribe((producto: Producto) => {
      if (producto) {
        this.spinner.show();
        this.rsProducto.guardar(producto)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Producut registrado correctamente');
                this.obtenerProductos();
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

  actualizar(codigoProducto: string) {
    this.spinner.show();
    this.rsProducto.obtenerPorCodigo(codigoProducto)
        .subscribe((data) => {
          this.spinner.hide();
          if (data.success) {
            const producto = data.producto;
            this.dialog.open(ProductoCreateUpdateComponent, {
              data: producto,
              disableClose: true,
              minWidth: '40vw'
            }).afterClosed().subscribe(productoActualizado => {
                if (productoActualizado) {
                  this.actualizarServico(productoActualizado, codigoProducto);
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

  actualizarServico(productoActualizado: Producto, codigoProducto: string) {
    this.spinner.show();
    this.rsProducto.actualizar(productoActualizado, codigoProducto)
        .subscribe((data) => {
          this.spinner.show();
          if (data.success) {
            this.openSnackBar('Producto actualizado correctamente');
            this.obtenerProductos();
          } else {
            this.util.mostrarMensajeError(data.message);
          }
        }, error => {
          this.spinner.hide();
          const mensajeError = error.error?.message || 'Error inesperado, vuelva a intentaro dentro de unos minutos'
          this.util.mostrarMensajeError(mensajeError);
        })
  }

  eliminar(codigoProducto: string) {
    Swal.fire({
      title: '¿Estás seguro de eliminar el producto?',
      showCancelButton: true,
      confirmButtonText: `Confirmar`,
      cancelButtonText: `Cancelar`,
    }).then((result) => {
      if (result.isConfirmed) {
        this.spinner.show();
        this.rsProducto.eliminar(codigoProducto)
            .subscribe((data) => {
              this.spinner.hide();
              if (data.success) {
                this.openSnackBar('Producto eliminado correctamente');
                this.obtenerProductos();
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
