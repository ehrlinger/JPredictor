%macro skkip;
// JOB hp.dead.patients.sas
sas8.2 hp.dead.patients.sas
//cp hp.dead.patients.l*\
//   /studies/cardiac/failure/ischemic/comparison/graphs/.
//spool cont printer 'lptxt -l110 -s6 -f LetterGothic-Bold'
spool cont to email
splfile hp.dead.patients.l*
// FILE hp.dead.patients.sas
%mend;
*______________________________________________________________________________;
*                                                                              ;
* /hp.dead.patients.sas
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
  patient=1;  age=75; 
              iv_pmi=10; ef_com=20; nyha34=1; cac_pr=4; afib_pr=1; chb_pr=1;
              avrgrg=1; lmtany=1; rca50=1; lcx70=1; ms_pmi=0; ms_cadsy=0;  

              hx_cva=1; hx_copd=1; hx_popdz=1; hx_dmtrt=1; hx_iddm=0; chol_pr=180;  
              hct_pr=28; hx_rnldz=1; bun_pr=35; ms_cholp=0; ms_hctp=0;           
  output;
  
  patient=2;  age=50; 
              iv_pmi=5; ef_com=25; nyha34=0; cac_pr=3; afib_pr=0; chb_pr=0;
              avrgrg=1; lmtany=0; rca50=1; lcx70=0; ms_pmi=0; ms_cadsy=0;  

              hx_cva=0; hx_copd=1; hx_popdz=0; hx_dmtrt=0; hx_iddm=0; chol_pr=150;  
              hct_pr=40; hx_rnldz=0; bun_pr=22; ms_cholp=0; ms_hctp=0;           
  output;
  
  patient=3;  age=60; 
              iv_pmi=12; ef_com=15; nyha34=1; cac_pr=4; afib_pr=1; chb_pr=1;
              avrgrg=1; lmtany=1; rca50=0; lcx70=1; ms_pmi=0; ms_cadsy=0;  

              hx_cva=1; hx_copd=1; hx_popdz=1; hx_dmtrt=0; hx_iddm=0; chol_pr=170;  
              hct_pr=30; hx_rnldz=1; bun_pr=32; ms_cholp=0; ms_hctp=0;           
  output;  
  
  patient=4;  age=60; 
              iv_pmi=6; ef_com=17; nyha34=0; cac_pr=0; afib_pr=1; chb_pr=1;
              avrgrg=1; lmtany=1; rca50=1; lcx70=1; ms_pmi=0; ms_cadsy=0;  

              hx_cva=1; hx_copd=1; hx_popdz=1; hx_dmtrt=0; hx_iddm=0; chol_pr=170;  
              hct_pr=40; hx_rnldz=1; bun_pr=20; ms_cholp=0; ms_hctp=0;           
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
  run;

******************************************************************************************;
* CABG+MV anuloplasty model;  
title6 "CABG + MV anuloplasty Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_cbgv out=pred_cbgv; time years; )
  run;

******************************************************************************************;
* DOR model;  
 title6 "DOR Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_dor out=pred_dor; time years; )
  run; 

******************************************************************************************;
* Heart Transplant model;  
  title6 "Heart Transplant Model";
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdead_txpl out=pred_txpl; time years; )
  run;  
 
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

data pred_cabg; set pred_cabg; if digital=0;
  ssurviv=_surviv*100;
  scllsurv=_cllsurv*100;
  sclusurv=_clusurv*100;

  shazard=_hazard*100;
  scllhaz=_cllhaz*100;
  scluhaz=_cluhaz*100;

  proc means;
  run; 

data pred_cbgv; set pred_cbgv; if digital=0;
  ssurviv=_surviv*100;
  scllsurv=_cllsurv*100;
  sclusurv=_clusurv*100;

  shazard=_hazard*100;
  scllhaz=_cllhaz*100;
  scluhaz=_cluhaz*100;

  proc means;
  run; 
  
  
data pred_dor; set pred_dor; if digital=0;
  ssurviv=_surviv*100;
  scllsurv=_cllsurv*100;
  sclusurv=_clusurv*100;

  shazard=_hazard*100;
  scllhaz=_cllhaz*100;
  scluhaz=_cluhaz*100;

  proc means;
  run; 
  
  
