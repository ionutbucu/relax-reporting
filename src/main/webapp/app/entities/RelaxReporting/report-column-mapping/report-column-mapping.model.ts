import { IReport } from 'app/entities/RelaxReporting/report/report.model';

export interface IReportColumnMapping {
  id: number;
  sourceColumnName?: string | null;
  sourceColumnIndex?: number | null;
  columnTitle?: string | null;
  lang?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewReportColumnMapping = Omit<IReportColumnMapping, 'id'> & { id: null };
