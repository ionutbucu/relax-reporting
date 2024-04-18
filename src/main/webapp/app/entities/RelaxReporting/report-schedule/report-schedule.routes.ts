import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ReportScheduleComponent } from './list/report-schedule.component';
import { ReportScheduleDetailComponent } from './detail/report-schedule-detail.component';
import { ReportScheduleUpdateComponent } from './update/report-schedule-update.component';
import ReportScheduleResolve from './route/report-schedule-routing-resolve.service';

const reportScheduleRoute: Routes = [
  {
    path: '',
    component: ReportScheduleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportScheduleDetailComponent,
    resolve: {
      reportSchedule: ReportScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportScheduleUpdateComponent,
    resolve: {
      reportSchedule: ReportScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportScheduleUpdateComponent,
    resolve: {
      reportSchedule: ReportScheduleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default reportScheduleRoute;
