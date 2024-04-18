import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IReportColumnMapping, NewReportColumnMapping } from '../report-column-mapping.model';

export type PartialUpdateReportColumnMapping = Partial<IReportColumnMapping> & Pick<IReportColumnMapping, 'id'>;

export type EntityResponseType = HttpResponse<IReportColumnMapping>;
export type EntityArrayResponseType = HttpResponse<IReportColumnMapping[]>;

@Injectable({ providedIn: 'root' })
export class ReportColumnMappingService {
  protected http = inject(HttpClient);
  protected applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/report-column-mappings', 'relaxreporting');

  create(reportColumnMapping: NewReportColumnMapping): Observable<EntityResponseType> {
    return this.http.post<IReportColumnMapping>(this.resourceUrl, reportColumnMapping, { observe: 'response' });
  }

  update(reportColumnMapping: IReportColumnMapping): Observable<EntityResponseType> {
    return this.http.put<IReportColumnMapping>(
      `${this.resourceUrl}/${this.getReportColumnMappingIdentifier(reportColumnMapping)}`,
      reportColumnMapping,
      { observe: 'response' },
    );
  }

  partialUpdate(reportColumnMapping: PartialUpdateReportColumnMapping): Observable<EntityResponseType> {
    return this.http.patch<IReportColumnMapping>(
      `${this.resourceUrl}/${this.getReportColumnMappingIdentifier(reportColumnMapping)}`,
      reportColumnMapping,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IReportColumnMapping>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IReportColumnMapping[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getReportColumnMappingIdentifier(reportColumnMapping: Pick<IReportColumnMapping, 'id'>): number {
    return reportColumnMapping.id;
  }

  compareReportColumnMapping(o1: Pick<IReportColumnMapping, 'id'> | null, o2: Pick<IReportColumnMapping, 'id'> | null): boolean {
    return o1 && o2 ? this.getReportColumnMappingIdentifier(o1) === this.getReportColumnMappingIdentifier(o2) : o1 === o2;
  }

  addReportColumnMappingToCollectionIfMissing<Type extends Pick<IReportColumnMapping, 'id'>>(
    reportColumnMappingCollection: Type[],
    ...reportColumnMappingsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const reportColumnMappings: Type[] = reportColumnMappingsToCheck.filter(isPresent);
    if (reportColumnMappings.length > 0) {
      const reportColumnMappingCollectionIdentifiers = reportColumnMappingCollection.map(reportColumnMappingItem =>
        this.getReportColumnMappingIdentifier(reportColumnMappingItem),
      );
      const reportColumnMappingsToAdd = reportColumnMappings.filter(reportColumnMappingItem => {
        const reportColumnMappingIdentifier = this.getReportColumnMappingIdentifier(reportColumnMappingItem);
        if (reportColumnMappingCollectionIdentifiers.includes(reportColumnMappingIdentifier)) {
          return false;
        }
        reportColumnMappingCollectionIdentifiers.push(reportColumnMappingIdentifier);
        return true;
      });
      return [...reportColumnMappingsToAdd, ...reportColumnMappingCollection];
    }
    return reportColumnMappingCollection;
  }
}
