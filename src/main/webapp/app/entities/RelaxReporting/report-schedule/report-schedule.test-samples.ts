import { IReportSchedule, NewReportSchedule } from './report-schedule.model';

export const sampleWithRequiredData: IReportSchedule = {
  rid: '21bfefc3-0f81-4406-acc7-772b773ffe93',
  cron: 'neatly cooperative',
};

export const sampleWithPartialData: IReportSchedule = {
  rid: '5b047449-abe5-42ac-8052-e508cf21ab54',
  cron: 'discontinue',
};

export const sampleWithFullData: IReportSchedule = {
  rid: '74b0a6e9-4299-45c2-af71-215e7fa4d819',
  cron: 'yuck like',
};

export const sampleWithNewData: NewReportSchedule = {
  cron: 'waffle',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
