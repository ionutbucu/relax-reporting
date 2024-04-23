import { IReportParam, NewReportParam } from './report-param.model';

export const sampleWithRequiredData: IReportParam = {
  rid: 'ed7baff6-ab65-4713-aec3-5395122ca3cd',
};

export const sampleWithPartialData: IReportParam = {
  rid: 'bbe56971-8c6c-49f2-bfe5-ba1155801adc',
  name: 'as witness rabbit',
  value: 'phew recede',
  conversionRule: 'delayed carry helpfully',
};

export const sampleWithFullData: IReportParam = {
  rid: 'a137eb09-73a2-4a92-ba4c-6a3561ecaeaf',
  name: 'spook um apron',
  type: 'where pledge prevalence',
  value: 'mmm when so',
  conversionRule: 'shout before into',
};

export const sampleWithNewData: NewReportParam = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
