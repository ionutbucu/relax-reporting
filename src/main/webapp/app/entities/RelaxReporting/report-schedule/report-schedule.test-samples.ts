import { IReportSchedule, NewReportSchedule } from './report-schedule.model';

export const sampleWithRequiredData: IReportSchedule = {
  id: 5092,
  cron: 'annually',
};

export const sampleWithPartialData: IReportSchedule = {
  id: 32639,
  cron: 'jeweller decoy',
};

export const sampleWithFullData: IReportSchedule = {
  id: 30849,
  cron: 'times drat silently',
};

export const sampleWithNewData: NewReportSchedule = {
  cron: 'round',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
