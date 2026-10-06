export interface Company {
  id: number;
  companyName: string;
  companyAddress: string;
  areaId: number;
  cityId: number;
  stateId: number;
  contactPerson: string;
  mobileNo: string;
  emailId: string;
  panNo: string;
  gstNo: string;
  isDelete: boolean;
  saveDate: string;
  modifyDate?: string;
  state?: any;
  city?: any;
  area?: any;
}
