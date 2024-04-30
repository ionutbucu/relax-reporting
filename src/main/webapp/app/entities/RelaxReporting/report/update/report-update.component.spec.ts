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
      const report: IReport = { rid: 'CBA' };
      const datasource: IReportDataSource = { rid: '082fdfe3-389c-4238-8107-3080b66f427b' };
      report.datasource = datasource;

      const datasourceCollection: IReportDataSource[] = [{ rid: '04b8696c-7212-4189-943c-f972d7b5bb79' }];
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
      const report: IReport = { rid: 'CBA' };
      const metadata: IReportMetadata = { rid: 'ceaa820c-595f-4f80-bca7-3d0b5c542829' };
      report.metadata = metadata;

      const metadataCollection: IReportMetadata[] = [{ rid: 'fd2689d7-dc01-484c-825a-0825a6e75455' }];
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
      const report: IReport = { rid: 'CBA' };
      const datasource: IReportDataSource = { rid: '43547425-acba-455c-8949-1bcbeac16b27' };
      report.datasource = datasource;
      const metadata: IReportMetadata = { rid: '4bcf2489-0740-43d2-ad1f-590da54e8a86' };
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
      const report = { rid: 'ABC' };
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
      const report = { rid: 'ABC' };
      jest.spyOn(reportFormService, 'getReport').mockReturnValue({ rid: null });
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
      const report = { rid: 'ABC' };
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
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportDataSourceService, 'compareReportDataSource');
        comp.compareReportDataSource(entity, entity2);
        expect(reportDataSourceService.compareReportDataSource).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareReportMetadata', () => {
      it('Should forward to reportMetadataService', () => {
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportMetadataService, 'compareReportMetadata');
        comp.compareReportMetadata(entity, entity2);
        expect(reportMetadataService.compareReportMetadata).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
