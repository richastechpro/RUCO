import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'shorten',
})
export class ShortenPipe implements PipeTransform {
  transform(value: string): unknown {
    return value.length <= 15 ? value : value.substring(0, 15).concat('...');
  }
}
