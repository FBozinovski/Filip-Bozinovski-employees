import { Project } from "./project.interface";

export interface EmployeePair {
  firstEmployeeId: number;
  secondEmployeeId: number;
  daysWorked: number;
  projectResponseList: Project[];

}
