import { IReportMetadata, NewReportMetadata } from './report-metadata.model';

export const sampleWithRequiredData: IReportMetadata = {
  id: 20643,
};

export const sampleWithPartialData: IReportMetadata = {
  id: 5442,
};

export const sampleWithFullData: IReportMetadata = {
  id: 1336,
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportMetadata = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