data pred_txpl; set pred_txpl; if digital=0;
  ssurviv=_surviv*100;
  scllsurv=_cllsurv*100;
  sclusurv=_clusurv*100;

  shazard=_hazard*100;
  scllhaz=_cllhaz*100;
  scluhaz=_cluhaz*100;

  proc means;
  run; 
*******************************************************************************;  
* Bring in PostScript plot macro                                               ;
  filename plot "!MACROS/plot.sas"; %inc plot;
  filename gsasfile pipe 'lp';
*______________________________________________________________________________;
*                                                                              ;
*                     P O S T S C R I P T   P L O T S
*______________________________________________________________________________;
*                                                                              ;
* Survival for patient 1                                                       ;
 filename gsasfile "&STUDY/graphs/hp.dead.pt1.survival.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile, end;
    id l="&STUDY/graphs/hp.dead.patients.sas survival", end;
    labelx l="Years",end;
      axisx order=(0 to 9 by 1), minor=none, end;
    labely l="Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
    
    tuple set=pred_cabg(where=(patient=1)), x=years, y=ssurviv, linepe=2, end;
    
    tuple set=pred_cbgv(where=(patient=1)),x=years, y=ssurviv,linepe=21,end;
   
    tuple set=pred_dor(where=(patient=1)), x=years, y=ssurviv,linepe=22, end;  
  
    tuple set=pred_txpl(where=(patient=1)), x=years, y=ssurviv, linepe=23,end;  
       
  );
  run;
*********************************************************************************;  
* Survival for patient 2                                                       ;
 filename gsasfile "&STUDY/graphs/hp.dead.pt2.survival.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile, end;
    id l="&STUDY/graphs/hp.dead.patients.sas survival", end;
    labelx l="Years",end;
      axisx order=(0 to 9 by 1), minor=none, end;
    labely l="Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
    
    tuple set=pred_cabg(where=(patient=2)), x=years, y=ssurviv, linepe=2, end;
    
    tuple set=pred_cbgv(where=(patient=2)),x=years, y=ssurviv,linepe=21,end;
   
    tuple set=pred_dor(where=(patient=2)), x=years, y=ssurviv,linepe=22, end;  
  
    tuple set=pred_txpl(where=(patient=2)), x=years, y=ssurviv, linepe=23,end;  
       
  );
  run;  
*********************************************************************************;  
* Survival for patient 3                                                       ;
 filename gsasfile "&STUDY/graphs/hp.dead.pt3.survival.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile, end;
    id l="&STUDY/graphs/hp.dead.patients.sas survival", end;
    labelx l="Years",end;
      axisx order=(0 to 9 by 1), minor=none, end;
    labely l="Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
    
    tuple set=pred_cabg(where=(patient=3)), x=years, y=ssurviv, linepe=2, end;
    
    tuple set=pred_cbgv(where=(patient=3)),x=years, y=ssurviv,linepe=21,end;
   
    tuple set=pred_dor(where=(patient=3)), x=years, y=ssurviv,linepe=22, end;  
  
    tuple set=pred_txpl(where=(patient=3)), x=years, y=ssurviv, linepe=23,end;  
       
  );
  run;    
*********************************************************************************;  
* Survival for patient 4                                                       ;
 filename gsasfile "&STUDY/graphs/hp.dead.pt4.survival.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile, end;
    id l="&STUDY/graphs/hp.dead.patients.sas survival", end;
    labelx l="Years",end;
      axisx order=(0 to 9 by 1), minor=none, end;
    labely l="Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
    
    tuple set=pred_cabg(where=(patient=4)), x=years, y=ssurviv, linepe=2, end;
    
    tuple set=pred_cbgv(where=(patient=4)),x=years, y=ssurviv,linepe=21,end;
   
    tuple set=pred_dor(where=(patient=4)), x=years, y=ssurviv,linepe=22, end;  
  
    tuple set=pred_txpl(where=(patient=4)), x=years, y=ssurviv, linepe=23,end;  
       
  );
  run;    
  
