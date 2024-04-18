export interface IReportDataSource {
  id: number;
  type?: string | null;
  url?: string | null;
  user?: string | null;
  password?: string | null;
}

export type NewReportDataSource = Omit<IReportDataSource, 'id'> & { id: null };
