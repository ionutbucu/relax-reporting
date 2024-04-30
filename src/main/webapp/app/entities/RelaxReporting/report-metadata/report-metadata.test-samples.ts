import { IReportMetadata, NewReportMetadata } from './report-metadata.model';

export const sampleWithRequiredData: IReportMetadata = {
  rid: '634596b1-2e77-4f65-b36c-615db056be78',
};

export const sampleWithPartialData: IReportMetadata = {
  rid: '160e18a6-e0c9-4e28-9416-6f30acae1c3e',
};

export const sampleWithFullData: IReportMetadata = {
  rid: '87986622-7ca3-444d-8bd8-ce59f156b1d2',
  metadata: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewReportMetadata = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
