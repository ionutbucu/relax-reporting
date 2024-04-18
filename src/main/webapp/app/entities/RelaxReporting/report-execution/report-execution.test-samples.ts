import dayjs from 'dayjs/esm';

import { IReportExecution, NewReportExecution } from './report-execution.model';

export const sampleWithRequiredData: IReportExecution = {
  id: 11892,
  date: dayjs('2024-04-17T19:27'),
};

export const sampleWithPartialData: IReportExecution = {
  id: 10843,
  date: dayjs('2024-04-18T10:03'),
};

export const sampleWithFullData: IReportExecution = {
  id: 30537,
  date: dayjs('2024-04-18T05:44'),
  error: 'yet',
  url: 'https://bleak-ethyl.name/',
  user: 'hungry drat',
};

export const sampleWithNewData: NewReportExecution = {
  date: dayjs('2024-04-18T06:22'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
