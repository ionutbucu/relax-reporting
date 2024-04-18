import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReportExecutionComponent } from './list/report-execution.component';
import { ReportExecutionDetailComponent } from './detail/report-execution-detail.component';
import { ReportExecutionUpdateComponent } from './update/report-execution-update.component';
import ReportExecutionResolve from './route/report-execution-routing-resolve.service';

const reportExecutionRoute: Routes = [
  {
    path: '',
    component: ReportExecutionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportExecutionDetailComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportExecutionUpdateComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportExecutionUpdateComponent,
    resolve: {
      reportExecution: ReportExecutionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportExecutionRoute;
