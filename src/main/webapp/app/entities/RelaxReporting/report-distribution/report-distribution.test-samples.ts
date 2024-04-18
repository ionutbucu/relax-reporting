import { IReportDistribution, NewReportDistribution } from './report-distribution.model';

export const sampleWithRequiredData: IReportDistribution = {
  id: 25643,
  email: '1vS@,:',
};

export const sampleWithPartialData: IReportDistribution = {
  id: 15739,
  email: 'GLN@1',
};

export const sampleWithFullData: IReportDistribution = {
  id: 13315,
  email: 'No@P^',
  description: 'contributor trepan',
};

export const sampleWithNewData: NewReportDistribution = {
  email: 'vL=@v.$',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
