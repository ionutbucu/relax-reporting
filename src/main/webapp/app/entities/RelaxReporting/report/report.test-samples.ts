import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  id: 22791,
  name: 'wrongly clamp',
  query: 'down geez worth',
  fileName: 'exhaustion plastic jellyfish',
};

export const sampleWithPartialData: IReport = {
  id: 14543,
  name: 'annually unfortunately',
  query: 'atopXXXXXX',
  queryType: 'HQL',
  fileName: 'dispute',
};

export const sampleWithFullData: IReport = {
  id: 32414,
  name: 'recede',
  description: 'basin',
  query: 'wander gadzooks hm',
  queryType: 'NATIVE_QUERY',
  fileName: 'unnatural party confess',
  reportType: 'XLS',
};

export const sampleWithNewData: NewReport = {
  name: 'quirkily giant',
  query: 'aboveXXXXX',
  fileName: 'ewX',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
