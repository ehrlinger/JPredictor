%macro skkip;
// JOB hp.dead.prediction_model.sas
sas8.2 hp.dead.prediction_model.sas
//cp hp.dead.prediction_model.l*\
//   /studies/cardiac/failure/ischemic/comparison/graphs/.
//spool cont printer 'lptxt -l110 -s6 -f LetterGothic-Bold'
spool cont to email
splfile hp.dead.prediction_model.l*
// FILE hp.dead.prediction_model.sas
%mend;
*______________________________________________________________________________;
*                                                                              ;
* /hp.dead.prediction_model.sas
*______________________________________________________________________________;
*                                                                              ;
 %let STUDY=/studies/cardiac/failure/ischemic/comparison;
*______________________________________________________________________________;
*                                                                              ;
* Surgical Management for Patients with Ischemic Cardiomyopathy
* (CCF, 1997 to 2006, n=1321)
*
* Multivariable analysis of time-related death
*
* Using the multivariable equation for death, predict survival
* For four surgical management groups;
*   a). CABG alone
*   b). CABG +MV anuloplasty
*   c). DOR
*   d). Heart Transplant  
*   
*
* Export estimates for the four models to transport files for blackbox
*______________________________________________________________________________;
*                                                                              ;
  options pagesize=107 linesize=132;
  libname est v8  "&STUDY/estimates";
  libname graphs  "&STUDY/graphs";
  title1 "Surgical Management for Patients with Ischemic Cardiomyopathy";
  title2 "(CCF, 1997 to 2006, n=1321)";
  title3 "Multivariable Analysis of Death";
  title4 "Preoperative  Models for 4 surgical management groups";
*______________________________________________________________________________;
*                                                                              ;
*               M U L T I V A R I A B L E   M O D E L S
*______________________________________________________________________________;
*                                                                              ;
* variables listed in order:
*
* Early Hazard Phase:
*       nyha34,ln_ef,cac_pr, chb_pr,
*       hx_cva, hx_copd, hx_rnldz
        
* Late Hazard Phase:
*       agee, 
*       ef2, afib_pr,chb_pr, lmtany, rca50, lcx70, avrgrg, in_ivpmi,ivpmi2, ms_pmi,
*       hx_popdz, hx_copd, hx_dmtrt, hx_iddm, in_chol, ms_cholp,
*       hx_rnldz, ln_bun, ln_hct,  ms_hctp
*
*______________________________________________________________________________;
*                                                                              ;
* Definitions of variables in models;                                            
  data design;

* Demography;
  age=62;
  
* Cardiac morbidity;
  iv_pmi=3;          /* Interval (years) from MI to operation              */
  ef_com=20;          /* LV ejection fraction (%)                           */
  nyha34=1;          /* NYHA function class 1/II vs III/IV (binary: 0, 1)  */
  cac_pr=2;          /* Cadian Angina Class:possible values: (0, 1, 2, 3, 4) */
  afib_pr=0;         /* Atrial Fibrillation (no/yes) (0,1) */
  chb_pr=0;          /* Complete heart block/pacer (no/yes) (0,1) */
  avrgrg=0;          /* AV regurgitation (no/yes) (0, 1) */
  lmtany=1;          /* LMT disease: stenosis>0%  (no/yes) (0, 1) */
  rca50=1;           /* RCA disease: stenosis>=50%  (no/yes) (0, 1) */
  lcx70=1;           /* LCX disease: stenosis>=70%  (no/yes) (0, 1) */
  
  ms_pmi=0;          /* missing value flag */
  ms_cadsy=0;        /* missing value flag */

* Non-cardiac comorbidity;
  hx_cva=0;             /* History of stroke (no/yes) (0, 1) */
  hx_copd=0;            /* History of COPD (no/yes) (0, 1) */
  hx_popdz=0;           /* History of popliteal disease (no/yes) (0, 1) */
  hx_dmtrt=0;           /* History of treated diabetes (no/yes) (0, 1) */
  hx_iddm=0;            /* History of insulin dependent diabetes (no/yes) (0, 1) */
  chol_pr=168;           /* Cholesterol (mg/dL)                       */
  hct_pr=37;            /* Hematocrit (%)                       */
  hx_rnldz=0;           /* History of renal disease (no/yes) (0, 1) */
  bun_pr=24;            /* Blood urea nitrogen (mg/dL)                       */

  ms_cholp=0;           /* missing value flag */
  ms_hctp=0;            /* missing value flag */
  years=1;

