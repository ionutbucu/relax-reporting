import { IReportDataSource } from 'app/entities/RelaxReporting/report-data-source/report-data-source.model';
import { IReportMetadata } from 'app/entities/RelaxReporting/report-metadata/report-metadata.model';
import { QueryType } from 'app/entities/enumerations/query-type.model';
import { ReportType } from 'app/entities/enumerations/report-type.model';

export interface IReport {
  id: number;
  name?: string | null;
  description?: string | null;
  query?: string | null;
  queryType?: keyof typeof QueryType | null;
  fileName?: string | null;
  reportType?: keyof typeof ReportType | null;
  datasource?: Pick<IReportDataSource, 'id'> | null;
  metadata?: Pick<IReportMetadata, 'id'> | null;
}

export type NewReport = Omit<IReport, 'id'> & { id: null };
