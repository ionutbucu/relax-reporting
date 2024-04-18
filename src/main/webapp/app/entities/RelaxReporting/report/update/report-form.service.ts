import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReport, NewReport } from '../report.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReport for edit and NewReportFormGroupInput for create.
 */
type ReportFormGroupInput = IReport | PartialWithRequiredKeyOf<NewReport>;

type ReportFormDefaults = Pick<NewReport, 'id'>;

type ReportFormGroupContent = {
  id: FormControl<IReport['id'] | NewReport['id']>;
  name: FormControl<IReport['name']>;
  description: FormControl<IReport['description']>;
  query: FormControl<IReport['query']>;
  queryType: FormControl<IReport['queryType']>;
  fileName: FormControl<IReport['fileName']>;
  reportType: FormControl<IReport['reportType']>;
  datasource: FormControl<IReport['datasource']>;
  metadata: FormControl<IReport['metadata']>;
};

export type ReportFormGroup = FormGroup<ReportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportFormService {
  createReportFormGroup(report: ReportFormGroupInput = { id: null }): ReportFormGroup {
    const reportRawValue = {
      ...this.getFormDefaults(),
      ...report,
    };
    return new FormGroup<ReportFormGroupContent>({
      id: new FormControl(
        { value: reportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportRawValue.name, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      description: new FormControl(reportRawValue.description, {
        validators: [Validators.maxLength(512)],
      }),
      query: new FormControl(reportRawValue.query, {
        validators: [Validators.required, Validators.minLength(10), Validators.maxLength(10000)],
      }),
      queryType: new FormControl(reportRawValue.queryType),
      fileName: new FormControl(reportRawValue.fileName, {
        validators: [Validators.required, Validators.minLength(3)],
      }),
      reportType: new FormControl(reportRawValue.reportType),
      datasource: new FormControl(reportRawValue.datasource),
      metadata: new FormControl(reportRawValue.metadata),
    });
  }

  getReport(form: ReportFormGroup): IReport | NewReport {
    return form.getRawValue() as IReport | NewReport;
  }

  resetForm(form: ReportFormGroup, report: ReportFormGroupInput): void {
    const reportRawValue = { ...this.getFormDefaults(), ...report };
    form.reset(
      {
        ...reportRawValue,
        id: { value: reportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportFormDefaults {
    return {
      id: null,
    };
  }
}
