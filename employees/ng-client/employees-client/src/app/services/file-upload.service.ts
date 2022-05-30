import { Injectable } from '@angular/core';
import { HttpClient, } from "@angular/common/http";
import { Observable } from 'rxjs';
import { EmployeePair } from "../interfaces/employee-pair.interface";

@Injectable({providedIn: "root"})
export class FileUploadService {

  constructor(private _http: HttpClient) {
  }

  uploadFile(file: File, dateFormat: string): Observable<EmployeePair[]> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    formData.append('dateFormat', dateFormat);
    return this._http.post<EmployeePair[]>('/api/upload', formData);
  }

}
