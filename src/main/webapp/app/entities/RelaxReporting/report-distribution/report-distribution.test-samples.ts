import { IReportDistribution, NewReportDistribution } from './report-distribution.model';

export const sampleWithRequiredData: IReportDistribution = {
  rid: 'c52e8424-8756-4770-8263-7d38a78d3bf4',
  email: '$@08$xKv',
};

export const sampleWithPartialData: IReportDistribution = {
  rid: '56e20205-d06d-4b70-977a-b65e05432db9',
  email: 'q}@A^eU',
  description: 'queasily gah',
};

export const sampleWithFullData: IReportDistribution = {
  rid: 'a83f17c4-5470-45a0-b2b9-8d5e60d71949',
  email: '`(_@<',
  description: 'large gah',
};

export const sampleWithNewData: NewReportDistribution = {
  email: 'dA^%.@y|<Q:G',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
