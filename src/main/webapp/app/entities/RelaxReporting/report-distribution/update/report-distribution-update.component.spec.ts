import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject, from } from 'rxjs';

import { IReport } from 'app/entities/RelaxReporting/report/report.model';
import { ReportService } from 'app/entities/RelaxReporting/report/service/report.service';
import { ReportDistributionService } from '../service/report-distribution.service';
import { IReportDistribution } from '../report-distribution.model';
import { ReportDistributionFormService } from './report-distribution-form.service';

import { ReportDistributionUpdateComponent } from './report-distribution-update.component';

describe('ReportDistribution Management Update Component', () => {
  let comp: ReportDistributionUpdateComponent;
  let fixture: ComponentFixture<ReportDistributionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportDistributionFormService: ReportDistributionFormService;
  let reportDistributionService: ReportDistributionService;
  let reportService: ReportService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, ReportDistributionUpdateComponent],
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
      .overrideTemplate(ReportDistributionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportDistributionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportDistributionFormService = TestBed.inject(ReportDistributionFormService);
    reportDistributionService = TestBed.inject(ReportDistributionService);
    reportService = TestBed.inject(ReportService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Report query and add missing value', () => {
      const reportDistribution: IReportDistribution = { id: 456 };
      const report: IReport = { id: 32703 };
      reportDistribution.report = report;

      const reportCollection: IReport[] = [{ id: 27786 }];
      jest.spyOn(reportService, 'query').mockReturnValue(of(new HttpResponse({ body: reportCollection })));
      const additionalReports = [report];
      const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
      jest.spyOn(reportService, 'addReportToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ reportDistribution });
      comp.ngOnInit();

      expect(reportService.query).toHaveBeenCalled();
      expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(
        reportCollection,
        ...additionalReports.map(expect.objectContaining),
      );
      expect(comp.reportsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const reportDistribution: IReportDistribution = { id: 456 };
      const report: IReport = { id: 6646 };
      reportDistribution.report = report;

      activatedRoute.data = of({ reportDistribution });
      comp.ngOnInit();

      expect(comp.reportsSharedCollection).toContain(report);
      expect(comp.reportDistribution).toEqual(reportDistribution);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDistribution>>();
      const reportDistribution = { id: 123 };
      jest.spyOn(reportDistributionFormService, 'getReportDistribution').mockReturnValue(reportDistribution);
      jest.spyOn(reportDistributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDistribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDistribution }));
      saveSubject.complete();

      // THEN
      expect(reportDistributionFormService.getReportDistribution).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportDistributionService.update).toHaveBeenCalledWith(expect.objectContaining(reportDistribution));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDistribution>>();
      const reportDistribution = { id: 123 };
      jest.spyOn(reportDistributionFormService, 'getReportDistribution').mockReturnValue({ id: null });
      jest.spyOn(reportDistributionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDistribution: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reportDistribution }));
      saveSubject.complete();

      // THEN
      expect(reportDistributionFormService.getReportDistribution).toHaveBeenCalled();
      expect(reportDistributionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReportDistribution>>();
      const reportDistribution = { id: 123 };
      jest.spyOn(reportDistributionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reportDistribution });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportDistributionService.update).toHaveBeenCalled();
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
