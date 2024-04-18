import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IReportMetadata, NewReportMetadata } from '../report-metadata.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IReportMetadata for edit and NewReportMetadataFormGroupInput for create.
 */
type ReportMetadataFormGroupInput = IReportMetadata | PartialWithRequiredKeyOf<NewReportMetadata>;

type ReportMetadataFormDefaults = Pick<NewReportMetadata, 'id'>;

type ReportMetadataFormGroupContent = {
  id: FormControl<IReportMetadata['id'] | NewReportMetadata['id']>;
  metadata: FormControl<IReportMetadata['metadata']>;
};

export type ReportMetadataFormGroup = FormGroup<ReportMetadataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ReportMetadataFormService {
  createReportMetadataFormGroup(reportMetadata: ReportMetadataFormGroupInput = { id: null }): ReportMetadataFormGroup {
    const reportMetadataRawValue = {
      ...this.getFormDefaults(),
      ...reportMetadata,
    };
    return new FormGroup<ReportMetadataFormGroupContent>({
      id: new FormControl(
        { value: reportMetadataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      metadata: new FormControl(reportMetadataRawValue.metadata),
    });
  }

  getReportMetadata(form: ReportMetadataFormGroup): IReportMetadata | NewReportMetadata {
    return form.getRawValue() as IReportMetadata | NewReportMetadata;
  }

  resetForm(form: ReportMetadataFormGroup, reportMetadata: ReportMetadataFormGroupInput): void {
    const reportMetadataRawValue = { ...this.getFormDefaults(), ...reportMetadata };
    form.reset(
      {
        ...reportMetadataRawValue,
        id: { value: reportMetadataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ReportMetadataFormDefaults {
    return {
      id: null,
    };
  }
}
