import { IReportColumnMapping, NewReportColumnMapping } from './report-column-mapping.model';

export const sampleWithRequiredData: IReportColumnMapping = {
  id: 11265,
};

export const sampleWithPartialData: IReportColumnMapping = {
  id: 5520,
  columnTitle: 'blame',
  lang: 'through amid',
};

export const sampleWithFullData: IReportColumnMapping = {
  id: 20397,
  sourceColumnName: 'for around',
  sourceColumnIndex: 14203,
  columnTitle: 'hence jailhouse',
  lang: 'bossy um',
};

export const sampleWithNewData: NewReportColumnMapping = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
