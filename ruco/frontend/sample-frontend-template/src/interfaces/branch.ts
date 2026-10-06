export interface Branch {
  id: number;
  branchName: string;
  branchAddress: string;
  areaId: number;
  cityId: number;
  stateId: number;
  descr?: string;
  gstNo: string;
  panNo: string;
  isDelete: boolean;
  saveDate: string;
  modifyDate?: string;
  state?: any;
  city?: any;
  area?: any;
  warehouseName:string;
}
