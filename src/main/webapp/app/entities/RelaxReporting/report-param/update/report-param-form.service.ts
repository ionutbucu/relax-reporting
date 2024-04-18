import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportParam, NewReportParam } from '../report-param.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportParam for edit and NewReportParamFormGroupInput for create.
 */
type ReportParamFormGroupInput = IReportParam | PartialWithRequiredKeyOf<NewReportParam>;

type ReportParamFormDefaults = Pick<NewReportParam, 'id'>;

type ReportParamFormGroupContent = {
  id: FormControl<IReportParam['id'] | NewReportParam['id']>;
  name: FormControl<IReportParam['name']>;
  type: FormControl<IReportParam['type']>;
  value: FormControl<IReportParam['value']>;
  conversionRule: FormControl<IReportParam['conversionRule']>;
  report: FormControl<IReportParam['report']>;
};

export type ReportParamFormGroup = FormGroup<ReportParamFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportParamFormService {
  createReportParamFormGroup(reportParam: ReportParamFormGroupInput = { id: null }): ReportParamFormGroup {
    const reportParamRawValue = {
      ...this.getFormDefaults(),
      ...reportParam,
    };
    return new FormGroup<ReportParamFormGroupContent>({
      id: new FormControl(
        { value: reportParamRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(reportParamRawValue.name),
      type: new FormControl(reportParamRawValue.type),
      value: new FormControl(reportParamRawValue.value),
      conversionRule: new FormControl(reportParamRawValue.conversionRule),
      report: new FormControl(reportParamRawValue.report),
    });
  }

  getReportParam(form: ReportParamFormGroup): IReportParam | NewReportParam {
    return form.getRawValue() as IReportParam | NewReportParam;
  }

  resetForm(form: ReportParamFormGroup, reportParam: ReportParamFormGroupInput): void {
    const reportParamRawValue = { ...this.getFormDefaults(), ...reportParam };
    form.reset(
      {
        ...reportParamRawValue,
        id: { value: reportParamRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportParamFormDefaults {
    return {
      id: null,
    };
  }
}
