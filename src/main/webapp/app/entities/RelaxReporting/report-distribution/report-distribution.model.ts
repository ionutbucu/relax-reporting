import { IReport } from 'app/entities/RelaxReporting/report/report.model';

export interface IReportDistribution {
  id: number;
  email?: string | null;
  description?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewReportDistribution = Omit<IReportDistribution, 'id'> & { id: null };
