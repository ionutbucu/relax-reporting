import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReport } from 'app/entities/RelaxReporting/report/report.model';
import { ReportService } from 'app/entities/RelaxReporting/report/service/report.service';
import { ReportExecutionService } from '../service/report-execution.service';
import { IReportExecution } from '../report-execution.model';
import { ReportExecutionFormService } from './report-execution-form.service';

import { ReportExecutionUpdateComponent } from './report-execution-update.component';

describe('ReportExecution Management Update Component', () => {
  let comp: ReportExecutionUpdateComponent;
  let fixture: ComponentFixture<ReportExecutionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportExecutionFormService: ReportExecutionFormService;
  let reportExecutionService: ReportExecutionService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportExecutionUpdateComponent],
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
      .overrideTemplate(ReportExecutionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportExecutionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportExecutionFormService = TestBed.inject(ReportExecutionFormService);
    reportExecutionService = TestBed.inject(ReportExecutionService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Report query and add missing value', () => {
      const reportExecution: IReportExecution = { rid: 'CBA' };
      const report: IReport = { rid: '9daf8b69-32b8-49d0-95e6-29bbbd09308c' };
      reportExecution.report = report;

      const reportCollection: IReport[] = [{ rid: '08f2c8fa-9e3e-4033-8add-b53ba795d6fd' }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportExecution: IReportExecution = { rid: 'CBA' };
      const report: IReport = { rid: '69c78e4f-01e1-441f-a558-ebe74ab2f828' };
      reportExecution.report = report;

      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.reportExecution).toEqual(reportExecution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { rid: 'ABC' };
      jest.spyOn(reportExecutionFormService, 'getReportExecution').mockReturnValue(reportExecution);
      jest.spyOn(reportExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportExecution }));
      saveSubject.complete();

      // THEN
      expect(reportExecutionFormService.getReportExecution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportExecutionService.update).toHaveBeenCalledWith(expect.objectContaining(reportExecution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { rid: 'ABC' };
      jest.spyOn(reportExecutionFormService, 'getReportExecution').mockReturnValue({ rid: null });
      jest.spyOn(reportExecutionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportExecution }));
      saveSubject.complete();

      // THEN
      expect(reportExecutionFormService.getReportExecution).toHaveBeenCalled();
      expect(reportExecutionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportExecution>>();
      const reportExecution = { rid: 'ABC' };
      jest.spyOn(reportExecutionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportExecution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportExecutionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReport', () => {
      it('Should forward to reportService', () => {
        const entity = { rid: 'ABC' };
        const entity2 = { rid: 'CBA' };
        jest.spyOn(reportService, 'compareReport');
        comp.compareReport(entity, entity2);
        expect(reportService.compareReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
