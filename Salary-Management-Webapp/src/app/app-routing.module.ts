import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CSVuploadComponent } from './csvupload/csvupload.component';
import { EmployeeInfoComponent } from './employee-info/employee-info.component';

const routes: Routes = [
  {path:"",component:CSVuploadComponent},
  {path:"employees",component:EmployeeInfoComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
