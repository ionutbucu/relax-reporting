import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReportDistributionComponent } from './list/report-distribution.component';
import { ReportDistributionDetailComponent } from './detail/report-distribution-detail.component';
import { ReportDistributionUpdateComponent } from './update/report-distribution-update.component';
import ReportDistributionResolve from './route/report-distribution-routing-resolve.service';

const reportDistributionRoute: Routes = [
  {
    path: '',
    component: ReportDistributionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportDistributionDetailComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportDistributionUpdateComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportDistributionUpdateComponent,
    resolve: {
      reportDistribution: ReportDistributionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportDistributionRoute;
