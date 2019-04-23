package com.pkb.entities.enums;


import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;

/**
 * This enum describes the measurement types that PKB supports; each enum element includes
 * name, api ref, unit, optional unit for second value (if any).
 * Unit2 is null if there's no second value to collect (OR if the user didn't enter a value...)
 *
 * @author robwhelan
 */
public enum MeasurementType implements Serializable {

    WEIGHT(10, "measurementType.txt.weight", "weight", Category.BMI, "kg", "107647005", "2.16.840.1.113883.6.96", "SCT") {//replaced "General appraisal of patient weight (procedure) [404639000]"
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    //long id, String name, String apiRef, Category category, String unit, String unit2,
    //String code, String code2, String codeOfWhole, String codeSystem, String codeSystemName, boolean derived

    WEIGHT_LBS_OZ(12, "measurementType.txt.weight", "weightLbsOz", Category.BMI, "pounds", "ounces", null, null, "107647005", "2.16.840.1.113883.6.96", "SCT", false) {
        @Override
        public MeasurementType getParentType() {
            return WEIGHT;
        }
    },
    WEIGHT_STONE(13, "measurementType.txt.weight", "weightStone", Category.BMI, "stone", "pounds", null, null, "107647005", "2.16.840.1.113883.6.96", "SCT", false) {
        @Override
        public MeasurementType getParentType() {
            return WEIGHT;
        }
    },
    HEIGHT(20, "measurementType.txt.height", "height", Category.BMI, "cm", "162755006", "2.16.840.1.113883.6.96", "SCT") {// replaced "Sitting height (observable entity) [248335006]"
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    HEIGHT_FTIN(21, "measurementType.txt.height", "heightFtIn", Category.BMI, "feet", "inches", null, null, "162755006", "2.16.840.1.113883.6.96", "SCT", false) {
        @Override
        public MeasurementType getParentType() {
            return HEIGHT;
        }
    },
    // difference between "finding" and "observational entity"... all actual measurements are findings; but we use the observational entity to indicate the type of BP reading (patient standing, supine, etc.)
    // this is better than the first iteration, but still needs review.
    //                          name                                           apiRef                                         unit1              unit2               code1        code2        codeOfWhole
    BLOOD_PRESSURE(30, "measurementType.txt.blood_pressure", "bloodPressure", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", "75367002", "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_SITTING(31, "measurementType.txt.blood_pressure_sitting", "bloodPressureSitting", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", "163035008", "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_STANDING(32, "measurementType.txt.blood_pressure_standing", "bloodPressureStanding", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", "163034007", "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_SUPINE(33, "measurementType.txt.blood_pressure_supine", "bloodPressureSupine", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", "163033001", "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_LEFT_ARM(34, "measurementType.txt.blood_pressure_left_arm", "bloodPressureLeftArm", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", null, "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_RIGHT_ARM(35, "measurementType.txt.blood_pressure_right_arm", "bloodPressureRightArm", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", null, "2.16.840.1.113883.6.96", "SCT", false),
    BLOOD_PRESSURE_AFTER_STANDING_UP_2_3_MINS(36, "measurementType.txt.blood_pressure_after_standing_up_2_3_mins", "bloodPressureAfterStandingUp2to3mins", Category.VITAL_SIGNS, "mmHg (systolic)", "mmHg (diastolic)", "163030003", "163031004", null, "2.16.840.1.113883.6.96", "SCT", false),

    TEMPERATURE(40, "measurementType.txt.temperature", "temperature", Category.VITAL_SIGNS, "degrees Celcius", "105723007", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    // note: body temp code does NOT imply unit
    TEMPERATURE_F(41, "measurementType.txt.temperature", "temperatureF", Category.VITAL_SIGNS, "degrees Fahrenheit", "105723007", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public MeasurementType getParentType() {
            return TEMPERATURE;
        }
    },
    PULSE(50, "measurementType.txt.pulse", "pulse", Category.VITAL_SIGNS, "bpm", "162986007", "2.16.840.1.113883.6.96", "SCT"),
    RESPIRATION(60, "measurementType.txt.respiration", "respiration", Category.VITAL_SIGNS, "rpm", "162913005", "2.16.840.1.113883.6.96", "SCT"),
    PEAK_FLOW(70, "measurementType.txt.peak_flow", "peakFlow", Category.BREATHING, "L/min", "366155002", "2.16.840.1.113883.6.96", "SCT"),

    BODY_SURFACE_AREA(73, "measurementType.txt.body_surface_area", "bodySurfaceArea", Category.VITAL_SIGNS, "square metres", "301898006", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    CENTRAL_VENOUS_PRESSURE(77, "measurementType.txt.central_venous_pressure", "centralVenousPressure", Category.VITAL_SIGNS, "cmH20", "366162006", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },

    FETAL_HEART_RATE(80, "measurementType.txt.fetal_heart_rate", "fetalHeartRate", Category.CHILDHOOD, "bpm", "249043002", "2.16.840.1.113883.6.96", "SCT"),
    FETAL_HEART_BASELINE(81, "measurementType.txt.fetal_heart_rate_baseline", "fetalHeartRateBaseline", Category.PREGNANCY, "bpm", "251670001", "2.16.840.1.113883.6.96", "SCT"),
    FETAL_HEART_ACCELERATION(82, "measurementType.txt.fetal_heart_rate_acceleration", "fetalHeartRateAcceleration", Category.PREGNANCY, "bpm", "251676007", "2.16.840.1.113883.6.96", "SCT"),
    FETAL_HEART_VARIABILITY(83, "measurementType.txt.fetal_heart_rate_variability", "fetalHeartRateVariability", Category.PREGNANCY, "bpm", "251671002", "2.16.840.1.113883.6.96", "SCT"),
    FETAL_HEART_DECELERATION(84, "measurementType.txt.fetal_heart_rate_deceleration", "fetalHeartRateDeceleration", Category.PREGNANCY, "bpm", "251673004", "2.16.840.1.113883.6.96", "SCT"),

    MATERNAL_PULSE_RATE(85, "measurementType.txt.maternal_pulse_rate", "maternalPulseRate", Category.PREGNANCY, "bpm", "78564009", "2.16.840.1.113883.6.96", "SCT"),
    CONTRACTIONS_PER_10MIN(86, "measurementType.txt.contractions_per_10min", "contractionsPer10Min", Category.PREGNANCY, "bpm", "289700000", "2.16.840.1.113883.6.96", "SCT"),

    HEAD_CIRCUMFERENCE(90, "measurementType.txt.head_circumference", "headCircumference", Category.CHILDHOOD, "cm", "301338002", "2.16.840.1.113883.6.96", "SCT"),

    SPO2(100, "measurementType.txt.spo2_oxygen_saturation", "spo2", Category.BREATHING, "%", "431314004", "2.16.840.1.113883.6.96", "SCT"),
    FEV1_LPM(110, "measurementType.txt.fev1_forced_expiratory_volumne_in_1_sec", "fev1Lpm", Category.BREATHING, "l/min", null, null, null),
    FEV1_PCT(120, "measurementType.txt.fev1_forced_expiratory_volumne_in_1_sec", "fev1Pct", Category.BREATHING, "% of predicted", null, null, null),

    FVC_LPM(130, "measurementType.txt.fvc_forced_vital_capacity", "fvcLpm", Category.BREATHING, "l/min", null, null, null),
    FVC_PCT(140, "measurementType.txt.fvc_forced_vital_capacity", "fvcPct", Category.BREATHING, "% of predicted", null, null, null),

    PEF(150, "measurementType.txt.pef_peak_expiratory_flow", "pef", Category.BREATHING, "l/s", "366156001", "2.16.840.1.113883.6.96", "SCT"),

    BMI(160, "measurementType.txt.bmi_body_mass_index", "bmi", Category.BMI, "kg/m^2", "301331008", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    // *Shouldn't* have been stored anywhere, but let's not delete quite yet
    //QRISK2(162, "measurementType.txt.qrisk2_score", "qrisk2", Category.BMI, "%") {
    //    public String format(Double v) {
    //        if( v == null )
    //            return "";
    //        return getOnePlaceValueFormat().format(v);
    //    }
    //},
    IDEAL_BODY_WEIGHT(164, "measurementType.txt.ideal_body_weight", "idealBodyWeight", Category.BMI, "kg", "170804003", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },

    FAT_FREE_MASS(170, "measurementType.txt.fat_free_mass", "fatFreeMass", Category.BMI, "kg", null, null, null),
    FAT_RATIO(171, "measurementType.txt.fat_ratio", "fatRatio", Category.BMI, "%", null, null, null),
    FAT_MASS_WEIGHT(172, "measurementType.txt.fat_mass_weight", "fatMassWeight", Category.BMI, "kg", null, null, null),

    WAIST_SIZE(180, "measurementType.txt.waist_size", "waistSize", Category.BMI, "cm", "276361009", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public String format(Double v) {
            if (v == null) {
                return "";
            }
            return getOnePlaceValueFormat().format(v);
        }
    },
    WAIST_SIZE_IN(181, "measurementType.txt.waist_size", "waistSizeIn", Category.BMI, "inches", "276361009", "2.16.840.1.113883.6.96", "SCT") {
        @Override
        public MeasurementType getParentType() {
            return WAIST_SIZE;
        }
    },

    HIP_CIRCUMFERENCE(182, "measurementType.txt.hip_circumference", "hipCircumference", Category.VITAL_SIGNS, "cm", null, null, null),
    CLINICAL_OPIATE_WITHDRAWAL_SCALE(183, "measurementType.txt.clinical_opiate_withdrawal_scale", "clinicalOpiateWithdrawalScale", Category.VITAL_SIGNS, "", "1086221000000103", "2.16.840.1.113883.6.96", "SCT"),
    CLINICAL_INSTITUTE_WITHDRAWAL_ASSESSMENT_ALCOHOL(184, "measurementType.txt.clinical_institute_withdrawal_assessment_alcohol", "clinicalInstituteWithdrawalAssessmentAlcohol", Category.VITAL_SIGNS, "", "445504001", "2.16.840.1.113883.6.96", "SCT"),
    EXPIRED_CARBON_MONOXIDE_CONCENTRATION(185, "measurementType.txt.expired_carbon_monoxide_concentration", "expiredCarbonMonoxideConcentration", Category.VITAL_SIGNS, "ppm", "251900003", "2.16.840.1.113883.6.96", "SCT"),
    NATIONAL_EARLY_WARNING_SCORE(186, "measurementType.txt.national_early_warning_score", "nationalEarlyWarningScore", Category.VITAL_SIGNS, "", "859771000000107", "2.16.840.1.113883.6.96", "SCT"),
    LEVEL_OF_CONSCIOUSNESS(187, "measurementType.txt.level_of_consciousness", "levelOfConsciousness", Category.VITAL_SIGNS, "", "6942003", "2.16.840.1.113883.6.96", "SCT"),
    ON_OXYGEN_THERAPY(188, "measurementType.txt.on_oxygen_therapy", "onOxygenTherapy", Category.VITAL_SIGNS, "%", "866701000000100", "2.16.840.1.113883.6.96", "SCT"),

    // Strength
    MUAC(200, "measurementType.txt.muac", "muac", Category.STRENGTH, "cm", null, null, null),
    BICEP_SKINFOLD_THICKNESS(210, "measurementType.txt.bicep_skinfold_thickness", "bicepSkinfoldThickness", Category.STRENGTH, "mm", null, null, null),
    TRICEP_SKINFOLD_THICKNESS(220, "measurementType.txt.tricep_skinfold_thickness", "tricepSkinfoldThickness", Category.STRENGTH, "mm", null, null, null),
    SUBSCAPULAR_SKINFOLD_THICKNESS(230, "measurementType.txt.subscapular_skinfold_thickness", "subscapularSkinfoldThickness", Category.STRENGTH, "mm", null, null, null),
    SUPRA_ILIAC_SKINFOLD_THICKNESS(240, "measurementType.txt.supra_iliac_skinfold_thickness", "supraIliacSkinfoldThickness", Category.STRENGTH, "mm", null, null, null),
    HAND_GRIP(250, "measurementType.txt.hand_grip", "handGrip", Category.STRENGTH, "kg", null, null, null),
    //    BIA_STANDING(260, "BIA standing", "biaStanding", Category.STRENGTH, "", null,null,null),
    //    BIA_LYING(270, "BIA lying", "biaLying", Category.STRENGTH, "", null,null,null),

    // Acute Tech integration
    MOTION_SENSED(300, "measurementType.txt.motion_sensed", "motionSensed", Category.VITAL_SIGNS, "location", null, null, null),

    // Nutrition
    CALORIES_CONSUMED(310, "measurementType.txt.calories_consumed", "caloriesConsumed", Category.NUTRITION, "calories", null, null, null),
    CARBOHYDRATES(320, "measurementType.txt.carbohydrates", "carbohydrates", Category.NUTRITION, "g", null, null, null),
    FAT(330, "measurementType.txt.fat", "fat", Category.NUTRITION, "g", null, null, null),
    FIBER(340, "measurementType.txt.fiber", "fiber", Category.NUTRITION, "g", null, null, null),
    PROTEIN(350, "measurementType.txt.protein", "protein", Category.NUTRITION, "g", null, null, null),
    SODIUM(360, "measurementType.txt.sodium", "sodium", Category.NUTRITION, "mg", null, null, null),
    WATER(370, "measurementType.txt.water", "water", Category.NUTRITION, "floz", null, null, null),

    // Deliberately NOT converted to WATER since UK and US fl oz are different.
    WATER_IN_L(371, "measurementType.txt.water_in_litres", "waterInLitres", Category.NUTRITION, "L", "226354008", "2.16.840.1.113883.6.96", "SCT"),

    MEAL(380, "measurementType.txt.meal", "meal", Category.NUTRITION, "", null, null, null),

    // Deliberately NOT in the DRUGS_ALCOHOL since that is hidden from user view
    // (see com.pkb.action.test.AddMeasurementsAction.input)
    ALCHOL_UNITS_PER_DAY(781, "measurementType.txt.alcohol_units_per_day", "alcoholUnitsPerDay", Category.NUTRITION, "units", "160573003", "2.16.840.1.113883.6.96", "SCT"),
    NON_CAFFEINATED_DRINKS_PER_DAY(790, "measurementType.txt.noncaffeinated_drinks_per_day", "nonCaffeinatedDrinksPerDay", Category.NUTRITION, "cups", null, null, null),

    // Fitness
    ACTIVITY_TYPE(400, "measurementType.txt.activity_type", "activityType", Category.FITNESS, "", null, null, null),
    ARM_ACTIVITY_RATE_0_10(401, "measurementType.txt.arm_activity_rate_0_10", "armActivityRate010", Category.FITNESS, "", null, null, null),
    WALK_ACTIVITY_RATE_0_10(402, "measurementType.txt.walk_activity_rate_0_10", "walkActivityRate010", Category.FITNESS, "", null, null, null),
    INTENSITY(410, "measurementType.txt.intensity", "intensity", Category.FITNESS, "", null, null, null),
    START_TIME(420, "measurementType.txt.start_time", "startTime", Category.FITNESS, "", null, null, null),
    DISTANCE_FITNESS(430, "measurementType.txt.distance_fitness", "distanceFitness", Category.FITNESS, "m", null, null, null),
    DURATION(440, "measurementType.txt.duration", "duration", Category.FITNESS, "s", null, null, null),
    CALORIES_BURNED_FITNESS(450, "measurementType.txt.calories_burned_fitness", "caloriesBurnedFitness", Category.FITNESS, "calories", null, null, null),
    FATIGUE_AFTER_ACTIVITY_0_10(451, "measurementType.txt.fatigue_after_activity_0_10", "fatigueAfterActivity010", Category.FITNESS, "", null, null, null),

    CIGARETTES_PER_DAY(455, "measurementType.txt.cigarettes_per_day", "cigarettesPerDay", Category.FITNESS, "cigarettes", null, null, null),

    // Routine
    STEPS(500, "measurementType.txt.steps", "steps", Category.ROUTINE, "", null, null, null),
    STANDING(505, "measurementType.txt.standing", "standing", Category.ROUTINE, "", null, null, null),
    DISTANCE_ROUTINE(510, "measurementType.txt.distance_routine", "distanceRoutine", Category.ROUTINE, "m", null, null, null),
    FLOORS(520, "measurementType.txt.floors", "floors", Category.ROUTINE, "", null, null, null),
    ELEVATION(530, "measurementType.txt.elevation", "elevation", Category.ROUTINE, "m", null, null, null),
    CALORIES_BURNED_ROUTINE(540, "measurementType.txt.calories_burned_routine", "caloriesBurnedRoutine", Category.ROUTINE, "calories", null, null, null),
    GENERAL_FATIGUE_0_10(550, "measurementType.txt.general_fatigue_0_10", "generalFatigue010", Category.ROUTINE, "", null, null, null),

    // Sleep
    AWAKE(600, "measurementType.txt.awake", "awake", Category.SLEEP, "s", null, null, null),
    DEEP(610, "measurementType.txt.deep", "deep", Category.SLEEP, "s", null, null, null),
    LIGHT(620, "measurementType.txt.light", "light", Category.SLEEP, "s", null, null, null),
    REM(630, "measurementType.txt.rem", "rem", Category.SLEEP, "s", null, null, null),
    TIMES_WOKEN(640, "measurementType.txt.times_woken", "timesWoken", Category.SLEEP, "", null, null, null),
    TOTAL_SLEEP(650, "measurementType.txt.total_sleep", "totalSleep", Category.SLEEP, "s", null, null, null),

    // Perinatal

    BIRTH_WEIGHT_CENTILE(700, "measurementType.txt.birth_weight_centile", "birthWeightCentile", Category.PREGNANCY, "s", null, null, null),
    FUNDAL_HEIGHT(710, "measurementType.txt.fundal_height", "fundalHeight", Category.PREGNANCY, "s", null, null, null),
    ROLLUPS_PER_DAY(720, "measurementType.txt.rollsups_per_day", "rollsupsPerDay", Category.DRUG_ALCOHOL, "s", null, null, null),
    CANNABIS_PER_DAY(730, "measurementType.txt.cannabis_per_day", "cannabisPerDay", Category.DRUG_ALCOHOL, "s", null, null, null),
    SHISHA_PER_DAY(740, "measurementType.txt.shisha_per_day", "shishaPerDay", Category.DRUG_ALCOHOL, "", null, null, null),
    CO_SCREENING(750, "measurementType.txt.co_screening", "coScreening", Category.DRUG_ALCOHOL, "ppm", null, null, null),
    E_CIGARETTES_PER_DAY(760, "measurementType.txt.e_cigarettes_per_day", "eCigarettesPerDay", Category.DRUG_ALCOHOL, "cigarettes", null, null, null),
    ALCOHOL_UNITS_PER_WEEK_PRE_PREGNANCY(770, "measurementType.txt.alcohol_units_per_week_pre_pregnancy", "alcoholUnitsPerWeekPrePregnancy", Category.DRUG_ALCOHOL, "s", null, null, null),
    ALCOHOL_UNITS_PER_WEEK_CURRENTLY(780, "measurementType.txt.alcohol_units_per_week_currently", "alcoholUnitsPerWeekCurrently", Category.DRUG_ALCOHOL, "s", null, null, null),

    // Yuck

    URINE_VOLUME(800, "measurementType.txt.urine_volume", "urineVolume", Category.VITAL_SIGNS, "ml", null, null, null),
    STOOL_VOLUME(810, "measurementType.txt.stool_volume", "stoolVolume", Category.VITAL_SIGNS, "ml", null, null, null),
    VOMIT_VOLUME(820, "measurementType.txt.vomit_volume", "vomitVolume", Category.VITAL_SIGNS, "ml", null, null, null),

    // 10/11/2015

    LIVER_VOLUME(900, "measurementType.txt.liver_volume", "liverVolume", Category.VITAL_SIGNS, "ml", null, null, null),
    SPLEEN_VOLUME(910, "measurementType.txt.spleen_volume", "spleenVolume", Category.VITAL_SIGNS, "ml", null, null, null),
    QCSI(920, "measurementType.txt.qcsi", "qcsi", Category.VITAL_SIGNS, "%", null, null, null),
    BONE_MARROW_BURDEN_SCORE(930, "measurementType.txt.bone_marrow_burden_score", "boneMarrowBurdenScore", Category.VITAL_SIGNS, "", null, null, null),
    FEMORAL_NECK_T_SCORE(940, "measurementType.txt.femoral_neck_t_score", "femoralNeckTScore", Category.VITAL_SIGNS, "", null, null, null),
    FEMORAL_NECK_Z_SCORE(950, "measurementType.txt.femoral_neck_z_score", "femoralNeckZScore", Category.VITAL_SIGNS, "", null, null, null),
    LUMBAR_SPINE_T_SCORE(960, "measurementType.txt.lumbar_spine_t_score", "lumbarSpineTScore", Category.VITAL_SIGNS, "", null, null, null),
    LUMBAR_SPINE_Z_SCORE(970, "measurementType.txt.lumbar_spine_z_score", "lumbarSpineZScore", Category.VITAL_SIGNS, "", null, null, null),
    VC_MAX_L_PER_MIN(980, "measurementType.txt.vc_max", "vcMaxLPerMin", Category.VITAL_SIGNS, "l/min", null, null, null),
    VC_MAX_PERCENT_OF_PREDICTED(981, "measurementType.txt.vc_max", "vcMaxPercentOfPredicted", Category.VITAL_SIGNS, "% of predicted", null, null, null),
    MEF_50_L_PER_SEC(990, "measurementType.txt.mef_50", "mef50LPerSec", Category.VITAL_SIGNS, "l/s", null, null, null),
    MEF_50_PERCENT(1000, "measurementType.txt.mef_50", "mef50Percent", Category.VITAL_SIGNS, "%", null, null, null),
    FIV1_L_PER_MIN(1010, "measurementType.txt.fiv1", "fiv1", Category.VITAL_SIGNS, "l/min", null, null, null),
    FIV1_PERCENT(1020, "measurementType.txt.fiv1", "fiv1Percent", Category.VITAL_SIGNS, "%", null, null, null),
    TLCO_SB(1040, "measurementType.txt.tlco_sb", "tlcoSb", Category.VITAL_SIGNS, "mmol/min/kPa", null, null, null),
    TLCOC_SB(1050, "measurementType.txt.tlcoc_sb", "tlcocSb", Category.VITAL_SIGNS, "mmol/min/kPa", null, null, null),
    TLCOc_VA(1060, "measurementType.txt.tlcoc_va", "tlcocVa", Category.VITAL_SIGNS, "mmol/min/kPa/l", null, null, null),
    VA(1070, "measurementType.txt.va", "va", Category.VITAL_SIGNS, "l", null, null, null),
    IVC(1080, "measurementType.txt.ivc", "ivc", Category.VITAL_SIGNS, "l", null, null, null),

    // Range of motion
    LEFT_SHOULDER_ABDUCTION(1100, "measurementType.txt.left_shoulder_abduction", "leftShoulderAbduction", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_SHOULDER_EXTERNAL_ROTATION(1110, "measurementType.txt.left_shoulder_external_rotation", "leftShoulderExternalRotation", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_ELBOW_EXTENSION(1120, "measurementType.txt.left_elbow_extension", "leftElbowExtension", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_ELBOW_ANGLE_SUPINATION(1130, "measurementType.txt.left_elbow_angle_supination", "leftElbowAngleSupination", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_ELBOW_FLEXION(1140, "measurementType.txt.left_elbow_flexion", "leftElbowFlexion", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_WRIST_EXTENSION_WITH_FLEXED_FINGERS(1150, "measurementType.txt.left_wrist_extension_with_flexed_fingers", "leftWristExtensionWithFlexedFingers", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_FINGER_EXTENSION_WITH_WRIST_FLEXED(1160, "measurementType.txt.left_finger_extension_with_wrist_flexed", "leftFingerExtensionWithWristFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_FINGER_EXTENSION_WITH_WRIST_EXTENDED(1170, "measurementType.txt.left_finger_extension_with_wrist_extended", "leftFingerExtensionWithWristExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_THUMB_EXTENSION_WITH_WRIST_EXTENDED(1180, "measurementType.txt.left_thumb_extension_with_wrist_extended", "leftThumbExtensionWithWristExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_HIP_SPREAD_ANGLE_FLEXED(1190, "measurementType.txt.left_hip_spread_angle_flexed", "leftHipSpreadAngleFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_HIP_SPREAD_ANGLE_EXTENDED(1200, "measurementType.txt.left_hip_spread_angle_extended", "leftHipSpreadAngleExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_KNEE_ANGLE_FLEXED(1210, "measurementType.txt.left_knee_angle_flexed", "leftKneeAngleFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_KNEE_ANGLE_EXTENDED(1220, "measurementType.txt.left_knee_angle_extended", "leftKneeAngleExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_KNEE_POPLITEAL_ANGLE(1230, "measurementType.txt.left_knee_popliteal_angle", "leftKneePoplitealAngle", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_KNEE_FLEXION_WHILE_LYING_ON_FRONT(1240, "measurementType.txt.left_knee_flexion_while_lying_on_front", "leftKneeFlexionWhileLyingOnFront", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_ANKLE_DORSIFLEXION_KNEE_FLEXED(1250, "measurementType.txt.left_ankle_dorsiflexion_knee_flexed", "leftAnkleDorsiflexionKneeFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    LEFT_ANKLE_DORSIFLEXION_KNEE_EXTENDED(1260, "measurementType.txt.left_ankle_dorsiflexion_knee_extended", "leftAnkleDorsiflexionKneeExtended", Category.VITAL_SIGNS, "degrees", null, null, null),

    RIGHT_SHOULDER_ABDUCTION(1101, "measurementType.txt.right_shoulder_abduction", "rightShoulderAbduction", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_SHOULDER_EXTERNAL_ROTATION(1111, "measurementType.txt.right_shoulder_external_rotation", "rightShoulderExternalRotation", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_ELBOW_EXTENSION(1121, "measurementType.txt.right_elbow_extension", "rightElbowExtension", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_ELBOW_ANGLE_SUPINATION(1131, "measurementType.txt.right_elbow_angle_supination", "rightElbowAngleSupination", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_ELBOW_FLEXION(1141, "measurementType.txt.right_elbow_flexion", "rightElbowFlexion", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_WRIST_EXTENSION_WITH_FLEXED_FINGERS(1151, "measurementType.txt.right_wrist_extension_with_flexed_fingers", "rightWristExtensionWithFlexedFingers", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_FINGER_EXTENSION_WITH_WRIST_FLEXED(1161, "measurementType.txt.right_finger_extension_with_wrist_flexed", "rightFingerExtensionWithWristFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_FINGER_EXTENSION_WITH_WRIST_EXTENDED(1171, "measurementType.txt.right_finger_extension_with_wrist_extended", "rightFingerExtensionWithWristExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_THUMB_EXTENSION_WITH_WRIST_EXTENDED(1181, "measurementType.txt.right_thumb_extension_with_wrist_extended", "rightThumbExtensionWithWristExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_HIP_SPREAD_ANGLE_FLEXED(1191, "measurementType.txt.right_hip_spread_angle_flexed", "rightHipSpreadAngleFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_HIP_SPREAD_ANGLE_EXTENDED(1201, "measurementType.txt.right_hip_spread_angle_extended", "rightHipSpreadAngleExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_KNEE_ANGLE_FLEXED(1211, "measurementType.txt.right_knee_angle_flexed", "rightKneeAngleFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_KNEE_ANGLE_EXTENDED(1221, "measurementType.txt.right_knee_angle_extended", "rightKneeAngleExtended", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_KNEE_POPLITEAL_ANGLE(1231, "measurementType.txt.right_knee_popliteal_angle", "rightKneePoplitealAngle", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_KNEE_FLEXION_WHILE_LYING_ON_FRONT(1241, "measurementType.txt.right_knee_flexion_while_lying_on_front", "rightKneeFlexionWhileLyingOnFront", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_ANKLE_DORSIFLEXION_KNEE_FLEXED(1251, "measurementType.txt.right_ankle_dorsiflexion_knee_flexed", "rightAnkleDorsiflexionKneeFlexed", Category.VITAL_SIGNS, "degrees", null, null, null),
    RIGHT_ANKLE_DORSIFLEXION_KNEE_EXTENDED(1261, "measurementType.txt.right_ankle_dorsiflexion_knee_extended", "rightAnkleDorsiflexionKneeExtended", Category.VITAL_SIGNS, "degrees", null, null, null),

    // physio measurements (0-10 rating scale)
    LEG_STIFFNESS_0_10(1300, "measurementType.txt.leg_stiffness_0_10", "legStiffness010", Category.VITAL_SIGNS, "", null, null, null),
    LEG_CRAMPS_0_10(1310, "measurementType.txt.leg_cramps_0_10", "legCramps010", Category.VITAL_SIGNS, "", null, null, null),
    LEG_PAIN_IN_REST_0_10(1320, "measurementType.txt.leg_pain_in_rest_0_10", "legPainInRest010", Category.VITAL_SIGNS, "", null, null, null),
    LEG_PAIN_IN_MOVEMENT_0_10(1330, "measurementType.txt.leg_pain_in_movement_0_10", "legPainInMovement010", Category.VITAL_SIGNS, "", null, null, null),
    FOOT_DRAGGING_GAIT_ANALYSIS_0_10(1340, "measurementType.txt.foot_dragging_gait_analysis_0_10", "footDraggingGaitAnalysis010", Category.VITAL_SIGNS, "", null, null, null),
    KNEE_HYPER_EXTENSION_GAIT_ANALYSIS_0_10(1350, "measurementType.txt.knee_hyper_extension_gait_analysis_0_10", "kneeHyperExtensionGaitAnalysis010", Category.VITAL_SIGNS, "", null, null, null),
    LEG_SCISSORING_GAIT_ANALYSIS_0_10(1360, "measurementType.txt.leg_scissoring_gait_analysis_0_10", "legScissoringGaitAnalysis010", Category.VITAL_SIGNS, "", null, null, null),
    ARM_STIFFNESS_0_10(1370, "measurementType.txt.arm_stiffness_0_10", "armStiffness010", Category.VITAL_SIGNS, "", null, null, null),
    ARM_CRAMPS_0_10(1380, "measurementType.txt.arm_cramps_0_10", "armCramps010", Category.VITAL_SIGNS, "", null, null, null),
    ARM_PAIN_IN_REST_0_10(1390, "measurementType.txt.arm_pain_in_rest_0_10", "armPainInRest010", Category.VITAL_SIGNS, "", null, null, null),
    ARM_PAIN_IN_MOVEMENT_0_10(1400, "measurementType.txt.arm_pain_in_movement_0_10", "armPainInMovement010", Category.VITAL_SIGNS, "", null, null, null),
    NUMBER_OF_TIMES_I_NEARLY_FELL(1410, "measurementType.txt.number_of_times_i_nearly_fell", "numberOfTimesINearlyFell", Category.VITAL_SIGNS, "", null, null, null),
    PAIN_SCALE_0_10(1420, "measurementType.txt.pain_scale_0_10", "painScale010", Category.VITAL_SIGNS, "", null, null, null),

    // Other ?
    MEDICATION_COMPLIANCE(1500, "measurementType.txt.medication_compliance", "medicationCompliance", Category.VITAL_SIGNS, "%", null, null, null);

    public enum Category {
        // the toString() value is a i18n property lookup (...after a prefix?)
        BMI, // BMI (Body mass index)
        VITAL_SIGNS, // Vital signs
        STRENGTH, // Strength
        BREATHING, // Breathing
        CHILDHOOD, // Childhood
        NUTRITION, // Nutrition
        FITNESS, // Fitness
        ROUTINE, // Routine
        SLEEP, // Sleep
        PREGNANCY,
        DRUG_ALCOHOL, // smoking, drugs , alcohol etc
    }

    public enum DisplayCategory {
        GENERAL("myMeasurements.txt.general"),
        NUTRITION("myMeasurements.txt.nutrition"),
        FITNESS("myMeasurements.txt.fitness"),
        @Deprecated // Not used anymore. Routine category belongs to fitness display category.
                ROUTINE("myMeasurements.txt.routine"),
        SLEEP("myMeasurements.txt.sleep");

        private final String text;

        DisplayCategory(String text) {
            this.text = text;
        }

        public final String getText() {
            return text;
        }
    }

    // TODO: doesn't handle locale! can we use any default number formatter?

    private static DecimalFormat getDefaultValueFormat() {
        return new DecimalFormat("#,##0.################");
    }

    private static DecimalFormat getOnePlaceValueFormat() {
        return new DecimalFormat("#,##0.#");
    }

    private Long id;
    private String name;
    private String apiRef;
    private Category category;

    private String unit;
    private String code;
    private String codeSystem;
    private String codeSystemName;

    // for measurements like BP with two data points -- all codes use the same coding system
    private String code2;
    private String unit2;
    /* if this measurement contains 2 data points, there may be a code referring the whole thing (e.g., standing BP) */
    private String codeOfWhole;

    private boolean derived; // Derived measurements, such as BMI, cannot be directly input

    /** no code or coding system; phasing these out! */
    MeasurementType(long id, String name, String apiRef, Category category, String unit) {
        this(id, name, apiRef, category, unit, null, null, null, null, null, null, true);
    }

    /** with one data point, coded */
    MeasurementType(long id, String name, String apiRef, Category category, String unit,
                    String code, String codeSystem, String codeSystemName) {
        this(id, name, apiRef, category, unit, null, code, null, null, codeSystem, codeSystemName, false);
    }

    /** with 2 data points, coded */
    MeasurementType(long id, String name, String apiRef, Category category, String unit, String unit2,
                    String code, String code2, String codeOfWhole, String codeSystem, String codeSystemName,
                    boolean derived) {
        this.id = id;
        this.name = name;
        this.apiRef = apiRef;
        this.category = category;
        this.unit = unit;
        this.unit2 = unit2;
        this.code = code;
        this.code2 = code2;
        this.codeOfWhole = codeOfWhole;
        this.codeSystem = codeSystem;
        this.codeSystemName = codeSystemName;
        this.derived = derived;
    }

    public MeasurementType getParentType() {
        // override if this type just converts to another (e.g., WEIGHT_LBS_OZ converts to WEIGHT in GBP)
        return null;
    }

    public @NotNull List<MeasurementType> getChildTypes() {
        // use getParentType() to find out
        // loop through to find types where /this/ is parent
        return Arrays.stream(MeasurementType.values())
                .filter(mt -> mt.getParentType() != null)
                .filter(mt -> mt.getParentType().getId() == this.getId())
                .collect(Collectors.toList());
    }

    public String format(Double value) {
        // override for less precision
        if (value == null) {
            return "";
        }
        return getDefaultValueFormat().format(value);
    }

    /** used for API references. Blood pressure => bloodPressure */
    public String getApiRef() {
        return apiRef;
    }

    public String getCode() {
        return code;
    }

    public String getCodeSystem() {
        return codeSystem;
    }

    public String getCodeSystemName() {
        return codeSystemName;
    }

    // temporary: required by trim (so: fix trim and/or remove save to trim entirely!)
    public String getCodeSystem2() {
        return codeSystem;
    }

    public String getCodeSystemName2() {
        return codeSystemName;
    }

    public String getCode2() {
        return code2;
    }

    public String getCodeOfWhole() {
        return codeOfWhole;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public DisplayCategory getDisplayCategory() {
        switch (category) {
            case NUTRITION:
                return DisplayCategory.NUTRITION;
            case FITNESS:
                return DisplayCategory.FITNESS;
            case ROUTINE:
                return DisplayCategory.FITNESS;
            case SLEEP:
                return DisplayCategory.SLEEP;
            default:
                return DisplayCategory.GENERAL;
        }
    }

    public String getUnit() {
        return unit;
    }

    public String getUnit2() {
        return unit2;
    }

    public boolean isDerived() {
        return derived;
    }

    public static MeasurementType getById(long id) {
        for (MeasurementType mt : values()) {
            if (mt.id == id) {
                return mt;
            }
        }
        return null; // no match
    }

    public static MeasurementType getByApiRef(String apiRef) {
        for (MeasurementType mt : values()) {
            if (mt.getApiRef().equals(apiRef)) {
                return mt;
            }
        }
        return null; // no match
    }

    /**
     * Note that name is not necessarily unique! This will return the first match found in the enum
     * (which is always the one with the "official" unit, not the one that will be converted).
     *
     * @param name
     * @return
     */
    public static List<MeasurementType> getByName(String name) {
        List<MeasurementType> list = new ArrayList<>();
        for (MeasurementType mt : values()) {
            if (mt.getName().equals(name)) {
                list.add(mt);
            }
        }
        return list;
    }

    public static MeasurementType getByParentName(String name) {
        for (MeasurementType mt : values()) {
            if ((mt.getParentType() == null) && mt.getName().equals(name)) {
                return mt;
            }
        }

        return null;
    }

    public static Map<Category, List<MeasurementType>> getCategoryMap() {
        Map<Category, List<MeasurementType>> map = new LinkedHashMap<>();
        // load the categories into the map using their order in the enum
        for (Category cat : Category.values()) {
            // fetch measurements for this category
            map.put(cat, new ArrayList<>());
        }

        // now loop the measurement types and load them into the right lists
        // this does NOT yet permit uncategorized MeasurementTypes.
        for (MeasurementType type : values()) {
            map.get(type.getCategory()).add(type);
        }

        return map;
    }
}
