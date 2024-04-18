import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { ReportColumnMappingDetailComponent } from './report-column-mapping-detail.component';

describe('ReportColumnMapping Management Detail Component', () => {
  let comp: ReportColumnMappingDetailComponent;
  let fixture: ComponentFixture<ReportColumnMappingDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportColumnMappingDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ReportColumnMappingDetailComponent,
              resolve: { reportColumnMapping: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ReportColumnMappingDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ReportColumnMappingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reportColumnMapping on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ReportColumnMappingDetailComponent);

      // THEN
      expect(instance.reportColumnMapping()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
