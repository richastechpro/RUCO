import { Area } from './area';
import { Branch } from './branch';
import { City } from './city';
import { State } from './state';

export interface Transporter {
  branchName: string;
  id: number;
  transporterName: string;
  transporterAddress: string;
  areaId: number;
  cityId: number;
  stateId: number;
  contactPerson: string;
  mobileNo: string;
  emailId?: string; // Optional as per DB schema
  password?: string; // Optional
  vehicleNo: string;
  licenseNo: string;
  gstNo?: string; // Optional
  panNo?: string; // Optional
  contractDate?: string; // Optional, stored in ISO date format
  ratePerKg?: number; // Optional, should use number (Decimal in DB)
  branchId: number;
  isDelete: boolean;
  saveDate: string; // Stored as ISO date string
  modifyDate?: string; // Optional
  city?: City;
  state?: State;
  area?: Area;
  branch?: Branch;
}
