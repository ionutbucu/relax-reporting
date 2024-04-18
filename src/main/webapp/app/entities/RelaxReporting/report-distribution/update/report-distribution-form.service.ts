import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportDistribution, NewReportDistribution } from '../report-distribution.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportDistribution for edit and NewReportDistributionFormGroupInput for create.
 */
type ReportDistributionFormGroupInput = IReportDistribution | PartialWithRequiredKeyOf<NewReportDistribution>;

type ReportDistributionFormDefaults = Pick<NewReportDistribution, 'id'>;

type ReportDistributionFormGroupContent = {
  id: FormControl<IReportDistribution['id'] | NewReportDistribution['id']>;
  email: FormControl<IReportDistribution['email']>;
  description: FormControl<IReportDistribution['description']>;
  report: FormControl<IReportDistribution['report']>;
};

export type ReportDistributionFormGroup = FormGroup<ReportDistributionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportDistributionFormService {
  createReportDistributionFormGroup(reportDistribution: ReportDistributionFormGroupInput = { id: null }): ReportDistributionFormGroup {
    const reportDistributionRawValue = {
      ...this.getFormDefaults(),
      ...reportDistribution,
    };
    return new FormGroup<ReportDistributionFormGroupContent>({
      id: new FormControl(
        { value: reportDistributionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      email: new FormControl(reportDistributionRawValue.email, {
        validators: [Validators.required, Validators.minLength(3), Validators.pattern('^[^@]+@[^@]+$')],
      }),
      description: new FormControl(reportDistributionRawValue.description, {
        validators: [Validators.maxLength(256)],
      }),
      report: new FormControl(reportDistributionRawValue.report),
    });
  }

  getReportDistribution(form: ReportDistributionFormGroup): IReportDistribution | NewReportDistribution {
    return form.getRawValue() as IReportDistribution | NewReportDistribution;
  }

  resetForm(form: ReportDistributionFormGroup, reportDistribution: ReportDistributionFormGroupInput): void {
    const reportDistributionRawValue = { ...this.getFormDefaults(), ...reportDistribution };
    form.reset(
      {
        ...reportDistributionRawValue,
        id: { value: reportDistributionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportDistributionFormDefaults {
    return {
      id: null,
    };
  }
}
