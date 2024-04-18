import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportColumnMapping, NewReportColumnMapping } from '../report-column-mapping.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportColumnMapping for edit and NewReportColumnMappingFormGroupInput for create.
 */
type ReportColumnMappingFormGroupInput = IReportColumnMapping | PartialWithRequiredKeyOf<NewReportColumnMapping>;

type ReportColumnMappingFormDefaults = Pick<NewReportColumnMapping, 'id'>;

type ReportColumnMappingFormGroupContent = {
  id: FormControl<IReportColumnMapping['id'] | NewReportColumnMapping['id']>;
  sourceColumnName: FormControl<IReportColumnMapping['sourceColumnName']>;
  sourceColumnIndex: FormControl<IReportColumnMapping['sourceColumnIndex']>;
  columnTitle: FormControl<IReportColumnMapping['columnTitle']>;
  lang: FormControl<IReportColumnMapping['lang']>;
  report: FormControl<IReportColumnMapping['report']>;
};

export type ReportColumnMappingFormGroup = FormGroup<ReportColumnMappingFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportColumnMappingFormService {
  createReportColumnMappingFormGroup(reportColumnMapping: ReportColumnMappingFormGroupInput = { id: null }): ReportColumnMappingFormGroup {
    const reportColumnMappingRawValue = {
      ...this.getFormDefaults(),
      ...reportColumnMapping,
    };
    return new FormGroup<ReportColumnMappingFormGroupContent>({
      id: new FormControl(
        { value: reportColumnMappingRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceColumnName: new FormControl(reportColumnMappingRawValue.sourceColumnName),
      sourceColumnIndex: new FormControl(reportColumnMappingRawValue.sourceColumnIndex),
      columnTitle: new FormControl(reportColumnMappingRawValue.columnTitle),
      lang: new FormControl(reportColumnMappingRawValue.lang),
      report: new FormControl(reportColumnMappingRawValue.report),
    });
  }

  getReportColumnMapping(form: ReportColumnMappingFormGroup): IReportColumnMapping | NewReportColumnMapping {
    return form.getRawValue() as IReportColumnMapping | NewReportColumnMapping;
  }

  resetForm(form: ReportColumnMappingFormGroup, reportColumnMapping: ReportColumnMappingFormGroupInput): void {
    const reportColumnMappingRawValue = { ...this.getFormDefaults(), ...reportColumnMapping };
    form.reset(
      {
        ...reportColumnMappingRawValue,
        id: { value: reportColumnMappingRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportColumnMappingFormDefaults {
    return {
      id: null,
    };
  }
}
