import { Branch } from "./branch";

export interface User {
  id: number;
  loginId: string;
  password: string;
  username: string;
  email: string;
  mobileNo: number;
  userType: string;
  branch?:Branch;
  isDelete: boolean;
}
