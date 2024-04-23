import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'report',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReport.home.title' },
    loadChildren: () => import('./RelaxReporting/report/report.routes'),
  },
  {
    path: 'report-param',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportParam.home.title' },
    loadChildren: () => import('./RelaxReporting/report-param/report-param.routes'),
  },
  {
    path: 'report-data-source',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportDataSource.home.title' },
    loadChildren: () => import('./RelaxReporting/report-data-source/report-data-source.routes'),
  },
  {
    path: 'report-metadata',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportMetadata.home.title' },
    loadChildren: () => import('./RelaxReporting/report-metadata/report-metadata.routes'),
  },
  {
    path: 'report-schedule',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportSchedule.home.title' },
    loadChildren: () => import('./RelaxReporting/report-schedule/report-schedule.routes'),
  },
  {
    path: 'report-distribution',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportDistribution.home.title' },
    loadChildren: () => import('./RelaxReporting/report-distribution/report-distribution.routes'),
  },
  {
    path: 'report-execution',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportExecution.home.title' },
    loadChildren: () => import('./RelaxReporting/report-execution/report-execution.routes'),
  },
  {
    path: 'report-column-mapping',
    data: { pageTitle: 'relaxReportingApp.relaxReportingReportColumnMapping.home.title' },
    loadChildren: () => import('./RelaxReporting/report-column-mapping/report-column-mapping.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
