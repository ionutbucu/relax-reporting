import dayjs from 'dayjs/esm';

import { IReportExecution, NewReportExecution } from './report-execution.model';

export const sampleWithRequiredData: IReportExecution = {
  rid: 'defe53e6-03dd-430a-9f41-5a945a8b8697',
  date: dayjs('2024-04-23T04:28'),
};

export const sampleWithPartialData: IReportExecution = {
  rid: 'd7d32fd6-9ded-4af9-8d56-5412897eff7d',
  date: dayjs('2024-04-22T20:11'),
  user: 'stand',
  additionalInfo: 'crystallize yum since',
};

export const sampleWithFullData: IReportExecution = {
  rid: '60bbc2bd-e15d-4690-9776-02ebfef79809',
  date: dayjs('2024-04-22T23:13'),
  error: 'if material after',
  url: 'https://numb-paranoia.biz',
  user: 'curriculum spiffy outback',
  additionalInfo: 'linger pro pen',
};

export const sampleWithNewData: NewReportExecution = {
  date: dayjs('2024-04-22T12:42'),
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
