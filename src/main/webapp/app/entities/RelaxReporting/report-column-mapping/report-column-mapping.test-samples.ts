import { IReportColumnMapping, NewReportColumnMapping } from './report-column-mapping.model';

export const sampleWithRequiredData: IReportColumnMapping = {
  rid: 'd610c0bf-a6f6-4c9a-b21c-8bd3ff3aabd0',
};

export const sampleWithPartialData: IReportColumnMapping = {
  rid: '61a29ad7-a100-486c-94a9-ca17707b026e',
  sourceColumnIndex: 5825,
};

export const sampleWithFullData: IReportColumnMapping = {
  rid: '1df79301-38aa-4d7a-abec-961fc737333c',
  sourceColumnName: 'indeed',
  sourceColumnIndex: 4239,
  columnTitle: 'nor',
  lang: 'harmless countess',
};

export const sampleWithNewData: NewReportColumnMapping = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