*******************************************************************************;
* Set up variable values for a specific patient;
* Two last variables are for prognostic model;
  patient=1;  age=60; 
              iv_pmi=3; ef_com=20; nyha34=1; cac_pr=2; afib_pr=0; chb_pr=0;
              avrgrg=0; lmtany=1; rca50=1; lcx70=1; ms_pmi=0; ms_cadsy=0;  

              hx_cva=0; hx_copd=0; hx_popdz=0; hx_dmtrt=0; hx_iddm=0; chol_pr=168;  
              hct_pr=37; hx_rnldz=0; bun_pr=24; ms_cholp=0; ms_hctp=0;           
  output;
  
  patient=2;  age=75; 
              iv_pmi=5; ef_com=15; nyha34=1; cac_pr=2; afib_pr=0; chb_pr=0;
              avrgrg=0; lmtany=1; rca50=1; lcx70=1; ms_pmi=0; ms_cadsy=0;  

              hx_cva=1; hx_copd=0; hx_popdz=0; hx_dmtrt=0; hx_iddm=0; chol_pr=200;  
              hct_pr=37; hx_rnldz=0; bun_pr=30; ms_cholp=0; ms_hctp=0;           
  output;
*******************************************************************************;
* Transformations of variables;
  data design; set design;
  agee=exp(age/50);
  ln_ef=log(ef_com);
  ef2=(ef_com/20)**2;
  ivpmi2=(iv_pmi/3)**2;
  in_ivpmi=(1/(iv_pmi+1));
  in_chol=(100/chol_pr);
  ln_bun=log(bun_pr);
  ln_hct=log(hct_pr);
  proc print d;
*______________________________________________________________________________;
*                                                                              ;
* Predictions                                                                  ;
*______________________________________________________________________________;
*                                                                              ;
* Generate and output points, remembering any transforms                       ;
  data predict; set design; digital=0;
  min=-8; max=log(9); inc=(max-min)/999.9;
  do ln_yrs=min to max by inc, max; years=exp(ln_yrs); output; end;
*******************************************************************************;
* Generate time points at "nice" intervals for tabular (digital) depiction     ;
  data digital; set design; digital=1;
  do years=30/365.2425, 3/12, 6/12, 9/12, 1 to 9 by 1; output; end;
*******************************************************************************;
* Transformations of variables                                                 ;
  data predict; set predict digital;
*******************************************************************************;
* Do predictions, exporting at the same time the estimates to transport files;
* CABG alone model;  
   title6 "CABG alone Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_cabg out=pred_cabg; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_cabg.dta";
  data hmdead_cabg; set est.hmdead_cabg;
  data export.export; set hmdead_cabg;
  proc print data=est.hmdead_cabg;
  run;

******************************************************************************************;
* CABG+MV anuloplasty model;  
title6 "CABG + MV anuloplasty Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_cbgv out=pred_cbgv; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_cbgv.dta";
  data hmdead_cbgv; set est.hmdead_cbgv;
  data export.export; set hmdead_cbgv;
  proc print data=est.hmdead_cbgv;
  run;

******************************************************************************************;
* DOR model;  
 title6 "DOR Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_dor out=pred_dor; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_dor.dta";
  data hmdead_dor; set est.hmdead_dor;
  data export.export; set hmdead_dor;
  proc print data=est.hmdead_dor;
  run; 

******************************************************************************************;
* Heart Transplant model;  
  title6 "Heart Transplant Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_txpl out=pred_txpl; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_txpl.dta";
  data hmdead_txpl; set est.hmdead_txpl;
  data export.export; set hmdead_txpl;
  proc print data=est.hmdead_txpl;
  run;  
 
******************************************************************************************;
******************************************************************************************; 
 
  /* did not work
  filename import "&STUDY/estimates/hmpodead.dta";
  proc cimport library=est infile=import;
  proc print data=hmpodead;
  run;

  libname graphs  "&STUDY/graphs";
  libname import xport "&STUDY/estimates/hmpodead.dta";
  proc copy in=import out=graphs;
  proc print data=graphs.export;
  run;
  */


  /* Made an file called export.sas7bdat in /graphs */
*______________________________________________________________________________;
*                                                                              ;
*                                  P L O T S
*______________________________________________________________________________;
*                                                                              ;
* Printer plot survival and hazard                                             ;
  
  title6 "CABG alone Model";
  proc plot data=pred_cabg;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run;
  
  title6 "CABG + MV anuloplasty Model";
  proc plot data=pred_cbgv;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run; 
  
  title6 "DOR Model";
  proc plot data=pred_dor;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run; 
  
  title6 "Heart Transplant Model";
  proc plot data=pred_txpl;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run; 
  
*******************************************************************************;
* Digital nomogram                                                             ;
  title6 "CABG alone Model";
  data digital; set pred_cabg; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;
  
  title6 "CABG+MV anuloplasty Model";
  data digital; set pred_cbgv; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;  

  title6 "DOR Model";
  data digital; set pred_dor; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;    
  
  title6 "Heart Transplant Model";
  data digital; set pred_txpl; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;  

*******************************************************************************;
