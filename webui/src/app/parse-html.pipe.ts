import { Pipe, PipeTransform } from '@angular/core';

/*
 * Takes an html source code as string, parses it
 * into a DOM Document and returns the body element as string.
*/
@Pipe({ name: 'parseHtml' })
export class ParseHtmlPipe implements PipeTransform {
  transform(html: string) {
    const parsed = new DOMParser().parseFromString(html, 'text/html');
    console.log('parsed: ');
    console.log(parsed);
    return parsed;
  }
}
