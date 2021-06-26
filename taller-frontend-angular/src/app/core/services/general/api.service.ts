import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  private API_URL = `${environment.api}/`
  

  httpOptions(): any {
    const httpHeaders = new HttpHeaders({
      "Content-Type": "application/json",
      Authorization: 'Basic ' + btoa('admin:123456'),
    });
    return {
      headers: httpHeaders,
      responseType: "json",
    };
  }

  constructor(private http: HttpClient) { }

  get(url: string, urlParams?: HttpParams, httpOptions?: any): Observable<any> {
    let params = this.httpOptions();
    if (httpOptions) {
      params = httpOptions;
    }
    if (urlParams) {
      params['params'] = urlParams;
    }
    return this.http.get( `${this.API_URL}${url}`, params)
              .pipe(
                map(resp => resp),
                catchError((error: any): any => {
                  return throwError(error || 'Server error');
                })
              );
  }

  post(url: string, body: any): Observable<any> {
    return this.http.post<any>(
      `${this.API_URL}${url}`,
      body,
      this.httpOptions()
    ).pipe( map( resp => {
      let resultado = resp;
      return resultado;
      }
    ), catchError((error: any): any => {
      return throwError(error || 'Server error');
    }));
  }

  put(url: string, body: any, id): Observable<any> {
    return this.http.put<any>(
      `${this.API_URL}${url}/${id}`,
      body,
      this.httpOptions()
    ).pipe( map( resp => {
      let resultado = resp;
      return resultado;
      }
    ), catchError((error: any): any => {
      return throwError(error || 'Server error');
    }));
  }

  delete(url: string, id): Observable<any> {
    return this.http.delete<any>(
      `${this.API_URL}${url}/${id}`,
      this.httpOptions()
    ).pipe( map( resp => {
      let resultado = resp;
      return resultado;
      }
    ), catchError((error: any): any => {
      return throwError(error || 'Server error');
    }));
  }


}
