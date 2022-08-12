import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ResponseMessage } from '../response.model';

@Component({
  selector: 'app-csvupload',
  templateUrl: './csvupload.component.html',
  styleUrls: ['./csvupload.component.css']
})
export class CSVuploadComponent implements OnInit {

  constructor(private http:HttpClient) { }

  ngOnInit(): void {
  }
  
  filename='';
  file:File | null = null;
  uploadMsg = '';

  onFileSelected(event:any) {
    this.file = event.target.files[0];
    // console.log(file)

  }
  
  onUpload() {
    
    if (this.file) {
      this.filename = this.file.name;
  
      const formData = new FormData();
      formData.append("file", this.file);
      const upload$ = this.http.post<ResponseMessage>("http://localhost:8080/users/upload", formData);
      upload$.subscribe(responseData=>{
        this.uploadMsg = responseData.message;
      }, error=>{
        console.log(error)
        this.uploadMsg = error.error.message;
      });
    }
  }

}
