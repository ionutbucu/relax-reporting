import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IReport } from '../report.model';

@Component({
  standalone: true,
  selector: 'jhi-report-detail',
  templateUrl: './report-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class ReportDetailComponent {
  report = input<IReport | null>(null);

  previousState(): void {
    window.history.back();
  }
}
