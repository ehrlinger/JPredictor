%macro skip;
// JOB hp.dead.prediction_model.sas
sas8.2 hp.dead.prediction_model.sas
//cp hp.dead.prediction_model.l*\
//   /studies/cardiac/failure/ischemic/comparison/graphs/.
//   /studies/cardiac/ischemic/comparison/cabg_pci/graphs/.
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
 %let STUDY=/studies/cardiac/ischemic/comparison/cabg_pci;
*______________________________________________________________________________;
*                                                                              ;
* Predictive Survival Models for Coronary Intervention
* CABG vs. PCI (BM and DES)
*
*
* Multivariable analysis of time-related death
*
* Using the multivariable equation for death, predict survival
* For three intervention groups;
*   a). CABG 
*   b). PCI-DES
*   c). PCI-BMS
*   
*
* Export estimates for the three models to transport files for blackbox
*______________________________________________________________________________;
*                                                                              ;
  options pagesize=107 linesize=132;
  libname est v8  "&STUDY/estimates";
  libname graphs  "&STUDY/graphs";
  title1 "Predictive Survival Models for Coronary Intervention";
  title2 "CCF, 1995 through 2006, CABG=13,114 and PCI=10,068 (3104 DES, 6964 BMS)";
  title3 "Multivariable Analysis of Death";
  title4 "Preoperative  Models for 3 intervention groups";
*______________________________________________________________________________;
*                                                                              ;
*               M U L T I V A R I A B L E   M O D E L S
*______________________________________________________________________________;
*                                                                              ;
* variables listed in order for CABG model:
* Early Hazard Phase:
*       agee, emgsrg, in_bmi, ln_bmi,
*       hx_cva, hx_chf, hx_copd, HX_RNLDZ, hx_diab,
*       hct_pr, /*crcl_pr,*/
*       afib_pr, mvrgsev, lvef, /*vd3,*/ bpsyst, bpdias
*       
* Late Hazard Phase:
*        age, ln_age, female,
*        in_bmi,  wt,
*        hx_malig, hx_cva, hx_chf, hx_copd, hx_rnldz,
*        hx_diab, hx_smoke,
*        ln_bun, creat_pr, gfr_pr,
*        hct_pr, crcl_pr,
*        afib_pr, lvef, cad_sys, lmt, hx_fcad

*******************************************************************************;
* variables listed in order for PCI-DES model:
* Early Hazard Phase:
*       agee, emgsrg, in_bmi,
*       hx_cva, hx_chf, hx_copd, HX_RNLDZ, hx_diab,
*       crcl_pr, /*hct_pr,*/
*       afib_pr, mvrgrg, lvef, vd3, bpsyst, bpdias
*       
* Late Hazard Phase:
*        agee, female,
*        in_bmi,  
*        hx_malig, hx_cva, hx_chf, hx_copd, hx_rnldz,
*        hx_diab, hx_smoke,
*        ln_bun, ln_creat, gfr_pr,
*        ln_hct, 
*        afib_pr, lvef, cad_sys, lmt, hx_fcad

*******************************************************************************;
* variables listed in order for PCI-BMS model:
* Early Hazard Phase:                 
*       agee, emgsrg, /*in_bmi,*/
*       hx_cva, /*hx_chf,*/ hx_copd, HX_RNLDZ, hx_diab,
*       /*crcl_pr, hct_pr,*/
*       afib_pr, /*mvrgrg*/, lvef, vd3, bpsyst, bpdias
*       
* Constant Hazard Phase:
*        agee, female,
*        ln_bmi,  
*        hx_malig, hx_cva, hx_chf, hx_copd, hx_rnldz,
*        hx_diab, hx_smoke,
*        ln_bun, ln_creat, gfr_pr,
*        /*ln_hct,*/
*        afib_pr, lvef, cad_sys, lmt, hx_fcad,  mvrgrg
*       
* Late Hazard Phase:
          hx_copd, hx_htn, lmt70, in2age
*
*______________________________________________________________________________;
*                                                                              ;
* Definitions of variables in models;                                            
  data design;

* Demography;
  age=62;            /* Age         */
  female=0;          /* Female       */
  ht=170;            /* Height in cm  */
  wt=84;             /* Weight in kg  */
  
* Status;
  emgsrg=0;          /* Emergent status */
  
* Cardiac morbidity;
  afib_pr=0;         /* Atrial Fibrillation (no/yes) (0,1) */
  lvef=50;           /* LV ejection fraction (%)           */
  mvrgsev=1;         /* MV regurgitation grade (0,1,2,3,4) */
  cad_sys=2;         /* Number of coronary systems>=50%  (0,1,2,3) */
  lmt=0.10;          /* LMT disease: stenosis  (0.00-1.00) */
  hx_htn=0;          /* History of Hypertension (no/yes) (0,1) */
  bpsyst=130;        /* Systolic Blood Pressure */
  bpdias=80;         /* Diastolic Blood Pressure */ 
  
