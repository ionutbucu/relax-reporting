import { IReportDataSource, NewReportDataSource } from './report-data-source.model';

export const sampleWithRequiredData: IReportDataSource = {
  rid: '3336062b-f622-4892-a281-a7198e5377a9',
};

export const sampleWithPartialData: IReportDataSource = {
  rid: '96f9c92e-f3d0-489e-b7e6-321fd771a9d9',
  url: 'https://fussy-understanding.biz/',
  password: 'needily',
};

export const sampleWithFullData: IReportDataSource = {
  rid: '6db5c189-7582-4566-95c9-ae5144c63233',
  type: 'velvety promptly',
  url: 'https://vigilant-mambo.org/',
  user: 'finally prestigious brr',
  password: 'freely grate so',
};

export const sampleWithNewData: NewReportDataSource = {
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
