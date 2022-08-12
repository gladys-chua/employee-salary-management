import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Employee } from '../employee-info/employee.model';

@Component({
  selector: 'app-employee-dialog',
  templateUrl: './employee-dialog.component.html',
  styleUrls: ['./employee-dialog.component.css']
})
export class EmployeeDialogComponent implements OnInit {
  
  user: Employee;
  constructor(public dialogRef: MatDialogRef<EmployeeDialogComponent>,
              @Inject(MAT_DIALOG_DATA) data:Employee) { 
                this.user = data;
              }

  ngOnInit(): void {
  }

  save(){
    this.dialogRef.close(this.user);
  }

  close(){
    this.dialogRef.close();
  }
}