* Non-cardiac comorbidity;
  hx_malig=0;           /* History of malignancy    (no/yes) (0,1) */
  hx_cva=0;             /* History of stroke        (no/yes) (0,1) */
  hx_chf=0;             /* History of CHF           (no/yes) (0,1) */
  hx_copd=0;            /* History of COPD          (no/yes) (0,1) */
  hx_diab=0;            /* History of Diabetes      (no/yes) (0,1) */
  hx_smoke=0;           /* History of Smoking       (no/yes) (0,1) */
  hx_rnldz=0;           /* History of renal disease (no/yes) (0,1) */
  hx_fcad=0;            /* Family history of CAD    (no/yes) (0,1) */
  hct_pr=40;            /* Hematocrit (%)    */
  creat_pr=1.1;         /* Creatinine        */
  bun_pr=20;            /* Blood urea nitrogen (mg/dL)  */

  years=1;
*******************************************************************************;
* Set up variable values for a specific patient;
* Make all the missing value flags to 0;
  patient=1;  age=60; 
              afib_pr=0; lvef=50; cad_sys=2; hx_cva=0; hx_smoke=0; hx_rnldz=0; 
              bun_pr=22; creat_pr=0.8;           
  output;
  
  patient=2;  age=72;
              afib_pr=1; lvef=30; cad_sys=3; hx_cva=1; hx_smoke=1; hx_rnldz=1; 
              bun_pr=17; creat_pr=1.4;              
  output;
*******************************************************************************;
* Transformations of variables;
  data design; set design;
  agee=exp(age/50);
  ln_age=log(age);
  in_age=1/(age/50);
  in2age=in_age*in_age;
  
  bmi=wt/((ht/100)**2);
  in_bmi=40/bmi;
  ln_bmi=log(bmi);
  ln_creat=log(creat_pr);
  ln_bun=log(bun_pr);
  ln_hct=log(hct_pr);
  crcl_pr=(140 - age)*wt/(72*creat_pr);
    if female=1 then crcl_pr=0.85*crcl_pr;
  gfr_pr=exp(5.228 - 1.154*ln_creat - 0.203*ln_age - 0.299*female);
  
  vd3=(cad_sys=3);
  lmt70=(lmt>=0.70);
  mvrgrg=(mvrgsev>0);
  
  proc print d;
*______________________________________________________________________________;
*                                                                              ;
* Predictions                                                                  ;
*______________________________________________________________________________;
*                                                                              ;
* Generate and output points, remembering any transforms                       ;
  data predict; set design; digital=0;
  min=-8; max=log(10); inc=(max-min)/999.9;
  do ln_yrs=min to max by inc, max; years=exp(ln_yrs); output; end;
*******************************************************************************;
* Generate time points at "nice" intervals for tabular (digital) depiction     ;
  data digital; set design; digital=1;
  do years=30/365.2425, 3/12, 6/12, 9/12, 1 to 10 by 1; output; end;
*******************************************************************************;
* Transformations of variables                                                 ;
  data predict; set predict digital;
*******************************************************************************;
* Do predictions, exporting at the same time the estimates to transport files;
* CABG model;  
   title6 "CABG Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_cbg_all out=pred_cabg; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_cabg.dta";
  data hmdead_cabg; set est.hmdead_cbg_all;
  data export.export; set hmdead_cabg;
  proc print data=est.hmdead_cbg_all;
  run;

******************************************************************************************;
* DES model;  
 title6 "DES Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_des_all out=pred_des; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_des.dta";
  data hmdead_des; set est.hmdead_des_all;
  data export.export; set hmdead_des;
  proc print data=est.hmdead_des_all;
  run;

******************************************************************************************;
* BMS model;  
 title6 "BMS Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_bms_all out=pred_bms; time years; )
  
  libname export xport "&STUDY/estimates/hmdead_bms.dta";
  data hmdead_bms; set est.hmdead_bms_all;
  data export.export; set hmdead_bms;
  proc print data=est.hmdead_bms_all;
  run; 

*______________________________________________________________________________;
*                                                                              ;
*                                  P L O T S
*______________________________________________________________________________;
*                                                                              ;
* Printer plot survival and hazard                                             ;
 
  title6 "CABG Model";
  proc plot data=pred_cabg;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run;
  
  title6 "DES Model";
  proc plot data=pred_des;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run; 
  
  title6 "BMS Model";
  proc plot data=pred_bms;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
  run; 

  
*******************************************************************************;
* Digital nomogram                                                             ;

  title6 "CABG Model";
  data digital; set pred_cabg; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;
  
  title6 "DES Model";
  data digital; set pred_des; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;  

  title6 "BMS Model";
  data digital; set pred_bms; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;    
  

*******************************************************************************;
