import { IReport } from 'app/entities/RelaxReporting/report/report.model';

export interface IReportSchedule {
  id: number;
  cron?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewReportSchedule = Omit<IReportSchedule, 'id'> & { id: null };
