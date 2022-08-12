import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Employee } from '../employee-info/employee.model';

@Component({
  selector: 'app-employee-form',
  templateUrl: './employee-form.component.html',
  styleUrls: ['./employee-form.component.css']
})
export class EmployeeFormComponent implements OnInit {

  emp: Employee;
  constructor(public dialogRef: MatDialogRef<EmployeeFormComponent>,
              @Inject(MAT_DIALOG_DATA) data:Employee) { 
                this.emp = new Employee("","","",0,new Date());
              }
  
  ngOnInit(): void {
  }

  save(){
    this.dialogRef.close(this.emp);
  }
  
  close(){
    this.dialogRef.close();
  }

}
  

