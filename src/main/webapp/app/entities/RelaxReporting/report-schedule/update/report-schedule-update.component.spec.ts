import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReport } from 'app/entities/RelaxReporting/report/report.model';
import { ReportService } from 'app/entities/RelaxReporting/report/service/report.service';
import { ReportScheduleService } from '../service/report-schedule.service';
import { IReportSchedule } from '../report-schedule.model';
import { ReportScheduleFormService } from './report-schedule-form.service';

import { ReportScheduleUpdateComponent } from './report-schedule-update.component';

describe('ReportSchedule Management Update Component', () => {
  let comp: ReportScheduleUpdateComponent;
  let fixture: ComponentFixture<ReportScheduleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportScheduleFormService: ReportScheduleFormService;
  let reportScheduleService: ReportScheduleService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportScheduleUpdateComponent],
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
      .overrideTemplate(ReportScheduleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportScheduleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportScheduleFormService = TestBed.inject(ReportScheduleFormService);
    reportScheduleService = TestBed.inject(ReportScheduleService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Report query and add missing value', () => {
      const reportSchedule: IReportSchedule = { id: 456 };
      const report: IReport = { id: 4084 };
      reportSchedule.report = report;

      const reportCollection: IReport[] = [{ id: 4720 }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportSchedule });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportSchedule: IReportSchedule = { id: 456 };
      const report: IReport = { id: 26406 };
      reportSchedule.report = report;

      activatedRoute.data = of({ reportSchedule });
      comp.ngOnInit();

      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.reportSchedule).toEqual(reportSchedule);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSchedule>>();
      const reportSchedule = { id: 123 };
      jest.spyOn(reportScheduleFormService, 'getReportSchedule').mockReturnValue(reportSchedule);
      jest.spyOn(reportScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportSchedule }));
      saveSubject.complete();

      // THEN
      expect(reportScheduleFormService.getReportSchedule).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportScheduleService.update).toHaveBeenCalledWith(expect.objectContaining(reportSchedule));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSchedule>>();
      const reportSchedule = { id: 123 };
      jest.spyOn(reportScheduleFormService, 'getReportSchedule').mockReturnValue({ id: null });
      jest.spyOn(reportScheduleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSchedule: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportSchedule }));
      saveSubject.complete();

      // THEN
      expect(reportScheduleFormService.getReportSchedule).toHaveBeenCalled();
      expect(reportScheduleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportSchedule>>();
      const reportSchedule = { id: 123 };
      jest.spyOn(reportScheduleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportSchedule });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportScheduleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareReport', () => {
      it('Should forward to reportService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(reportService, 'compareReport');
        comp.compareReport(entity, entity2);
        expect(reportService.compareReport).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
