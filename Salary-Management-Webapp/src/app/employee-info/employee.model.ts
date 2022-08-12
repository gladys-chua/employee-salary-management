export class Employee  {
    id: string;
    name:string;
    login:string;
    salary:number;
    startDate: Date;

    constructor(id:string,name:string,login:string,salary:number,startDate:Date){
        this.id = id;
        this.name = name;
        this.login = login;
        this.salary = salary;
        this.startDate = startDate;
        
    }
}