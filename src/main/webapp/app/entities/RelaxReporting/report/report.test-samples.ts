import { IReport, NewReport } from './report.model';

export const sampleWithRequiredData: IReport = {
  rid: 'ae204f9d-41b6-42f3-9642-8769b1818399',
  name: 'fortify lest',
  query: 'tomorrow failing monthly',
  fileName: 'unfortunately',
};

export const sampleWithPartialData: IReport = {
  rid: '74a06296-0e4f-42bb-bb53-5b131b5441a4',
  cid: 'incidentally slum',
  name: 'near',
  description: 'who',
  query: 'gabXXXXXXX',
  fileName: 'babushka',
  licenseHolder: 'woeful',
};

export const sampleWithFullData: IReport = {
  rid: 'b85125a3-6604-4ba5-b866-45e412cfd339',
  cid: 'righteously',
  name: 'hunger jaggedly consequently',
  description: 'the stratify',
  query: 'boar provided',
  queryType: 'HQL',
  fileName: 'blot barring',
  reportType: 'CSV',
  licenseHolder: 'unsteady',
  owner: 'duh wonderfully until',
};

export const sampleWithNewData: NewReport = {
  name: 'high-level urgently',
  query: 'jubilantly zowie low',
  fileName: 'gosh urban',
  rid: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
