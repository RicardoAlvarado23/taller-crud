import { Inject, Injectable } from "@angular/core";
import Swal from "sweetalert2";

@Injectable({
    providedIn: 'root',
})
export class UtilService {


  constructor() {

  }

  mostrarMensajeError(mensaje: string, titulo?: string) {
      Swal.fire({
        title: titulo || "Mensaje del Sistema",
        html: mensaje,
        icon: "error",
        confirmButtonColor: "#db3724",
        confirmButtonText: "OK",
      });
  } 


  mostrarMensajeSuccess(mensaje: string, titulo?: string) {
    Swal.fire({
      title: titulo || "Mensaje del Sistema",
      html: mensaje,
      icon: "success",
      confirmButtonText: "OK",
    });
  }

  clearAllStorage() {
    localStorage.clear();
    sessionStorage.clear();
  }


  fileToBase64(file) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => resolve(reader.result);
      reader.onerror = error => reject(error);
    });
  }

}
