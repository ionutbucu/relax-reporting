import dayjs from 'dayjs/esm';
import { IReport } from 'app/entities/RelaxReporting/report/report.model';

export interface IReportExecution {
  id: number;
  date?: dayjs.Dayjs | null;
  error?: string | null;
  url?: string | null;
  user?: string | null;
  report?: Pick<IReport, 'id'> | null;
}

export type NewReportExecution = Omit<IReportExecution, 'id'> & { id: null };
