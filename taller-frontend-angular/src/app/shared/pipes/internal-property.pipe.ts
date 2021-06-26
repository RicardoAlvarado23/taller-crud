import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'internalProperty'
})
export class InternalPropertyPipe implements PipeTransform {

    transform(value: any | null, ...args: any[]): any {
        const row = args[0];
        const property = args[1];
        if (!property || !row || !property) {
            return value;
        }
        const propiedades = property.split('.');
        if (propiedades.length == 0) {
            return value;
        } else if (propiedades.length == 1) {
            return row[propiedades[0]];
        } else {
            value = row[propiedades[0]];
            if (value) {
                value = value[propiedades[1]];
            }
        }
        return value;
    }
  
  }
  