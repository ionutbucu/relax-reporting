import { IReportParam, NewReportParam } from './report-param.model';

export const sampleWithRequiredData: IReportParam = {
  id: 30686,
};

export const sampleWithPartialData: IReportParam = {
  id: 30952,
  type: 'cable than geez',
};

export const sampleWithFullData: IReportParam = {
  id: 25835,
  name: 'plus aw wholly',
  type: 'inquisitively wiseguy authentic',
  value: 'witness',
  conversionRule: 'leafy',
};

export const sampleWithNewData: NewReportParam = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
