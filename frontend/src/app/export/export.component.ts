import { Component } from '@angular/core';
import { saveAs } from 'file-saver/FileSaver';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-export',
  templateUrl: './export.component.html',
    styleUrls: ['./export.component.css', '../card.css'],
  providers: [HttpClient]
})
export class ExportComponent {

  constructor(private httpClient: HttpClient) { }

  saveFile() {
    const headers = new HttpHeaders();
    headers.append('Accept', 'text/plain');
    // TODO: need to append authentication in headers, and set real URL for REST call
    this.httpClient.get('/api/rest/call/here', { headers: headers })
      .toPromise()
      .then(response => this.saveToFileSystem(response));
  }

  private saveToFileSystem(response) {
    const contentDispositionHeader: string = response.headers.get('Content-Disposition');
    const parts: string[] = contentDispositionHeader.split(';');
    // assumes that filename comes in index 1 of Content-Disposition
    const filename = parts[1].split('=')[1];
    const blob = new Blob([response._body], { type: 'text/csv' });
    saveAs(blob, filename);
  }

}
