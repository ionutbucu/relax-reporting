import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportDataSource, NewReportDataSource } from '../report-data-source.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportDataSource for edit and NewReportDataSourceFormGroupInput for create.
 */
type ReportDataSourceFormGroupInput = IReportDataSource | PartialWithRequiredKeyOf<NewReportDataSource>;

type ReportDataSourceFormDefaults = Pick<NewReportDataSource, 'id'>;

type ReportDataSourceFormGroupContent = {
  id: FormControl<IReportDataSource['id'] | NewReportDataSource['id']>;
  type: FormControl<IReportDataSource['type']>;
  url: FormControl<IReportDataSource['url']>;
  user: FormControl<IReportDataSource['user']>;
  password: FormControl<IReportDataSource['password']>;
};

export type ReportDataSourceFormGroup = FormGroup<ReportDataSourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportDataSourceFormService {
  createReportDataSourceFormGroup(reportDataSource: ReportDataSourceFormGroupInput = { id: null }): ReportDataSourceFormGroup {
    const reportDataSourceRawValue = {
      ...this.getFormDefaults(),
      ...reportDataSource,
    };
    return new FormGroup<ReportDataSourceFormGroupContent>({
      id: new FormControl(
        { value: reportDataSourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      type: new FormControl(reportDataSourceRawValue.type),
      url: new FormControl(reportDataSourceRawValue.url),
      user: new FormControl(reportDataSourceRawValue.user),
      password: new FormControl(reportDataSourceRawValue.password),
    });
  }

  getReportDataSource(form: ReportDataSourceFormGroup): IReportDataSource | NewReportDataSource {
    return form.getRawValue() as IReportDataSource | NewReportDataSource;
  }

  resetForm(form: ReportDataSourceFormGroup, reportDataSource: ReportDataSourceFormGroupInput): void {
    const reportDataSourceRawValue = { ...this.getFormDefaults(), ...reportDataSource };
    form.reset(
      {
        ...reportDataSourceRawValue,
        id: { value: reportDataSourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportDataSourceFormDefaults {
    return {
      id: null,
    };
  }
}
