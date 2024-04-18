export interface IReportMetadata {
  id: number;
  metadata?: string | null;
}

export type NewReportMetadata = Omit<IReportMetadata, 'id'> & { id: null };
