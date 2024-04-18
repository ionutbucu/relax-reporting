import { IReportDataSource, NewReportDataSource } from './report-data-source.model';

export const sampleWithRequiredData: IReportDataSource = {
  id: 30739,
};

export const sampleWithPartialData: IReportDataSource = {
  id: 8077,
  password: 'after glitter',
};

export const sampleWithFullData: IReportDataSource = {
  id: 13411,
  type: 'beneath lazy ah',
  url: 'https://skinny-urge.name/',
  user: 'scaly before',
  password: 'off balloonist',
};

export const sampleWithNewData: NewReportDataSource = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
