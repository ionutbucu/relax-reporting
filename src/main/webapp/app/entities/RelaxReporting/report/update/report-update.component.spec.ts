import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReportDataSource } from 'app/entities/RelaxReporting/report-data-source/report-data-source.model';
import { ReportDataSourceService } from 'app/entities/RelaxReporting/report-data-source/service/report-data-source.service';
import { IReportMetadata } from 'app/entities/RelaxReporting/report-metadata/report-metadata.model';
import { ReportMetadataService } from 'app/entities/RelaxReporting/report-metadata/service/report-metadata.service';
import { IReport } from '../report.model';
import { ReportService } from '../service/report.service';
import { ReportFormService } from './report-form.service';

import { ReportUpdateComponent } from './report-update.component';

describe('Report Management Update Component', () => {
  let comp: ReportUpdateComponent;
  let fixture: ComponentFixture<ReportUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportFormService: ReportFormService;
  let reportService: ReportService;
  let reportDataSourceService: ReportDataSourceService;
  let reportMetadataService: ReportMetadataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReportUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportFormService = TestBed.inject(ReportFormService);
    reportService = TestBed.inject(ReportService);
    reportDataSourceService = TestBed.inject(ReportDataSourceService);
    reportMetadataService = TestBed.inject(ReportMetadataService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call datasource query and add missing value', () => {
      const report: IReport = { id: 456 };
      const datasource: IReportDataSource = { id: 1352 };
      report.datasource = datasource;

      const datasourceCollection: IReportDataSource[] = [{ id: 17555 }];
      jest.spyOn(reportDataSourceService, 'query').mockReturnValue(of(new HttpResponse({ body: datasourceCollection })));
      const expectedCollection: IReportDataSource[] = [datasource, ...datasourceCollection];
      jest.spyOn(reportDataSourceService, 'addReportDataSourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(reportDataSourceService.query).toHaveBeenCalled();
      expect(reportDataSourceService.addReportDataSourceToCollectionIfMissing).toHaveBeenCalledWith(datasourceCollection, datasource);
      expect(comp.datasourcesCollection).toEqual(expectedCollection);
    });

    it('Should call metadata query and add missing value', () => {
      const report: IReport = { id: 456 };
      const metadata: IReportMetadata = { id: 25637 };
      report.metadata = metadata;

      const metadataCollection: IReportMetadata[] = [{ id: 29707 }];
      jest.spyOn(reportMetadataService, 'query').mockReturnValue(of(new HttpResponse({ body: metadataCollection })));
      const expectedCollection: IReportMetadata[] = [metadata, ...metadataCollection];
      jest.spyOn(reportMetadataService, 'addReportMetadataToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(reportMetadataService.query).toHaveBeenCalled();
      expect(reportMetadataService.addReportMetadataToCollectionIfMissing).toHaveBeenCalledWith(metadataCollection, metadata);
      expect(comp.metadataCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const report: IReport = { id: 456 };
      const datasource: IReportDataSource = { id: 4786 };
      report.datasource = datasource;
      const metadata: IReportMetadata = { id: 22121 };
      report.metadata = metadata;

      activatedRoute.data = of({ report });
      comp.ngOnInit();

      expect(comp.datasourcesCollection).toContain(datasource);
      expect(comp.metadataCollection).toContain(metadata);
      expect(comp.report).toEqual(report);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue(report);
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportService.update).toHaveBeenCalledWith(expect.objectContaining(report));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue({ id: null });
      jest.spyOn(reportService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: report }));
      saveSubject.complete();

      // THEN
      expect(reportFormService.getReport).toHaveBeenCalled();
      expect(reportService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReport>>();
      const report = { id: 123 };
      jest.spyOn(reportService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ report });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReportDataSource', () => {
      it('Should forward to reportDataSourceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(reportDataSourceService, 'compareReportDataSource');
        comp.compareReportDataSource(entity, entity2);
        expect(reportDataSourceService.compareReportDataSource).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReportMetadata', () => {
      it('Should forward to reportMetadataService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(reportMetadataService, 'compareReportMetadata');
        comp.compareReportMetadata(entity, entity2);
        expect(reportMetadataService.compareReportMetadata).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
