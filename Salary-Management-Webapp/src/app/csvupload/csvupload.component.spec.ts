import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CSVuploadComponent } from './csvupload.component';

describe('CSVuploadComponent', () => {
  let component: CSVuploadComponent;
  let fixture: ComponentFixture<CSVuploadComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CSVuploadComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CSVuploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
