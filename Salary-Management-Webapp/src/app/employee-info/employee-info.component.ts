import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ResponseMessage } from '../response.model';
import { Employee } from './employee.model';
import { MatDialog} from "@angular/material/dialog";
import { EmployeeDialogComponent } from '../employee-dialog/employee-dialog.component';
import { EmployeeFormComponent } from '../employee-form/employee-form.component';

@Component({
  selector: 'app-employee-info',
  templateUrl: './employee-info.component.html',
  styleUrls: ['./employee-info.component.css']
})
export class EmployeeInfoComponent implements OnInit {

  employees: Employee[] = [];
  headers:string[] = ['id','name','login','salary','start Date']
  minS: number = 0;
  maxS: number = 4000;
  offset: number = 0;
  limit: number = 30;

  constructor(private http:HttpClient, private route: ActivatedRoute, 
              private router:Router, private editDialog: MatDialog) { }

  ngOnInit(): void {
    this.fetchEmployees();
  }

  private fetchEmployees() {
    this.http.get<Employee[]>("http://localhost:8080/all")
    .subscribe(u=>{
      // console.log(u);
      this.employees=u.slice(this.offset,this.limit);
    });
  }

  valChange() {
    // console.log(this.minS,this.maxS);
    this.router.navigate(['/employees'],{queryParams: {minSalary:this.minS, maxSalary:this.maxS, offset:this.offset, limit:this.limit}})
    .then(nav=>{
      // console.log(this.router.url)
      // console.log(this.router.url.slice("/employees".length))
      this.http.get<Employee[]>("http://localhost:8080/users"+this.router.url.slice("/employees".length))
      .subscribe(data=>{
        console.log(data);
        if (data === null){
          this.employees=[];
        } else {
          this.employees=data;
        }
      });
    });
  }

  onEmpDelete(u: Employee) {
    this.http.delete<ResponseMessage>("http://localhost:8080/users/"+u.id).subscribe(v=>{
        this.ngOnInit();
        console.log(v.message)
    }, error=>{
      console.log(error.error.message)
    });
  }

  openDialog(u:Employee): void{

    const dialogRef = this.editDialog.open(EmployeeDialogComponent,{
      width: '300px',
      data: u
    });
    
    dialogRef.afterClosed().subscribe( data => {      
      this.http.patch<Employee>("http://localhost:8080/users/"+data.id, new Employee(data.id,data.name,data.login,data.salary,data.startDate))
      .subscribe(responseData=>{"updated data: "+ console.log(responseData)},
      error=>{
        this.ngOnInit();
      });
    });
  }

  addEmp(){
    const dialogRef2 = this.editDialog.open(EmployeeFormComponent,{
      width: '300px'
    });
    
    dialogRef2.afterClosed().subscribe( data => {      
      console.log(data)
      this.http.post("http://localhost:8080/users",data).subscribe(
        responseData=> {
          console.log(responseData);
          this.ngOnInit();
        });
    });
  }

}
