// JOB hp.dead.patients.xpt.2.sas
sas9.1 hp.dead.patients.xpt.2.sas
cp hp.dead.patients.xpt.2.l* \
    /studies/cardiac/failure/transplant/scoring_system/graphs/
spool cont printer 'lptxt -l110 -s6 -f LetterGothic-Bold'
//spool cont to email
splfile hp.dead.patients.xpt.2.l*
// FILE hp.dead.patients.xpt.2.sas
*______________________________________________________________________________;
*                                                                              ;
* /graphs/hp.dead.patients.xpt.2.sas
*______________________________________________________________________________;
*                                                                              ;
  %let STUDY=/studies/cardiac/failure/transplant/scoring_system;
*______________________________________________________________________________;
*                                                                              ;
* Risk stratification in cardiac transplantation;
* (CCF 1984 to 2003 n=987);
*
* Multivariable analysis of death
* Strategic decision suppont non-parsimoneous model
*______________________________________________________________________________;
*                                                                              ;
  options pagesize=107 linesize=132;
  libname est "&STUDY/estimates";
  title1 "Risk Stratification in Cardiac Transplantation";
  title2 "(CCF; 1984 to 2003; n=987)";
  title3 "Multivariable Analysis of Death";
  title4 "Non-Parsimoneous Decision Support Model";
*______________________________________________________________________________;
*                                                                              ;
*               M U L T I V A R I A B L E   M O D E L                          ;
*______________________________________________________________________________;
*                                                                              ;
* Early Hazard Phase:                                                          ;
*

*   HT RACE_WH NPREGPR DIAG_DCM DIAG_RCM DIAG_ICM RABO_A RABO_B RABO_O 
*   CHB_PR HXCIGL6 HX_CVA HX_COPD HX_IDDM HX_NIDDM HX_DIEDM HX_ULCR VADLS 
*   ECMOLS AICDLS IABPLS IN2BILIL CREALS IN2GFRPR PASPR2 PCWPR2 LVEFPR2 
*   RCMVGPOS LN_TPRA PRAB0_LS LN_LSYRS PRE1997 MS_CVA MS_BILIL MS_LVEF 
*   MS_PASR 
*
* Late Hazard Phase:                                                           ;

*   NPREGPR DIAG_DCM DIAG_RCM DIAG_ICM RABO_A RABO_O HX_IDDM HX_NIDDM 
*   HX_DIEDM HX_ULCR IABPLS IN2GFRPR PASPR2 RCMVGPOS MS_LVEF R_FEMALE LN_HT 
*   RACE_BL HX_CSURG HX_CHOL TRANPR HXMALIG LN_BUNLS CREALS2 LN_HGBLS PTCAPR 
*   PADPR2 IN_LVEF RHBCOPOS HCAB PRAT1_10 
*______________________________________________________________________________;
*                                                                              ;
* Definitions of variables in model                                            ;
  data design;

* Demographics;         
  r_female=0;           /* Female sex vs male                                */
  npregpr=0;            /* Number of pregnancies (women)                     */
  ht=173;               /* Height (cm) (lo 110 - hi 210)                     */
  race_wh=1;            /* Race: white vs other                              */
  race_bl=0;            /* Race: black vs other                              */

* Cardiomyopathy;
  diag_dcm=1;           /* Dilated (vs. other)                               */
  diag_rcm=0;           /* Restrictive (vs. other)                           */
  diag_icm=0;           /* Ischemic (vs. other)                              */

* Cardic comorbidity;
  lvefpr=16;            /* LV ejection fraction (lo 5, high 57)              */
  ms_lvef=0;            /* Missing value indicator for LVEF (0)              */
  chb_pr=0;             /* Complete heart block                              */
  hx_csurg=0;           /* Previous cardiac surgery                          */
  ptcapr=0;             /* Previous PCI (percutaneous coronary intervention) */
  
* Hemodynamics             
  paspr=49;             /* PA systolic pressure (mmHg)                       */
  ms_pasr=0;            /* Missing value for pulmonary artery pressures      */
  ms_papr=0;            /* Missing value for right heart cath                */
  padpr=25;             /* PA systolic pressure (mmHg)                       */
  pcwpr=24;             /* Pulmonary capillary wedge pressure (mmHg)         */

* Circulatory support at listing;                                              
  vadls=0;              /* On VAD  at listing                                */
  ecmols=0;             /* On ECMO at listing                                */
  iabpls=0;             /* On IABP at listing                                */
  aicdls=0;             /* On ICD  at listing                                */

