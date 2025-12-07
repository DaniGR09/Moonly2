package com.moonly.app.core.constants

object ApiConstants {
    // Base URL (cambiar según environment)
    const val BASE_URL = "http://10.0.2.2:8000/"  // SIN /api/

    // Timeout
    const val CONNECT_TIMEOUT = 30L // segundos
    const val READ_TIMEOUT = 30L
    const val WRITE_TIMEOUT = 30L

    // Endpoints
    object Auth {
        const val SIGNUP = "auth/signup"
        const val SIGNIN = "auth/signin"
        const val SIGNOUT = "auth/signout"
        const val RESET_PASSWORD = "auth/reset-password"
        const val CHANGE_PASSWORD = "auth/change-password"
    }

    object Profile {
        const val GET_PROFILE = "profile/me"
        const val UPDATE_PROFILE = "profile/me"
        const val FIRST_LOGIN_SETUP = "profile/first-login-setup"
        const val DELETE_ACCOUNT = "profile/me"
        const val GET_SETTINGS = "profile/settings"
        const val UPDATE_SETTINGS = "profile/settings"
        const val UPDATE_FCM_TOKEN = "profile/fcm-token"
    }

    object Cycle {
        const val CREATE_CYCLE = "cycle/cycles"
        const val GET_CYCLES = "cycle/cycles"
        const val GET_CURRENT_CYCLE = "cycle/cycles/current"
        const val UPDATE_CYCLE = "cycle/cycles/{cycle_id}"
        const val ADD_PERIOD_DAY = "cycle/period-days"
        const val ADD_PERIOD_DAYS_BATCH = "cycle/period-days/batch"
        const val GET_PERIOD_DAYS = "cycle/period-days"
        const val ADD_SYMPTOMS = "cycle/symptoms"
        const val GET_SYMPTOMS = "cycle/symptoms/{symptom_date}"
        const val UPDATE_SYMPTOMS = "cycle/symptoms/{symptom_date}"
        const val GET_OVULATION_INFO = "cycle/ovulation"
    }

    object Contraceptives {
        const val CREATE = "contraceptives/"
        const val GET_ALL = "contraceptives/"
        const val GET_BY_ID = "contraceptives/{contraceptive_id}"
        const val UPDATE = "contraceptives/{contraceptive_id}"
        const val DELETE = "contraceptives/{contraceptive_id}"
    }

    object CareMethods {
        const val CREATE = "care-methods/"
        const val GET_ALL = "care-methods/"
        const val GET_BY_ID = "care-methods/{method_id}"
        const val UPDATE = "care-methods/{method_id}"
        const val REGISTER_CHANGE = "care-methods/register-change"
        const val DELETE = "care-methods/{method_id}"
    }

    object MedicalConditions {
        const val CREATE = "medical-conditions/"
        const val GET_ALL = "medical-conditions/"
        const val GET_BY_ID = "medical-conditions/{condition_id}"
        const val UPDATE = "medical-conditions/{condition_id}"
        const val DELETE = "medical-conditions/{condition_id}"
    }

    object Notifications {
        const val GET_HISTORY = "notifications/history"
        const val PROCESS_PENDING = "notifications/process-pending"
        const val SEND_TEST = "notifications/test"
    }
}