*______________________________________________________________________________;
*                                                                              ;
*                          C G M   P L O T T I N G                             ;
*______________________________________________________________________________;
*                                                                              ;
* Survival                                                                     ;
  filename gsasfile "&STUDY/graphs/hp.dead.pt1.cgm";
  %plot(goptions gsfmode=replace, device=cgmmppa, ftext=hwcgm001, end;
    axisx order=(0 to 8 by 2), minor=none, value=(height=2.4), end;
    axisy order=(0 to 100 by 20), minor=none, value=(height=2.4),
      value=(height=2.4 j=r ' ' '20' '40' '60' '80' '100'), end;
   
    tuple set=pred_cabg(where=(patient=1)), x=years, y=ssurviv, color=yellow, width=3, end;
    tuple set=pred_cbgv(where=(patient=1)), x=years, y=ssurviv, color=cyan, width=3, end;
    tuple set=pred_dor(where=(patient=1)), x=years, y=ssurviv, color=orange, width=3, end;
    tuple set=pred_txpl(where=(patient=1)), x=years, y=ssurviv, color=red, width=3, end;
    
    
   );
  run;
*******************************************************************************;  
* Survival                                                                     ;
  filename gsasfile "&STUDY/graphs/hp.dead.pt2.cgm";
  %plot(goptions gsfmode=replace, device=cgmmppa, ftext=hwcgm001, end;
    axisx order=(0 to 8 by 2), minor=none, value=(height=2.4), end;
    axisy order=(0 to 100 by 20), minor=none, value=(height=2.4),
      value=(height=2.4 j=r ' ' '20' '40' '60' '80' '100'), end;
   
    tuple set=pred_cabg(where=(patient=2)), x=years, y=ssurviv, color=yellow, width=3, end;
    tuple set=pred_cbgv(where=(patient=2)), x=years, y=ssurviv, color=cyan, width=3, end;
    tuple set=pred_dor(where=(patient=2)), x=years, y=ssurviv, color=orange, width=3, end;
    tuple set=pred_txpl(where=(patient=2)), x=years, y=ssurviv, color=red, width=3, end;
    
    
   );
  run;
*******************************************************************************;
* Survival                                                                     ;
  filename gsasfile "&STUDY/graphs/hp.dead.pt3.cgm";
  %plot(goptions gsfmode=replace, device=cgmmppa, ftext=hwcgm001, end;
    axisx order=(0 to 8 by 2), minor=none, value=(height=2.4), end;
    axisy order=(0 to 100 by 20), minor=none, value=(height=2.4),
      value=(height=2.4 j=r ' ' '20' '40' '60' '80' '100'), end;
   
    tuple set=pred_cabg(where=(patient=3)), x=years, y=ssurviv, color=yellow, width=3, end;
    tuple set=pred_cbgv(where=(patient=3)), x=years, y=ssurviv, color=cyan, width=3, end;
    tuple set=pred_dor(where=(patient=3)), x=years, y=ssurviv, color=orange, width=3, end;
    tuple set=pred_txpl(where=(patient=3)), x=years, y=ssurviv, color=red, width=3, end;
    
    
   );
  run;
*******************************************************************************;
* Survival                                                                     ;
  filename gsasfile "&STUDY/graphs/hp.dead.pt4.cgm";
  %plot(goptions gsfmode=replace, device=cgmmppa, ftext=hwcgm001, end;
    axisx order=(0 to 8 by 2), minor=none, value=(height=2.4), end;
    axisy order=(0 to 100 by 20), minor=none, value=(height=2.4),
      value=(height=2.4 j=r ' ' '20' '40' '60' '80' '100'), end;
   
    tuple set=pred_cabg(where=(patient=4)), x=years, y=ssurviv, color=yellow, width=3, end;
    tuple set=pred_cbgv(where=(patient=4)), x=years, y=ssurviv, color=cyan, width=3, end;
    tuple set=pred_dor(where=(patient=4)), x=years, y=ssurviv, color=orange, width=3, end;
    tuple set=pred_txpl(where=(patient=4)), x=years, y=ssurviv, color=red, width=3, end;
    
    
   );
  run;
*******************************************************************************;


quit;

