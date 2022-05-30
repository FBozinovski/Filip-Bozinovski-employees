import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FileUploadService } from "../../services/file-upload.service";
import { EmployeePair } from "../../interfaces/employee-pair.interface";
import { ToastrService } from "ngx-toastr";
import { FormBuilder, FormControl, FormGroup, Validators } from "@angular/forms";

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.css']
})
export class UploadFileComponent implements OnInit {
  @ViewChild("file", {static: false})
  file!: ElementRef;
  employeePairs!: EmployeePair[];
  uploadForm!: FormGroup;

  constructor(private _fileUploadService: FileUploadService,
              private _toaster: ToastrService,
              private _formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.uploadForm = this._formBuilder.group({
      dateFormat: new FormControl(null, Validators.required),
      fileUpload: new FormControl(null, Validators.required)
    });
  }

  upload() {
    this._fileUploadService.uploadFile(this.file.nativeElement.files[0], this.uploadForm.value.dateFormat).subscribe({
      next: employeePairs => {
        this.employeePairs = employeePairs;
        this.uploadForm.reset();
      }, error: err => {
        this._toaster.error(err.error);
        this.uploadForm.reset();
      }
    });
  }

  getClass(index: number) {
    return index % 2 === 0 ? 'table-primary' : '';
  }

}
