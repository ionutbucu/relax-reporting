import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReportDataSourceComponent } from './list/report-data-source.component';
import { ReportDataSourceDetailComponent } from './detail/report-data-source-detail.component';
import { ReportDataSourceUpdateComponent } from './update/report-data-source-update.component';
import ReportDataSourceResolve from './route/report-data-source-routing-resolve.service';

const reportDataSourceRoute: Routes = [
  {
    path: '',
    component: ReportDataSourceComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportDataSourceDetailComponent,
    resolve: {
      reportDataSource: ReportDataSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportDataSourceUpdateComponent,
    resolve: {
      reportDataSource: ReportDataSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportDataSourceUpdateComponent,
    resolve: {
      reportDataSource: ReportDataSourceResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportDataSourceRoute;