* Immunogenetics; 
  rabo_a=1;             /* ABO blood group: A vs AB                          */
  rabo_b=0;             /* ABO blood group: B vs AB                          */
  rabo_o=0;             /* ABO blood group: O vs AB                          */
  init_t=0;             /* T-cell PRA (%)                                    */
  init_b=0;             /* B-cell PRA (%)                                    */
  rcmvgpos=1;           /* CMV positive                                      */
  rhbcopos=0;           /* Hepatitis B core antibody                         */
  hcab=0;               /* Hepatitis C antibody                              */
  tranpr=0;             /* Prior blood transfusion                           */

* Comorbid conditions and history;
  hx_cva=0;             /* CVA                                               */
  ms_cva=0;             /* Missing value for CVA                             */
  hx_chol=0;            /* History of Cholesteremia                          */
  hx_diedm=0;           /* Diabetes (diet treated)                           */
  hx_diedm=0;           /* Diabetes (drug treated)                           */
  hx_niddm=0;           /* Diabetes (insulin treated)                        */
  hx_iddm=0;            /* Diabetes (pharmacologically treated)              */
  hx_copd=0;            /* Chronic obstructive pulmonary disease             */
  hxcigl6=0;            /* Cigarette use last 6 months before listing        */
  hx_ulcr=0;            /* History of peptic ulcer disease                   */
  hxmalig=0;            /* History of malignancy                             */

* Laboratory values;
  bilils=1.5;           /* Bilirubin (mg/dL)                                 */
  ms_bilil=0;           /* Missing value for bilirubin                       */
  bunls=29;             /* BUN (mg/dL)                                       */
  creals=1.4;           /* Creatinine (mg/dL)                                */
  hgbls=12.5;           /* Hemoglobin (mg/dL)                                */

* Experience;               
  dt_list= mdy(1,1,2004);
*******************************************************************************;
* Set up test patient;
  patient=1;
    ht=183; aicdls=1; hx_csurg=1; rabo_a=0; bpraflls=1; 
    albls=4.0; bunls=17; bilils=1.6; creals=1.0; hgbls=13.6; paspr=68; 
    pcwpr=32; pre_pvr=3.0; lvefpr=20; 
    birth_dt=mdy(5,5,1945);
    list_dt=mdy(3,28,2005);
*******************************************************************************;
* Transformations of variables                                                 ;
  data design; set design;
* Demographics;
  ln_ht=log(ht);
* Cardic comorbidity;
  lvefpr2=(lvefpr/16)**2;
  in_lvef=16/lvefpr;
* Hemodynamics;             
  paspr2=(paspr/48)**2;
  padpr2=(padpr/25)**2;
  pcwpr2=(pcwpr/24)**2;
* Immunogenetics; 
  ln_tpra=log(init_t+1);    
  if init_t>0 and init_t le 10 then prat1_10=1; else prat1_10=0;
  prab0_ls=0; if init_b=0 then prab0_ls=1;
* Laboratory values;
  in_bilil=1/bilils;
  in2bilil=in_bilil**2;
  creals2=creals**2;
  ln_bunls=log(bunls);
  ln_hgbls=log(hgbls);
* Calculate creatinine clearance at listing using abbreviated MDRD method;
  creal_ln=log(creals);
  agels=(list_dt - birth_dt)/365.2425;
  agels_ln=log(agels);
  
  if race_bl ne . then 
     gfr_pr=exp(5.228 - 1.154*creal_ln - 0.203*agels_ln - 0.299*r_female + 
           0.192*race_bl);
  else gfr_pr=exp(5.228 - 1.154*creal_ln - 0.203*agels_ln - 0.299*r_female);

  in2gfrpr=(68/gfr_pr)**2;
* Date of listing;
  iv_lsyrs=(dt_list - mdy(1, 1, 1984))/365.2425;
  ln_lsyrs=log(iv_lsyrs);
  year_ls=1984+iv_lsyrs;
  if year_ls < 1997 then pre1997 = 1; else pre1997=0;
  
  proc print d;
*______________________________________________________________________________;
*                                                                              ;
* Predictions                                                                  ;
*______________________________________________________________________________;
*                                                                              ;
* Generate and output points, remembering any transforms                       ;
  data predict; set design; digital=0;
  min=-5; max=log(12); inc=(max-min)/999.9;
  do ln_yrs=min to max by inc, max; years=exp(ln_yrs); output; end;
*******************************************************************************;
* Generate time points at "nice" intervals for tabular (digital) depiction     ;
  data digital; set design; digital=1;
  do years=30/365.2425, 1 to 12 by 1; output; end;
*******************************************************************************;
* Transformations of variables                                                 ;
  data predict; set predict digital;
  run;
*******************************************************************************;
* Do predictions from multivariable model;
  libname export xport "&STUDY/estimates/hmdeadls_nonparsimoneous2.dta";
  data export.export; set est.hmdeadls_nonpars2;
  run;
  
