import { IReport } from 'app/entities/RelaxReporting/report/report.model';

export interface IReportParam {
  id: number;
  name?: string | null;
  type?: string | null;
  value?: string | null;
  conversionRule?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewReportParam = Omit<IReportParam, 'id'> & { id: null };
