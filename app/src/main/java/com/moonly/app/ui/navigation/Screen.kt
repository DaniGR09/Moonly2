package com.moonly.app.ui.navigation

/**
 * Sealed class para todas las rutas de navegación
 */
sealed class Screen(val route: String) {

    // ==================== AUTH ====================
    data object Splash : Screen("splash")
    data object Login : Screen("login")
    data object Register : Screen("register")
    data object EmailVerification : Screen("email_verification")
    data object PasswordRecovery : Screen("password_recovery")

    // ⚠️ NUEVO: Para cuando el usuario viene del link de recuperación
    data object ResetPassword : Screen("reset_password/{token}") {
        fun createRoute(token: String) = "reset_password/$token"
    }

    // ==================== ONBOARDING ====================
    data object Onboarding : Screen("onboarding")

    // ==================== MAIN ====================
    data object Home : Screen("home")
    data object EditPeriod : Screen("edit_period")

    // ==================== SYMPTOMS ====================
    data object Emotion : Screen("emotion/{date}") {
        fun createRoute(date: String) = "emotion/$date"
    }

    data object Flow : Screen("flow/{date}") {
        fun createRoute(date: String) = "flow/$date"
    }

    data object Cravings : Screen("cravings/{date}") {
        fun createRoute(date: String) = "cravings/$date"
    }

    data object Pain : Screen("pain/{date}") {
        fun createRoute(date: String) = "pain/$date"
    }

    // ==================== PROFILE ====================
    data object Profile : Screen("profile")
    data object ProfileSettings : Screen("profile_settings")
    data object ProfileContraceptives : Screen("profile_contraceptives")
    data object ProfileMedicalConditions : Screen("profile_medical_conditions")
    data object ProfileCareMethods : Screen("profile_care_methods")

    // ==================== LEGACY (por si acaso) ====================
    data object Settings : Screen("settings")
    data object Contraceptives : Screen("contraceptives")
    data object ContraceptiveDetail : Screen("contraceptive/{contraceptiveId}") {
        fun createRoute(contraceptiveId: String) = "contraceptive/$contraceptiveId"
    }
    data object CareMethods : Screen("care_methods")
    data object CareMethodDetail : Screen("care_method/{methodId}") {
        fun createRoute(methodId: String) = "care_method/$methodId"
    }
    data object MedicalConditions : Screen("medical_conditions")
    data object MedicalConditionDetail : Screen("medical_condition/{conditionId}") {
        fun createRoute(conditionId: String) = "medical_condition/$conditionId"
    }
}