* proc print data=est.hmdeadls_nonpars;
  %hazpred(
  proc hazpred data=predict inhaz=est.hmdeadls_nonpars2 out=predict;
       time years; 
  );
*______________________________________________________________________________;
*                                                                              ;
*                                  P L O T S
*______________________________________________________________________________;
*                                                                              ;
* Digital nomogram                                                             ;
  data digital; set predict; if digital=1;
  proc sort; by patient years;
  proc print d; by patient; var patient years
       _surviv _cllsurv _clusurv _hazard _cllhaz _cluhaz;
  run;
*******************************************************************************;
* Scale plot output (after getting rid of digital nomogram points)             ;
  data predict; set predict; if digital=0;
  title5 "Individual Patient Survival Curve";
  ssurviv=_surviv*100;
  scllsurv=_cllsurv*100;
  sclusurv=_clusurv*100;
  proc means;
  run;
*******************************************************************************;
* Printer plot survival and hazard                                             ;
  proc plot data=predict;
       plot _surviv*years=patient _cllsurv*years='.' _clusurv*years='.'
          /overlay;
*******************************************************************************;
* Bring in PostScript plot macro                                               ;
  filename  plot "!MACROS/plot.sas";  %inc plot;
  filename gsasfile pipe 'lp';
*______________________________________________________________________________;
*                                                                              ;
*                     P O S T S C R I P T   P L O T S
*______________________________________________________________________________;
*                                                                              ;
* Survival                                                                     ;
  %macro ps;
  filename gsasfile "&STUDY/graphs/hp.dead.patients1_4.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile end;
    id l="&STUDY/graphs/hp.dead.patients.sas 1-4", end;
    labelx l="Years",end;
      axisx order=(0 to 12 by 1), minor=none, end;
    labely l="Predicted Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
    tuple set=predict(where=(patient=1)), x=years, linecl=4,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
    tuple set=predict(where=(patient=2)), x=years, linecl=33, color=gray,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
    tuple set=predict(where=(patient=3)), x=years, linecl=2,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
    tuple set=predict(where=(patient=4)), x=years, linecl=35, color=gray,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
  );
  run;
  
   filename gsasfile "&STUDY/graphs/hp.dead.patients357.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile end;
    id l="&STUDY/graphs/hp.dead.patients.sas 3,5,7 ( CS, DL, RMCF)", end;
    labelx l="Years",end;
      axisx order=(0 to 12 by 1), minor=none, end;
    labely l="Predicted Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
      
    tuple set=predict(where=(patient=3)), x=years, linecl=2,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
    tuple set=predict(where=(patient=5)), x=years, linecl=4, 
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
    tuple set=predict(where=(patient=7)), x=years, linecl=33,
      y=ssurviv, cll=scllsurv, clu=sclusurv, end;
  );
  run;
  
filename gsasfile "&STUDY/graphs/hp.dead.patients357.no_cl.ps";
  %plot(goptions gsfmode=replace, gaccess=gsasfile end;
    id l="&STUDY/graphs/hp.dead.patients.sas 3,5,7( CS, DL, RMCF)", end;
    labelx l="Years",end;
      axisx order=(0 to 12 by 1), minor=none, end;
    labely l="Predicted Survival (%)",end;
      axisy order=(0 to 100 by 10), minor=none, end;
      
    tuple set=predict(where=(patient=3)), x=years, 
      y=ssurviv,   end;
    tuple set=predict(where=(patient=5)), x=years,  
      y=ssurviv,   end;
    tuple set=predict(where=(patient=7)), x=years, 
      y=ssurviv,   end;
  );
  run;  
  
  %mend ps;
* %ps;
  
*______________________________________________________________________________;
*                                                                              ;
*                          C G M   P L O T T I N G                             ;
*______________________________________________________________________________;
*                                                                              ;
* Survival                                                                     ;
  %macro cgm;
  filename gsasfile	"&STUDY/graphs/hp.dead.patients357.no_cl.cgm";
  %plot(goptions gsfmode=replace, device=cgmmppa, ftext=hwcgm001, end;
    axisx order=(0 to 10 by 2), minor=none, value=(height=2.4), end;
    axisy order=(0 to 100 by 20), minor=none, value=(height=2.4), 
    value=(height=2.4 j=r ' ' '20' '40' '60' '80' '100'), end;
    tuple set=predict(where=(patient=3)), x=years,
      y=ssurviv, color=yellow, width=3, end;
    tuple set=predict(where=(patient=5)), x=years,
      y=ssurviv, color=yellow, width=3, end;
    tuple set=predict(where=(patient=7)), x=years,
      y=ssurviv, color=yellow, width=3, end;
  );
  run;
*******************************************************************************;
  %mend cgm;
  quit;
 * %cgm;
